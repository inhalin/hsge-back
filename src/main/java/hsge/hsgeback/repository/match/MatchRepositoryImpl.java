package hsge.hsgeback.repository.match;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsge.hsgeback.entity.QMatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QMatch match = QMatch.match;

    @Override
    public boolean existsByPetIdAndUserId(Long petId, Long userId) {
        return queryFactory.selectFrom(match)
                .where(match.pet.id.eq(petId),
                        match.user.id.eq(userId))
                .fetchFirst() != null;
    }

    @Override
    public void deleteAllByPetIdAndUserId(Long petId, Long userId) {
        queryFactory.delete(match)
                .where(match.pet.id.eq(petId),
                        match.user.id.eq(userId))
                .execute();
    }

    @Override
    public void updateLikeValue(Long petId, Long userId, boolean likeValue) {
        queryFactory.update(match)
                .set(match.likeValue, likeValue)
                .where(match.pet.id.eq(petId),
                        match.user.id.eq(userId))
                .execute();
    }

    @Override
    public List<Long> findAllMatchedPetIds(Long userId) {
        return queryFactory.select(match.pet.id)
                .from(match)
                .where(match.user.id.eq(userId))
                .fetch();
    }
}
