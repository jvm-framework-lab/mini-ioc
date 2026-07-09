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
            throw new NoUniqueBeanDefinitionException();
        }

        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition get(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new BeanDefinitionNotFoundException();
        }

        return beanDefinitionMap.get(beanName);
    }

    private boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }
}
