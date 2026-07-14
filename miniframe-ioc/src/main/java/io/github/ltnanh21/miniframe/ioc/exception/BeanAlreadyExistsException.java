package io.github.ltnanh21.miniframe.ioc.exception;

public class BeanAlreadyExistsException extends RuntimeException {
    public BeanAlreadyExistsException(String message) {
        super(message);
    }

    public BeanAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
