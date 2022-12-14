package hsge.hsgeback.exception;

public class NicknameDuplicateException extends RuntimeException {

    public NicknameDuplicateException() {
    }

    public NicknameDuplicateException(String message) {
        super(message);
    }
}
