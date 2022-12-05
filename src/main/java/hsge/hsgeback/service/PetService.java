package hsge.hsgeback.service;

import hsge.hsgeback.dto.request.PutDto;
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
    public void putPet(Long petId, PutDto putDto) {
        Pet pet = petRepository.findById(petId).orElseThrow(NoSuchElementException::new);
        pet.updatePetInfo(putDto.getPicture(),
                putDto.getNeutralization(),
                putDto.getLikeTag(),
                putDto.getDislikeTag(),
                putDto.getDescription(),
                putDto.getAge());
    }
}