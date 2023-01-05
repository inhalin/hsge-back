package hsge.hsgeback.event.match;

import hsge.hsgeback.dto.match.UserPetMatchDto;

public class MatchEvent {

    private final UserPetMatchDto userPetMatchDto;

    public MatchEvent(UserPetMatchDto userPetMatchDto) {
        this.userPetMatchDto = userPetMatchDto;
    }

    public static MatchEvent of(UserPetMatchDto userPetMatchDto) {
        return new MatchEvent(userPetMatchDto);
    }

    public UserPetMatchDto getUserPetMatchDto() {
        return userPetMatchDto;
    }
}
