package io.github.ltnanh21.miniframe.ioc.factory.support;

import io.github.ltnanh21.miniframe.ioc.bean.BeanDefinition;

public interface BeanCreator {
    Object createBean(BeanDefinition beanDefinition, Object[] constructorArguments);
}
