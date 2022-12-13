package hsge.hsgeback.util;

import hsge.hsgeback.constant.PushNotification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageUtilTest {

    @Test
    @DisplayName("메시지 파싱이 정상 동작한다.")
    void testParseBody() {
        String title = PushNotification.LIKE_TITLE.getContent();
        String petName = "엉금고양이";
        String parsedTitle = MessageUtil.parseMessage(title, petName);
        assertThat(parsedTitle).isEqualTo(petName + "을(를) 좋아해요🐶");

        String body = PushNotification.LIKE_BODY.getContent();
        String from = "골골";
        String parseBody = MessageUtil.parseMessage(body, from);
        assertThat(parseBody).isEqualTo(from + "님과 대화를 시작해보세요!");
    }
}
