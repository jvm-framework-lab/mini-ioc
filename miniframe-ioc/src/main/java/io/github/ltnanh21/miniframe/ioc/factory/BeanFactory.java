package io.github.ltnanh21.miniframe.ioc.factory;

public interface BeanFactory {
    Object getBean(String name);

    <T> T getBean(Class<T> requiredType);
}
