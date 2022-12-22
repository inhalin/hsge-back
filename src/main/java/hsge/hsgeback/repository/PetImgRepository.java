package hsge.hsgeback.repository;

import hsge.hsgeback.entity.PetImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetImgRepository extends JpaRepository<PetImg, Long> {
    Optional<PetImg> findByPetId(Long petId);
}
