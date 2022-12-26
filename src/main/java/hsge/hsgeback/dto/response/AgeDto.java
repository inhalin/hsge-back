package hsge.hsgeback.dto.response;

import lombok.Data;

@Data
public class AgeDto {
    private String key;

    private String value;

    public AgeDto(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
