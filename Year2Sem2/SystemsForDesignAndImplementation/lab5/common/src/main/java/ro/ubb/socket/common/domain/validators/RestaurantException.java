package ro.ubb.socket.common.domain.validators;

public class RestaurantException extends RuntimeException{
    public RestaurantException(String message) {
        super(message);
    }

    public RestaurantException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestaurantException(Throwable cause) {
        super(cause);
    }
}
