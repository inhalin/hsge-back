package hsge.hsgeback.repository.user;

import java.util.List;

public interface UserTokenRepositoryCustom {

    List<String> findTokenByEmail(String email);
}
