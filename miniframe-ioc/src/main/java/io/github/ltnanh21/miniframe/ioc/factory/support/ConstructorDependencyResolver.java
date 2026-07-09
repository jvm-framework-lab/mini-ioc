package io.github.ltnanh21.miniframe.ioc.factory.support;

import io.github.ltnanh21.miniframe.ioc.bean.BeanDefinition;

public class ConstructorDependencyResolver implements DependencyResolver {
    @Override
    public Object[] resolveDependencies(BeanDefinition beanDefinition) {
        return new Object[0];
    }
}
