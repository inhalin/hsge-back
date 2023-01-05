package hsge.hsgeback.repository.pet;

import hsge.hsgeback.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long>, PetRepositoryCustom {

    @Query(value = "select * from pet p" +
            " left join matching m on p.id = m.pet_id" +
            " where p.user_id != :userId" +
            " and m.user_id != :userId" +
            " or m.user_id is null",
            nativeQuery = true)
    List<Pet> findMatchablePets(@Param("userId") Long userId);
}
