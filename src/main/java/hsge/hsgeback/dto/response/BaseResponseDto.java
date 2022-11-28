package hsge.hsgeback.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class BaseResponseDto<T> {

    private String message;

    private T data;

    @Builder
    public BaseResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
