package hsge.hsgeback.service;

import hsge.hsgeback.dto.request.PutDto;
import hsge.hsgeback.dto.response.PetInfoResponseDto;
import hsge.hsgeback.dto.response.PetResponseDto;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.repository.PetRepository;
import hsge.hsgeback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class PetService {

    private final PetRepository petRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    @Transactional
    public void putPet(Long petId, PutDto putDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(
                () -> new NoSuchElementException());
        pet.updatePetInfo(putDto.getPicture(), putDto.getNeutralization(), putDto.getLikeTag(), putDto.getDislikeTag(), putDto.getDescription(), putDto.getAge());
    }

    public List<PetResponseDto> findPetByLocation(HttpServletRequest request) {
        User findUser = userService.getUser(request);
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

    public List<PetInfoResponseDto> getAllPet(Long userId){
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isEmpty()){
            throw new IllegalArgumentException();
        }
        User user = optional.get();
        List<Pet> pets = user.getPets();
        List<PetInfoResponseDto> result = new ArrayList<>();
        for (Pet pet : pets) {
            PetInfoResponseDto petInfoResponseDto = new PetInfoResponseDto();
            petInfoResponseDto.setPetName(pet.getPetName());
            petInfoResponseDto.setTag(pet.getLikeTag(),pet.getDislikeTag());
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

    public PetInfoResponseDto getOnePet(Long petId){
        Optional<Pet> optional = petRepository.findById(petId);
        if (optional.isEmpty()){
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
}