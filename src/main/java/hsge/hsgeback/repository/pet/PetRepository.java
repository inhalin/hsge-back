package hsge.hsgeback.repository.pet;

import hsge.hsgeback.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,Long>, PetRepositoryCustom {
}
