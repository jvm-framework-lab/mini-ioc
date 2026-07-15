package io.github.ltnanh21.miniframe.ioc.registry;

import io.github.ltnanh21.miniframe.ioc.bean.BeanDefinition;
import io.github.ltnanh21.miniframe.ioc.exception.BeanDefinitionNotFoundException;
import io.github.ltnanh21.miniframe.ioc.exception.NoUniqueBeanDefinitionException;

import java.util.HashMap;
import java.util.Map;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public void register(String beanName, BeanDefinition beanDefinition) {
        if (beanDefinitionMap.containsKey(beanName)) {
            throw new NoUniqueBeanDefinitionException("Bean name '" + beanName + "' is already registered");
        }

        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition get(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new BeanDefinitionNotFoundException("Bean name '" + beanName + "' is not registered");
        }

        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean contains(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }
}
