package me.maiz.app.little.littlespring.ioc.exceptions;

public class BeanRegistrationException extends RuntimeException {
    public BeanRegistrationException() {
    }

    public BeanRegistrationException(String message) {
        super(message);
    }

    public BeanRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanRegistrationException(Throwable cause) {
        super(cause);
    }

    public BeanRegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
