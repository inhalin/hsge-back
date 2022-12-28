package hsge.hsgeback.service;

import hsge.hsgeback.dto.request.SignupDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.dto.response.*;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.PetImg;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.exception.NotOwnerException;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    private final S3Upload s3Upload;

    public List<PetResponseDto> findPetByLocation(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        User findUser = optional.orElseThrow(IllegalArgumentException::new);
        findUser.calculateLocation();
        List<User> userList = userRepository.findByLatitudeBetweenAndLongitudeBetween(findUser.getStartLatitude(), findUser.getEndLatitude(), findUser.getStartLongitude(), findUser.getEndLongitude());
        return userList.stream()
                .filter(v -> !Objects.equals(v.getId(), findUser.getId()))
                .map(User::getPets)
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .stream()
                .map(pet -> PetResponseDto.builder()
                        .age(pet.getAge().getKorean())
                        .breed(pet.getBreed().getKorean())
                        .name(pet.getPetName())
                        .sex(pet.getGender())
                        .isNeuter(pet.getNeutralization())
                        .petId(pet.getId())
                        .tag(new PetResponseDto.Tag(pet.getLikeTag(), pet.getDislikeTag()))
                        .petImg(pet.getPetImg().stream().map(PetImg::getS3Url).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<PetInfoResponseDto> getAllPet(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Pet> pets = user.getPets();
        return pets.stream()
                .map(pet -> PetInfoResponseDto.builder()
                        .petName(pet.getPetName())
                        .age(pet.getAge().getKorean())
                        .petImg(pet.getPetImg().stream().map(PetImg::getS3Url).collect(Collectors.toList()))
                        .description(pet.getDescription())
                        .gender(pet.getGender())
                        .neutralization(pet.getNeutralization())
                        .id(pet.getId())
                        .tag(new PetInfoResponseDto.Tag(pet.getLikeTag(), pet.getDislikeTag()))
                        .breed(pet.getBreed().getKorean())
                        .ageDto(new AgeDto(pet.getAge().toString(), pet.getAge().getKorean()))
                        .breedDto(new BreedDto(pet.getBreed().toString(), pet.getBreed().getKorean()))
                        .build())
                .collect(Collectors.toList());
    }

    public PetInfoResponseDto getOnePet(Long petId) {
        Optional<Pet> optional = petRepository.findById(petId);
        Pet pet = optional.orElseThrow();
        PetInfoResponseDto dto = new PetInfoResponseDto();
        dto.setTag(new PetInfoResponseDto.Tag(pet.getLikeTag(), pet.getDislikeTag()));
        dto.setBreedDto(new BreedDto(pet.getBreed().toString(), pet.getBreed().getKorean()));
        dto.setAgeDto(new AgeDto(pet.getAge().toString(), pet.getAge().getKorean()));
        dto.setGender(pet.getGender());
        dto.setDescription(pet.getDescription());
        dto.setNeutralization(pet.getNeutralization());
        List<PetImg> petImg = pet.getPetImg();
        List<String> urlList = petImg.stream()
                .map(h -> new UrlDto(h.getS3Url()).getImageUrl())
                .collect(Collectors.toList());
        dto.setPetImg(urlList);
        dto.setPetName(pet.getPetName());
        dto.setId(pet.getId());
        return dto;
    }


    public List<PetInfoResponseDto> getMyPet(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow();
        List<Pet> pets = user.getPets();
        return pets.stream()
                .map(pet -> PetInfoResponseDto.builder()
                        .breedDto(new BreedDto(pet.getBreed().toString(), pet.getBreed().getKorean()))
                        .petName(pet.getPetName())
                        .ageDto(new AgeDto(pet.getAge().toString(), pet.getAge().getKorean()))
                        .petImg(pet.getPetImg().stream().map(PetImg::getS3Url).collect(Collectors.toList()))
                        .gender(pet.getGender())
                        .description(pet.getDescription())
                        .neutralization(pet.getNeutralization())
                        .id(pet.getId())
                        .tag(new PetInfoResponseDto.Tag(pet.getLikeTag(), pet.getDislikeTag()))
                        .build()
                ).collect(Collectors.toList());
    }

    @Transactional
    public void postPet(String email, SignupDto signupDto, MultipartFile multipartFile) throws IOException {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.orElseThrow();
        Pet pet = petRepository.save(signupDto.toPetEntity(user));
        PetImg petImg = s3Upload.upload(multipartFile, pet);
        List<PetImg> petImgList = new ArrayList<>();
        petImgList.add(petImg);
        pet.setPetImg(petImgList);
    }

    @Transactional
    public void updatePet(String email, Long petId, UserPetDto userPetDto, MultipartFile multipartFile) throws IOException {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElseThrow();
        Long id = pet.getUser().getId();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow();
        if (!Objects.equals(id, user.getId())) {
            throw new NotOwnerException("NotOwnerException");
        }

        if (multipartFile != null) {
            s3Upload.update(multipartFile, petId);
        }

        pet.updatePet(userPetDto.getPetName(), userPetDto.getGender(), userPetDto.getBreed(), userPetDto.getNeutralization(), userPetDto.getLikeTag(), userPetDto.getDislikeTag(), userPetDto.getDescription(), userPetDto.getAge());
    }

    @Transactional
    public void deletePet(String email, Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElseThrow();
        Long id = pet.getUser().getId();
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow();
        if (!Objects.equals(id, user.getId())) {
            throw new NotOwnerException("NotOwnerException");
        }
        petRepository.deleteById(petId);
    }
}