package hsge.hsgeback.repository.pet;

import hsge.hsgeback.entity.Pet;

import java.util.List;

public interface PetRepositoryCustom {
    List<Pet> findAllByUserId(Long counterUserId);
}
