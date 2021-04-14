package ro.ubb.socket.common.service;

public class CommonServiceException extends RuntimeException {
    public CommonServiceException(String message) {
        super(message);
    }

    public CommonServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
