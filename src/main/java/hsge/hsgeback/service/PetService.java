package hsge.hsgeback.service;

import hsge.hsgeback.dto.request.UserPetDto;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class PetService {

    private final PetRepository petRepository;

    @Transactional
    public void putPet(Long petId, UserPetDto userPetDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(NoSuchElementException::new);
        pet.updatePetInfo(userPetDto.getPicture(),
                userPetDto.getNeutralization(),
                userPetDto.getLikeTag(),
                userPetDto.getDislikeTag(),
                userPetDto.getDescription(),
                userPetDto.getAge());
    }
}