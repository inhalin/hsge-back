package hsge.hsgeback.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static hsge.hsgeback.entity.QUser.user;
import static hsge.hsgeback.entity.QUserToken.userToken;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public String getFcmTokenByEmail(String email) {
        return queryFactory.select(userToken.token)
                .from(user)
                .where(user.email.eq(email))
                .fetchFirst();
    }
}
