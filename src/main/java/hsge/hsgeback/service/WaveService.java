package hsge.hsgeback.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import hsge.hsgeback.constant.FcmPushId;
import hsge.hsgeback.dto.common.BasicResponse;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.exception.ResourceNotFoundException;
import hsge.hsgeback.repository.user.UserRepository;
import hsge.hsgeback.repository.user.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static hsge.hsgeback.constant.PushNotification.WAVE_BODY;
import static hsge.hsgeback.constant.PushNotification.WAVE_TITLE;

@Service
@RequiredArgsConstructor
public class WaveService {

    private final UserTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;

    public BasicResponse wave(String fromEmail, Long toUserId) {
        User from = userRepository.findByEmail(fromEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", fromEmail));
        User to = userRepository.findById(toUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", toUserId.toString()));

        List<String> tokens = tokenRepository.findTokenByEmail(to.getEmail());

        String title = WAVE_TITLE.getContent();
        String body = WAVE_BODY.getContent(from.getNickname());
        String image = String.valueOf(from.getProfilePath());
        String pushId = FcmPushId.WAVING.getPushId();

        Map<String, String> message = fcmService.buildMessage(title, body, image, pushId);

        try {
            boolean succeeded = fcmService.sendMulticastMessageTo(tokens, message);

            if (!succeeded) {
                throw new RuntimeException("손흔들기 푸시 발송에 실패하였습니다.");
            }

            return BasicResponse.of(null, "손흔들기 푸시가 정상적으로 보내졌습니다");
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("푸시 알림 동작시 문제가 발생하였습니다.");
        }
    }
}
