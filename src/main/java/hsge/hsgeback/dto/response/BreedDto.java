package hsge.hsgeback.dto.response;

import lombok.Data;

@Data
public class BreedDto {

    private String key;

    private String value;

    public BreedDto(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
