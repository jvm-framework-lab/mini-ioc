package io.github.ltnanh21.miniframe.ioc.factory;

import io.github.ltnanh21.miniframe.ioc.bean.BeanDefinition;
import io.github.ltnanh21.miniframe.ioc.bean.InitializationMode;
import io.github.ltnanh21.miniframe.ioc.bean.Scope;
import io.github.ltnanh21.miniframe.ioc.exception.BeanCreationException;
import io.github.ltnanh21.miniframe.ioc.exception.BeanDefinitionNotFoundException;
import io.github.ltnanh21.miniframe.ioc.registry.BeanDefinitionRegistry;
import io.github.ltnanh21.miniframe.ioc.registry.BeanRegistry;
import io.github.ltnanh21.miniframe.ioc.registry.DefaultBeanDefinitionRegistry;
import io.github.ltnanh21.miniframe.ioc.registry.DefaultBeanRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultBeanFactoryTest {
    private BeanRegistry beanRegistry;
    private BeanDefinitionRegistry beanDefinitionRegistry;
    private BeanFactory beanFactory;

    static class TestService {
    }

    static class BeanWithoutDefaultConstructor {

        BeanWithoutDefaultConstructor(String value) {
        }
    }

    @BeforeEach()
    void setup() {
        beanRegistry = new DefaultBeanRegistry();
        beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
        beanFactory = new DefaultBeanFactory(beanRegistry, beanDefinitionRegistry);
    }

    @Test
    void shouldReturnExistingBeanFromRegistry() {
        // Arrange
        TestService existingBean = new TestService();
        beanRegistry.register("testService", existingBean);

        // Act
        Object result = beanFactory.getBean("testService");

        // Assert
        assertSame(existingBean, result);
    }

    @Test
    void shouldCreateBeanFromDefinition() {
        // Arrange
        BeanDefinition definition = createDefinition();

        beanDefinitionRegistry.register("testService", definition);

        // Act
        Object result = beanFactory.getBean("testService");

        // Assert
        assertNotNull(result);
        assertInstanceOf(TestService.class, result);
    }

    @Test
    void shouldRegisterCreatedBean() {
        // Arrange
        BeanDefinition definition = createDefinition();

        beanDefinitionRegistry.register("testService", definition);

        // Act
        Object createdBean = beanFactory.getBean("testService");

        // Assert
        assertTrue(beanRegistry.contains("testService"));
        assertSame(createdBean, beanRegistry.get("testService"));
    }

    @Test
    void shouldReturnSameSingletonOnRepeatedLookup() {
        // Arrange
        BeanDefinition definition = createDefinition();

        beanDefinitionRegistry.register("testService", definition);

        // Act
        Object firstLookup = beanFactory.getBean("testService");
        Object secondLookup = beanFactory.getBean("testService");

        // Assert
        assertSame(firstLookup, secondLookup);
    }

    @Test
    void shouldThrowWhenBeanDefinitionDoesNotExist() {
        BeanDefinitionNotFoundException exception = assertThrows(BeanDefinitionNotFoundException.class, () -> beanFactory.getBean("missingBean"));

        assertTrue(exception.getMessage().contains("missingBean"));
    }

    @Test
    void shouldRejectNullBeanName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> beanFactory.getBean(null));

        assertTrue(exception.getMessage().contains("must not be null or blank"));
    }

    @Test
    void shouldRejectEmptyBeanName() {
        assertThrows(IllegalArgumentException.class, () -> beanFactory.getBean(""));
    }

    @Test
    void shouldRejectBlankBeanName() {
        assertThrows(IllegalArgumentException.class, () -> beanFactory.getBean("   "));
    }

    @Test
    void shouldWrapCreationFailureWhenDefaultConstructorDoesNotExist() {
        // Arrange
        BeanDefinition definition = createDefinition(BeanWithoutDefaultConstructor.class);

        beanDefinitionRegistry.register(
                "invalidBean",
                definition
        );

        // Act
        BeanCreationException exception = assertThrows(
                BeanCreationException.class,
                () -> beanFactory.getBean("invalidBean")
        );

        // Assert
        assertTrue(
                exception.getMessage().contains("invalidBean")
        );

        assertNotNull(exception.getCause());

        assertInstanceOf(
                NoSuchMethodException.class,
                exception.getCause()
        );
    }

    private BeanDefinition createDefinition() {
        return new BeanDefinition(TestService.class, InitializationMode.EAGER, Scope.SINGLETON);
    }

    private BeanDefinition createDefinition(Class<?> beanClass) {
        return new BeanDefinition(beanClass, InitializationMode.EAGER, Scope.SINGLETON);
    }
}

