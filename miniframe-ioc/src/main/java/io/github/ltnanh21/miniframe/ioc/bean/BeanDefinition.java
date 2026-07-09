package io.github.ltnanh21.miniframe.ioc.bean;

public class BeanDefinition {
    private final Class<?> beanClass;
    private final InitializationMode initializationMode;
    private final Scope scope;

    public BeanDefinition(Class<?> beanClass, InitializationMode initializationMode, Scope scope) {
        this.beanClass = beanClass;
        this.initializationMode = initializationMode;
        this.scope = scope;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public InitializationMode getInitializationMode() {
        return initializationMode;
    }

    public Scope getScope() {
        return scope;
    }
}
