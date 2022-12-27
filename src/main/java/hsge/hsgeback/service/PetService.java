package hsge.hsgeback.service;

import hsge.hsgeback.dto.request.SignupDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.dto.response.*;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.PetImg;
import hsge.hsgeback.entity.Report;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.exception.NotOwnerException;
import hsge.hsgeback.repository.PetImgRepository;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.ReportRepository;
import hsge.hsgeback.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.servlet.multipart.location}")
    private String pathName;
    private final PetRepository petRepository;

    private final PetImgRepository petImgRepository;
    private final UserRepository userRepository;

    private final ReportRepository reportRepository;

    private final S3Upload s3Upload;

    public List<PetResponseDto> findPetByLocation(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        User findUser = optional.orElseThrow(IllegalArgumentException::new);
        Double currentLatitude = findUser.getLatitude();
        Double currentLongtitude = findUser.getLongtitude();
        Double startLatitude = currentLatitude - findUser.getRadius();
        Double endLatitude = currentLatitude + findUser.getRadius();
        Double startLongtitude = currentLongtitude - findUser.getRadius();
        Double endLongtitude = currentLongtitude + findUser.getRadius();
        List<User> userList = userRepository.findByLatitudeBetweenAndLongtitudeBetween(startLatitude, endLatitude, startLongtitude, endLongtitude);
//        List<Pet> petList = userList.stream().map(v -> v.getPets()).flatMap(List::stream).collect(Collectors.toList());
        List<PetResponseDto> result = new ArrayList<>();
        List<Report> reportee1 = reportRepository.findReportee(findUser);
//        List<Report> reportee = findUser.getReportee(); // 신고 당한 유저는 userList에서 걸러지게 해야되는데, 반복문을 또 쓰는 건 아닌 것 같은데,
        log.info("===============================");
        for (Report report : reportee1) {
            log.info("re : {}", report.getReportee().getId());
        }
        log.info("===============================");
        for (User user : userList) {
            List<Pet> pets = user.getPets();
            for (Pet pet : pets) {
                if (!Objects.equals(pet.getUser().getId(), findUser.getId())) {  // 본인 강아지는 제외
                    PetResponseDto petResponseDto = new PetResponseDto();
                    List<PetImg> petImg = pet.getPetImg();
                    List<String> urlList = petImg.stream()
                            .map(h -> new UrlDto(h.getS3Url()).getImageUrl())
                            .collect(Collectors.toList());
                    petResponseDto.setPetImg(urlList);
                    petResponseDto.setPetId(pet.getId());
                    petResponseDto.setName(pet.getPetName());
                    petResponseDto.setSex(pet.getGender());
                    petResponseDto.setBreed(pet.getBreed().getKorean());
                    petResponseDto.setIsNeuter(pet.getNeutralization());
                    petResponseDto.setTag(pet.getLikeTag(), pet.getDislikeTag());
                    petResponseDto.setAge(pet.getAge().getKorean());
                    result.add(petResponseDto);
                }
            }
        }
        return result;
    }

    public List<PetInfoResponseDto> getAllPet(Long userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        List<Pet> pets = user.getPets();
        List<PetInfoResponseDto> result = new ArrayList<>();
        for (Pet pet : pets) {
            PetInfoResponseDto petInfoResponseDto = new PetInfoResponseDto();
            petInfoResponseDto.setPetName(pet.getPetName());
            petInfoResponseDto.setTag(pet.getLikeTag(), pet.getDislikeTag());
            petInfoResponseDto.setBreed(pet.getBreed().getKorean());
//            List<Pet> petList = userList.stream().map(v -> v.getPets()).flatMap(List::stream).collect(Collectors.toList());
            List<PetImg> petImg = pet.getPetImg();
            List<String> urlList = petImg.stream()
                    .map(h -> new UrlDto(h.getS3Url()).getImageUrl())
                    .collect(Collectors.toList());
            petInfoResponseDto.setPetImg(urlList);
            petInfoResponseDto.setId(pet.getId());
            petInfoResponseDto.setGender(pet.getGender());
            petInfoResponseDto.setDescription(pet.getDescription());
            petInfoResponseDto.setNeutralization(pet.getNeutralization());
            petInfoResponseDto.setAge(pet.getAge().getKorean());
            result.add(petInfoResponseDto);
        }
        return result;
    }

    public PetInfoResponseDto getOnePet(Long petId) {
        Optional<Pet> optional = petRepository.findById(petId);
        Pet pet = optional.orElseThrow();
        PetInfoResponseDto dto = new PetInfoResponseDto();
        AgeDto ageDto = new AgeDto();
        ageDto.setKey(pet.getAge().toString());
        ageDto.setValue(pet.getAge().getKorean());
        BreedDto breedDto = new BreedDto();
        breedDto.setKey(pet.getBreed().toString());
        breedDto.setValue(pet.getBreed().getKorean());
        dto.setTag(pet.getLikeTag(), pet.getDislikeTag());
        dto.setBreedDto(breedDto);
        dto.setAgeDto(ageDto);
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
        List<PetInfoResponseDto> result = new ArrayList<>();
        for (Pet pet : pets) {
            PetInfoResponseDto dto = new PetInfoResponseDto();
            BreedDto breedDto = new BreedDto();
            breedDto.setKey(pet.getBreed().toString());
            breedDto.setValue(pet.getBreed().getKorean());
            dto.setBreedDto(breedDto);
            AgeDto ageDto = new AgeDto();
            ageDto.setKey(pet.getAge().toString());
            ageDto.setValue(pet.getAge().getKorean());
            dto.setAgeDto(ageDto);
            dto.setTag(pet.getLikeTag(), pet.getDislikeTag());
            dto.setBreed(pet.getBreed().getKorean());
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
            result.add(dto);
        }
        return result;
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
    public void deletePet(String email,Long petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElseThrow();
        Long id = pet.getUser().getId();
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow();
        if (!Objects.equals(id, user.getId())){
            throw new NotOwnerException("NotOwnerException");
        }
        petRepository.deleteById(petId);
    }
}