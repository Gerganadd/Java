package bg.sofia.uni.fmi.mjt.simcity.exception;

public class UtilityTypeNotFoundException extends RuntimeException {
    public UtilityTypeNotFoundException(String message) {
        super(message);
    }

    public UtilityTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
