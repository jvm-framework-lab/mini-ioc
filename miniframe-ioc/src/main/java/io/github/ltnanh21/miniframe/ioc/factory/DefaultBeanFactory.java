package io.github.ltnanh21.miniframe.ioc.factory;

import io.github.ltnanh21.miniframe.ioc.bean.BeanDefinition;
import io.github.ltnanh21.miniframe.ioc.exception.BeanCreationException;
import io.github.ltnanh21.miniframe.ioc.exception.BeanDefinitionNotFoundException;
import io.github.ltnanh21.miniframe.ioc.registry.BeanDefinitionRegistry;
import io.github.ltnanh21.miniframe.ioc.registry.BeanRegistry;

import java.lang.reflect.InvocationTargetException;

public class DefaultBeanFactory implements BeanFactory {
    private final BeanRegistry beanRegistry;
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public DefaultBeanFactory(BeanRegistry beanRegistry, BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanRegistry = beanRegistry;
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public Object getBean(String name) {
        validateBeanName(name);
        if (beanRegistry.contains(name)) {
            return beanRegistry.get(name);
        }

        BeanDefinition beanDefinition = beanDefinitionRegistry.get(name);
        try {
            Object instance = beanDefinition.getBeanClass()
                    .getDeclaredConstructor()
                    .newInstance();

            beanRegistry.register(name, instance);
            return instance;
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 BeanDefinitionNotFoundException |
                 NoSuchMethodException e) {
            throw new BeanCreationException(
                    "Cannot instantiate bean '" + name + "' of type " + beanDefinition.getBeanClass().getName()
                    , e);
        }
    }

    private void validateBeanName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Bean name must not be null or blank");
        }
    }
}