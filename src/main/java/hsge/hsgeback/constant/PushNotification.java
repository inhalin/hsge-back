package hsge.hsgeback.constant;

import hsge.hsgeback.util.MessageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PushNotification {
    LIKE_TITLE("%s을(를) 좋아해요🐶"),
    LIKE_BODY("%s님과 대화를 시작해보세요!");

    private final String content;

    public String getContent(String ...arg) {
        return MessageUtil.parseMessage(content, arg);
    }
}
