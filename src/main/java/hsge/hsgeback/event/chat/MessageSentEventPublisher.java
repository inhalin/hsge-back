package hsge.hsgeback.event.chat;

import hsge.hsgeback.dto.chat.MessageSentEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSentEventPublisher {


    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final MessageSentEventDto eventDto) {
        applicationEventPublisher.publishEvent(MessageSentEvent.of(eventDto));
    }
}
