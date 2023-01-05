package hsge.hsgeback.event.chat;

import com.google.firebase.messaging.FirebaseMessagingException;
import hsge.hsgeback.constant.FcmPushId;
import hsge.hsgeback.dto.chat.MessageSentEventDto;
import hsge.hsgeback.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static hsge.hsgeback.constant.PushNotification.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageSentEventHandler {

    private final FcmService fcmService;

    @Async
    @EventListener
    public void onMessageSentEvent(MessageSentEvent event) {
        MessageSentEventDto eventDto = event.getChatMessageEventDto();

        sendNotification(eventDto);
    }

    private void sendNotification(MessageSentEventDto eventDto) {

        List<String> tokens = eventDto.getReceiverTokens();

        String title = MESSAGE_TITLE.getContent(eventDto.getSenderNickname());
        String body = MESSAGE_BODY.getContent(eventDto.getContent());
        String image = String.valueOf(eventDto.getSenderProfilePath());
        String pushId = FcmPushId.NEW_MESSAGE.getPushId();
        Long roomId = eventDto.getRoomId();

        Map<String, String> message = fcmService.buildMessage(title, body, image, pushId, roomId);

        try {
            log.info("sending push notification for new message.");
            boolean succeeded = fcmService.sendMulticastMessageTo(tokens, message);

            if (!succeeded) {
                throw new RuntimeException("메시지 푸시 발송에 실패하였습니다.");
            }
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("푸시 알림 동작시 문제가 발생했습니다.");
        }
    }
}
