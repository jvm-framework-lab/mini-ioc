package io.github.ltnanh21.miniframe.ioc.registry;

public interface BeanRegistry {
    void register(String beanName, Object object);

    Object get(String beanName);

}
