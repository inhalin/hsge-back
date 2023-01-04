package hsge.hsgeback.dto.redis;

import lombok.Data;

@Data
public class NameDto {
    private String name;

    public NameDto(String name) {
        this.name = name;
    }
}
