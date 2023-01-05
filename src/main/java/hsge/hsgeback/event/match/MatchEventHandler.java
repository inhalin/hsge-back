package hsge.hsgeback.event.match;

import com.google.firebase.messaging.FirebaseMessagingException;
import hsge.hsgeback.constant.FcmPushId;
import hsge.hsgeback.dto.match.UserPetMatchDto;
import hsge.hsgeback.entity.User;
import hsge.hsgeback.exception.ResourceNotFoundException;
import hsge.hsgeback.repository.user.UserRepository;
import hsge.hsgeback.repository.user.UserTokenRepository;
import hsge.hsgeback.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;

import static hsge.hsgeback.constant.PushNotification.LIKE_BODY;
import static hsge.hsgeback.constant.PushNotification.LIKE_TITLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchEventHandler {

    private final FcmService fcmService;
    private final UserRepository userRepository;
    private final UserTokenRepository tokenRepository;

    @Async
    @TransactionalEventListener
    public void onMatchEvent(MatchEvent event) throws FirebaseMessagingException {
        UserPetMatchDto matchDto = event.getUserPetMatchDto();

        sendNotification(matchDto);
    }

    private void sendNotification(UserPetMatchDto matchDto) throws FirebaseMessagingException {

        User petOwner = userRepository.findByEmail(matchDto.getPetOwnerEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", matchDto.getPetOwnerEmail()));
        List<String> fcmTokens = tokenRepository.findTokenByEmail(petOwner.getEmail());

        String title = LIKE_TITLE.getContent(matchDto.getPetName());
        String body = LIKE_BODY.getContent(matchDto.getLikerNickname());
        String image = String.valueOf(petOwner.getProfilePath());
        String pushId = FcmPushId.MATCH_SWIPE.getPushId();

        Map<String, String> message = fcmService.buildMessage(title, body, image, pushId);

        log.info("sending push notification for matching.");
        fcmService.sendMulticastMessageTo(fcmTokens, message);
    }
}
