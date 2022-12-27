package hsge.hsgeback.event.match;

import hsge.hsgeback.dto.match.UserPetMatchDto;
public class MatchedEvent {

    private final UserPetMatchDto userPetMatchDto;

    public MatchedEvent(UserPetMatchDto userPetMatchDto) {
        this.userPetMatchDto = userPetMatchDto;
    }

    public static MatchedEvent of(UserPetMatchDto userPetMatchDto) {
        return new MatchedEvent(userPetMatchDto);
    }

    public UserPetMatchDto getUserPetMatchDto() {
        return userPetMatchDto;
    }
}
