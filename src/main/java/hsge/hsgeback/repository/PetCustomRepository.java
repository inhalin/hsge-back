package hsge.hsgeback.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsge.hsgeback.entity.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static hsge.hsgeback.entity.QPet.pet;

@Repository
@RequiredArgsConstructor
public class PetCustomRepository {

    private final JPAQueryFactory queryFactory;

    public Pet findById(Long id) {
        return queryFactory.selectFrom(pet)
                .where(pet.id.eq(id))
                .fetchOne();
    }
}
