package hsge.hsgeback.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MessageDto {

    private MessageUserInfoDto userInfo;

    private List<MessageResponseDto> messageList;

    public MessageDto(MessageUserInfoDto userInfo, List<MessageResponseDto> messageList) {
        this.userInfo = userInfo;
        this.messageList = messageList;
    }
}
