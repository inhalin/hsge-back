package hsge.hsgeback.exception.advice;

import lombok.Data;

@Data
public class ErrorResult {
    private Object message;

    public ErrorResult(Object message) {
        this.message = message;
    }
}
