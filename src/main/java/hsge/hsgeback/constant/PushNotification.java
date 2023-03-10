package hsge.hsgeback.constant;

import hsge.hsgeback.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PushNotification {
    LIKE_TITLE("%s을(를) 좋아해요🐶"),
    LIKE_BODY("%s님과 대화를 시작해보세요!"),
    WAVE_TITLE("손 흔들기👋"),
    WAVE_BODY("%s님이 손을 흔들었어요!"),
    MESSAGE_TITLE("%s님"),
    MESSAGE_BODY("%s");

    private final String content;

    public String getContent(String ...arg) {
        return MessageUtil.parseMessage(content, arg);
    }
}
