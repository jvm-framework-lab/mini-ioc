package io.github.ltnanh21.miniframe.ioc.registry;

import io.github.ltnanh21.miniframe.ioc.exception.BeanAlreadyExistsException;
import io.github.ltnanh21.miniframe.ioc.exception.BeanNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class DefaultBeanRegistry implements BeanRegistry {
    private final Map<String, Object> beans = new HashMap<>();

    @Override
    public void register(String beanName, Object object) {
        if (beans.containsKey(beanName)) {
            throw new BeanAlreadyExistsException(beanName);
        }

        beans.put(beanName, object);
    }

    @Override
    public Object get(String beanName) {
        if (!beans.containsKey(beanName)) {
            throw new BeanNotFoundException("Cannot find bean " + beanName);
        }

        return beans.get(beanName);
    }

    @Override
    public boolean contains(String beanName) {
        return beans.containsKey(beanName);
    }
}
