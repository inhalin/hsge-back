package hsge.hsgeback.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public void sendMessageTo(String targetToken, String title, String body) throws FirebaseMessagingException {

        Message message = Message.builder()
                .setToken(targetToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();

        String response = firebaseMessaging.send(message);

        log.info("response = {}", response);
    }
}
