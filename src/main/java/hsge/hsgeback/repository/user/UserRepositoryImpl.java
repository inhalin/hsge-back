package hsge.hsgeback.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static hsge.hsgeback.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public String getFcmTokenByEmail(String email) {
        return queryFactory.select(user.fcmToken)
                .from(user)
                .where(user.email.eq(email))
                .fetchFirst();
    }

    @Override
    public void saveFcmToken(String email, String fcmToken) {
        queryFactory.update(user)
                .set(user.fcmToken, fcmToken)
                .where(user.email.eq(email))
                .execute();
    }

    @Override
    public void deleteFcmToken(String email) {
        queryFactory.update(user)
                .setNull(user.fcmToken)
                .where(user.email.eq(email))
                .execute();
    }
}
