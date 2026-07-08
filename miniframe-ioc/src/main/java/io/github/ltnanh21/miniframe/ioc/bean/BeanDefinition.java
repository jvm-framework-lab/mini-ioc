package io.github.ltnanh21.miniframe.ioc.bean;

public class BeanDefinition {
    private final String name;
    private final Class<?> beanClass;
    private final InitializationMode initializationMode;
    private final Scope scope;

    public BeanDefinition(String name, Class<?> beanClass, InitializationMode initializationMode, Scope scope) {
        this.name = name;
        this.beanClass = beanClass;
        this.initializationMode = initializationMode;
        this.scope = scope;
    }

    public String getName() {
        return name;
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
