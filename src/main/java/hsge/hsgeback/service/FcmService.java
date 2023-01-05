package hsge.hsgeback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.*;
import hsge.hsgeback.constant.FcmPushId;
import hsge.hsgeback.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static hsge.hsgeback.constant.FcmPushId.*;
import static hsge.hsgeback.constant.PushNotification.*;

@Slf4j
@Service
public class FcmService {

    private final String API_URI;
    private final ObjectMapper objectMapper;
    private final FirebaseMessaging firebaseMessaging;

    public FcmService(@Value("${fcm.api-uri}") String fcmApiUrl, ObjectMapper objectMapper, FirebaseMessaging firebaseMessaging) {
        this.API_URI = fcmApiUrl;
        this.objectMapper = objectMapper;
        this.firebaseMessaging = firebaseMessaging;
    }

    public Map<String, String> buildMessage(String title, String body, String image, String pushId) {
        return Map.of(
                "title", title,
                "body", body,
                "image", image,
                "pushID", pushId);
    }

    public Map<String, String> buildMessage(String title, String body, String image, String pushId, Long id) {
        return Map.of(
                "title", title,
                "body", body,
                "image", image,
                "pushID", pushId,
                "id", id.toString());
    }

    public void sendMessageTo(String targetToken, Map<String, String> messageMap) throws FirebaseMessagingException {

        Message message = Message.builder()
                .setToken(targetToken)
                .putAllData(messageMap)
                .build();

        String response = firebaseMessaging.send(message);

        log.info("response = {}", response);
    }

    public boolean sendMulticastMessageTo(List<String> targetTokens, Map<String, String> messageMap) throws FirebaseMessagingException {

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(targetTokens)
                .putAllData(messageMap)
                        .build();

        BatchResponse batchResponse = firebaseMessaging.sendMulticast(message);

        batchResponse.getResponses()
                .forEach(r -> log.info("response with messageId {} sent successfully? {}", r.getMessageId(), r.isSuccessful()));

        return batchResponse.getResponses().stream().allMatch(SendResponse::isSuccessful);
    }
}
