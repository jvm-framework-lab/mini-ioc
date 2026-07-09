package io.github.ltnanh21.miniframe.ioc.factory;

public class DefaultBeanFactory implements BeanFactory {

    @Override
    public Object getBean(String name) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return null;
    }
}