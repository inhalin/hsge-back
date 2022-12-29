package hsge.hsgeback.repository.pet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsge.hsgeback.entity.Pet;
import hsge.hsgeback.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hsge.hsgeback.entity.QPet.pet;

@Repository
@RequiredArgsConstructor
public class PetRepositoryImpl implements PetRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;

    @Override
    public List<Pet> findAllByUserId(Long userId) {
        return queryFactory.selectFrom(pet)
                .leftJoin(pet.user, user).fetchJoin()
                .where(pet.user.id.eq(userId))
                .fetch();
    }
}
