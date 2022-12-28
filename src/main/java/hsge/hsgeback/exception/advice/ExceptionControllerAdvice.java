package hsge.hsgeback.exception.advice;

import hsge.hsgeback.exception.*;
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
    public ErrorResult handleBadWebClientRequestException(BadWebClientRequestException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorResult handleUsernameNotFoundException(UsernameNotFoundException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NicknameDuplicateException.class)
    public ErrorResult handleNicknameDuplicateException(NicknameDuplicateException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotOwnerException.class)
    public ErrorResult handleNotOwnerException(NotOwnerException e) {
        return new ErrorResult(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorResult handleResourceNotFoundException(ResourceNotFoundException e, WebRequest request) {
        return new ErrorResult(e.getMessage(), request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceDuplicateException.class)
    public ErrorResult handleResourceDuplicateException(ResourceDuplicateException e, WebRequest request) {
        return new ErrorResult(e.getMessage(), request.getDescription(false));
    }
}
