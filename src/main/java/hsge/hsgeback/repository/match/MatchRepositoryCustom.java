package hsge.hsgeback.repository.match;

public interface MatchRepositoryCustom {

    boolean existsByPetIdAndUserId(Long petId, Long LikerId);

    void deleteAllByPetIdAndUserId(Long petId, Long userId);

    void updateLikeValue(Long petId, Long userId, boolean likeValue);
}
