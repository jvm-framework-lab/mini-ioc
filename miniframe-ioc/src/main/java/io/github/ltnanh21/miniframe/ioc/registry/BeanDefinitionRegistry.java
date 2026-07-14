package io.github.ltnanh21.miniframe.ioc.registry;

import io.github.ltnanh21.miniframe.ioc.bean.BeanDefinition;

public interface BeanDefinitionRegistry {
    void register(String beanName, BeanDefinition beanDefinition);

    BeanDefinition get(String beanName);

    boolean contains(String beanName);
}
