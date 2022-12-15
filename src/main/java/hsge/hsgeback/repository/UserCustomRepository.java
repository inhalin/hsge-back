package hsge.hsgeback.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsge.hsgeback.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static hsge.hsgeback.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    public User findByEmail(String email) {
        return queryFactory.selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne();
    }

    public String getFcmTokenByEmail(String email) {
        return queryFactory.select(user.fcmToken)
                .from(user)
                .where(user.email.eq(email))
                .fetchFirst();
    }
}
