package hsge.hsgeback.repository.match;

public interface MatchRepositoryCustom {

    boolean existsByPetIdAndLikerId(Long petId, Long LikerId);
}
