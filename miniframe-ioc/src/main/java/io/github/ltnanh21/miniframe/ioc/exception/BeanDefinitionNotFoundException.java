package io.github.ltnanh21.miniframe.ioc.exception;

public class BeanDefinitionNotFoundException extends RuntimeException {
    public BeanDefinitionNotFoundException(String message) {
        super(message);
    }

    public BeanDefinitionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
