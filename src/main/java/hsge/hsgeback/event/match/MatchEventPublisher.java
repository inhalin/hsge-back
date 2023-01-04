package hsge.hsgeback.event.match;

import hsge.hsgeback.dto.match.UserPetMatchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final UserPetMatchDto userPetMatchDto) {
        applicationEventPublisher.publishEvent(MatchEvent.of(userPetMatchDto));
    }
}
