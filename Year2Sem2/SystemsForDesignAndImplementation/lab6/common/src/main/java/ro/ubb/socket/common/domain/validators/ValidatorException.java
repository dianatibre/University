package ro.ubb.socket.common.domain.validators;

/**
 * @author radu.
 */

public class ValidatorException extends RestaurantException {
    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }
}
