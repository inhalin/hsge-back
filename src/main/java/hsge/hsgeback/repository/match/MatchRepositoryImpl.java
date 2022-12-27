package hsge.hsgeback.repository.match;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsge.hsgeback.entity.QMatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QMatch match = QMatch.match;

    @Override
    public boolean existsByPetIdAndLikerId(Long petId, Long likerId) {
        return queryFactory.selectFrom(match)
                .where(match.pet.id.eq(petId),
                        match.user.id.eq(likerId))
                .fetchFirst() != null;
    }
}
