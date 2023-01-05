package hsge.hsgeback.event.chat;

import hsge.hsgeback.dto.chat.MessageSentEventDto;
import lombok.Getter;

@Getter
public class MessageSentEvent {

    private final MessageSentEventDto chatMessageEventDto;

    public MessageSentEvent(MessageSentEventDto chatMessageEventDto) {
        this.chatMessageEventDto = chatMessageEventDto;
    }

    public static MessageSentEvent of(MessageSentEventDto eventDto) {
        return new MessageSentEvent(eventDto);
    }
}
