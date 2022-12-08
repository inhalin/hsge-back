package hsge.hsgeback.service;

import hsge.hsgeback.dto.request.SignupDto;
import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.dto.response.PetInfoResponseDto;
import hsge.hsgeback.dto.response.PetResponseDto;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.exception.NotOwnerException;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PetService {

    private final PetRepository petRepository;

    private final UserRepository userRepository;


    public List<PetResponseDto> findPetByLocation(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User findUser = optional.get();
        Double currentLatitude = findUser.getLatitude();
        Double currentLongtitude = findUser.getLongtitude();
        Double startLatitude = currentLatitude - findUser.getRadius();
        Double endLatitude = currentLatitude + findUser.getRadius();
        Double startLongtitude = currentLongtitude - findUser.getRadius();
        Double endLongtitude = currentLongtitude + findUser.getRadius();
        List<User> userList = userRepository.findByLatitudeBetweenAndLongtitudeBetween(startLatitude, endLatitude, startLongtitude, endLongtitude);
        List<PetResponseDto> result = new ArrayList<>();
        for (User user : userList) {
            List<Pet> pets = user.getPets();
            for (Pet pet : pets) {
                PetResponseDto petResponseDto = new PetResponseDto();
                petResponseDto.setName(pet.getPetName());
                petResponseDto.setSex(pet.getGender());
                petResponseDto.setBreed(pet.getBreed().getKorean());
                petResponseDto.setIsNeuter(pet.getNeutralization());
                petResponseDto.setPicture(pet.getPicture());
                petResponseDto.setTag(pet.getLikeTag(), pet.getDislikeTag());
                petResponseDto.setAge(pet.getAge().getKorean());
                result.add(petResponseDto);
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
            petInfoResponseDto.setPicture(pet.getPicture());
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
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Pet pet = optional.get();
        PetInfoResponseDto dto = new PetInfoResponseDto();
        dto.setAge(pet.getAge().getKorean());
        dto.setTag(pet.getLikeTag(), pet.getDislikeTag());
        dto.setBreed(pet.getBreed().getKorean());
        dto.setGender(pet.getGender());
        dto.setDescription(pet.getDescription());
        dto.setNeutralization(pet.getNeutralization());
        dto.setPicture(pet.getPicture());
        dto.setPetName(pet.getPetName());
        dto.setId(pet.getId());
        return dto;
    }

    public List<PetInfoResponseDto> getMyPet(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        List<Pet> pets = user.getPets();
        List<PetInfoResponseDto> result = new ArrayList<>();
        for (Pet pet : pets) {
            PetInfoResponseDto dto = new PetInfoResponseDto();
            dto.setAge(pet.getAge().getKorean());
            dto.setTag(pet.getLikeTag(), pet.getDislikeTag());
            dto.setBreed(pet.getBreed().getKorean());
            dto.setGender(pet.getGender());
            dto.setDescription(pet.getDescription());
            dto.setNeutralization(pet.getNeutralization());
            dto.setPicture(pet.getPicture());
            dto.setPetName(pet.getPetName());
            dto.setId(pet.getId());
            result.add(dto);
        }
        return result;
    }

    @Transactional
    public void postPet(String email, SignupDto signupDto) {
        Optional<User> optional = userRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        petRepository.save(signupDto.toPetEntity(user));
    }

    @Transactional
    public void updatePet(String email, Long petId, UserPetDto userPetDto) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        if (optionalPet.isEmpty()) {
            throw new IllegalArgumentException();
        }
        Pet pet = optionalPet.get();
        Long id = pet.getUser().getId();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException();
        }
        User user = optionalUser.get();
        if (!Objects.equals(id, user.getId())) {
            throw new NotOwnerException("NotOwnerException");
        }
        pet.updatePetInfo(userPetDto.getPetName(), userPetDto.getGender(), userPetDto.getBreed(), userPetDto.getPicture(), userPetDto.getNeutralization(), userPetDto.getLikeTag(), userPetDto.getDislikeTag(), userPetDto.getDescription(), userPetDto.getAge());
    }

    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }
}