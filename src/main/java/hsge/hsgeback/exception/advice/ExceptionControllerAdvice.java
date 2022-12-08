package hsge.hsgeback.exception.advice;

import hsge.hsgeback.exception.BadWebClientRequestException;
import hsge.hsgeback.exception.NotOwnerException;
import hsge.hsgeback.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadWebClientRequestException.class)
    public ErrorResult badWebClientRequestException(BadWebClientRequestException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResult usernameNotFoundException(UsernameNotFoundException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotOwnerException.class)
    public ErrorResult notOwnerException(NotOwnerException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResult handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return new ErrorResult(e.getMessage(), LocalDateTime.now(), request.getDescription(false));
    }
}
