package hsge.hsgeback.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class BaseResponseDto<T> {

    private Object message;

    private T data;

    public BaseResponseDto(Object message, T data) {
        this.message = message;
        this.data = data;
    }
}
