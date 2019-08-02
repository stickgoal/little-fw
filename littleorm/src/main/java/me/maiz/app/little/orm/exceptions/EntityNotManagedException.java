package me.maiz.app.little.orm.exceptions;

public class EntityNotManagedException extends PersistException {

    public EntityNotManagedException()  {
    }

    public EntityNotManagedException(String message) {
        super(message);
    }

    public EntityNotManagedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotManagedException(Throwable cause) {
        super(cause);
    }

    public EntityNotManagedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
