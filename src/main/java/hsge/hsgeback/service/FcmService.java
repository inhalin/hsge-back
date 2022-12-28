package hsge.hsgeback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    public void sendMessageTo(String targetToken, Map<String, String> messageMap) throws FirebaseMessagingException {

        Message message = Message.builder()
                .setToken(targetToken)
                .putAllData(messageMap)
                .build();

        String response = firebaseMessaging.send(message);

        log.info("response = {}", response);
    }
}
