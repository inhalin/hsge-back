package hsge.hsgeback.repository.match;

import hsge.hsgeback.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>, MatchRepositoryCustom {

    void deleteAllByLikeValue(Boolean likeValue);

}
