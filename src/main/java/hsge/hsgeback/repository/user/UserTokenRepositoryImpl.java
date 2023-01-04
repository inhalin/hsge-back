package hsge.hsgeback.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hsge.hsgeback.entity.QUserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserTokenRepositoryImpl implements UserTokenRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QUserToken userToken = QUserToken.userToken;

    @Override
    public List<String> findTokenByEmail(String email) {
        return queryFactory.select(userToken.token)
                .from(userToken)
                .where(userToken.email.eq(email))
                .fetch();
    }
}
