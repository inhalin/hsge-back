package hsge.hsgeback.dto.match;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPetMatchDto {
    private String likerEmail;
    private String likerNickname;

    private Long petId;
    private String petName;

    private String petOwnerEmail;
    private String petOwnerNickname;
}
