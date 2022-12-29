package hsge.hsgeback.repository.pet;

import hsge.hsgeback.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long>, PetRepositoryCustom {

    @Query(value = "select * from pet left join matching on pet.id=matching.pet_id where like_value is null or matching.user_id != :userId group by pet.id",
            nativeQuery = true)
    List<Pet> findMatchablePets(@Param("userId") Long userId);
}
