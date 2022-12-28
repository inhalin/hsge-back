package hsge.hsgeback.repository.user;

public interface UserRepositoryCustom {

    String getFcmTokenByEmail(String email);

    void saveFcmToken(String email, String fcmToken);

    void deleteFcmToken(String email);
}
