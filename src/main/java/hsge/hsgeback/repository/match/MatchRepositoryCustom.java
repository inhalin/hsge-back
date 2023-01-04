package hsge.hsgeback.repository.match;

import java.util.List;

public interface MatchRepositoryCustom {

    boolean existsByPetIdAndUserId(Long petId, Long LikerId);

    void deleteAllByPetIdAndUserId(Long petId, Long userId);

    void updateLikeValue(Long petId, Long userId, boolean likeValue);

    List<Long> findAllMatchedPetIds(Long userId);
}
