# IOC-005 — Extract Bean Creation Responsibility

## Status

`PLANNED`

---

## Context

After IOC-004, `DefaultBeanFactory` can:

- retrieve an existing singleton by bean name;
- obtain a `BeanDefinition`;
- create a bean through its no-argument constructor;
- register the created singleton;
- return the managed instance.

The current implementation performs reflection directly inside `DefaultBeanFactory`.

Example responsibility currently owned by the factory:

```java
beanDefinition.getBeanClass()
        .getDeclaredConstructor()
        .newInstance();
```

This works, but bean creation is a separate responsibility from bean lookup and singleton orchestration.

---

## Goal

Extract reflection-based bean instantiation from `DefaultBeanFactory` into a dedicated `BeanCreator` abstraction.

After this ticket:

```text
DefaultBeanFactory
    → coordinates lookup and singleton caching

BeanCreator
    → creates an object from BeanDefinition metadata
```

This ticket must preserve all behavior implemented in IOC-004.

---

## Proposed Design

### BeanCreator

Create:

```java
public interface BeanCreator {

    Object createBean(
            String beanName,
            BeanDefinition beanDefinition
    );
}
```

### ReflectionBeanCreator

Create:

```java
public class ReflectionBeanCreator implements BeanCreator {
}
```

`ReflectionBeanCreator` is responsible for:

- reading the bean class from `BeanDefinition`;
- obtaining its no-argument constructor;
- invoking the constructor;
- wrapping reflection failures in `BeanCreationException`;
- preserving the original root cause;
- including bean name and bean type in the exception message.

---

## Factory Collaboration

`DefaultBeanFactory` must receive `BeanCreator` through constructor injection.

Target constructor:

```java
public DefaultBeanFactory(
        BeanRegistry beanRegistry,
        BeanDefinitionRegistry beanDefinitionRegistry,
        BeanCreator beanCreator
) {
    this.beanRegistry = beanRegistry;
    this.beanDefinitionRegistry = beanDefinitionRegistry;
    this.beanCreator = beanCreator;
}
```

The factory must not instantiate `ReflectionBeanCreator` internally.

---

## Required Flow

```text
getBean(beanName)
    ↓
validate beanName
    ↓
check BeanRegistry
    ├── found → return singleton
    └── missing
            ↓
      get BeanDefinition
            ↓
      BeanCreator.createBean(...)
            ↓
      register singleton
            ↓
      return singleton
```

---

## Functional Requirements

### 1. BeanCreator contract

`BeanCreator` must expose one operation that creates a bean from:

- bean name;
- `BeanDefinition`.

The contract must not expose reflection-specific checked exceptions.

### 2. ReflectionBeanCreator

`ReflectionBeanCreator` must create beans using:

```java
beanClass.getDeclaredConstructor().newInstance();
```

It must not use:

```java
getDeclaredConstructors()[0]
```

### 3. Exception handling

Reflection failures must be wrapped in:

```java
BeanCreationException
```

The exception must contain:

- bean name;
- fully qualified bean class name;
- original root cause.

### 4. DefaultBeanFactory

`DefaultBeanFactory` must delegate object creation to `BeanCreator`.

It must no longer contain:

```java
getDeclaredConstructor()
newInstance()
ReflectiveOperationException
```

### 5. Behavior preservation

All IOC-004 behavior must remain unchanged:

- existing singleton lookup;
- bean-name validation;
- creation from `BeanDefinition`;
- singleton registration;
- repeated lookup returns the same instance;
- meaningful missing-bean exception.

---

## Technical Constraints

Do not implement:

- constructor selection;
- parameterized constructors;
- dependency resolution;
- constructor injection;
- lookup by type;
- prototype scope;
- circular-dependency detection;
- annotation scanning;
- lifecycle callbacks.

`BeanCreator` creates only beans with a no-argument constructor.

---

## Required Tests

### ReflectionBeanCreatorTest

Add focused tests:

1. `shouldCreateBeanUsingDefaultConstructor`
2. `shouldWrapFailureWhenDefaultConstructorDoesNotExist`
3. `shouldIncludeBeanNameInCreationException`
4. `shouldIncludeBeanTypeInCreationException`
5. `shouldPreserveOriginalCause`

### DefaultBeanFactoryTest

Update existing tests so the factory receives a `BeanCreator`.

Existing IOC-004 tests must continue to pass.

At least one factory test should confirm that the bean created by `BeanCreator` is registered and reused as a singleton.

---

## Acceptance Criteria

### BeanCreator

- [ ] `BeanCreator` interface exists.
- [ ] `ReflectionBeanCreator` implements `BeanCreator`.
- [ ] Bean creation uses the no-argument constructor.
- [ ] Reflection-specific checked exceptions do not leak.
- [ ] Creation failures are wrapped in `BeanCreationException`.
- [ ] Exception message contains bean name and bean type.
- [ ] Original root cause is preserved.

### DefaultBeanFactory

- [ ] `BeanCreator` is supplied through constructor injection.
- [ ] `DefaultBeanFactory` delegates bean creation to `BeanCreator`.
- [ ] `DefaultBeanFactory` contains no direct reflection code.
- [ ] Singleton registration remains the responsibility of `DefaultBeanFactory`.
- [ ] Existing IOC-004 behavior remains unchanged.

### Testing

- [ ] `ReflectionBeanCreator` has focused unit tests.
- [ ] Existing `DefaultBeanFactoryTest` tests still pass.
- [ ] Positive creation behavior is covered.
- [ ] Reflection-failure behavior is covered.
- [ ] `mvn clean test` passes.

---

## Suggested Implementation Order

1. Create the `BeanCreator` interface.
2. Implement `ReflectionBeanCreator`.
3. Move reflection and exception wrapping out of `DefaultBeanFactory`.
4. Inject `BeanCreator` into `DefaultBeanFactory`.
5. Update existing factory tests.
6. Add focused tests for `ReflectionBeanCreator`.
7. Run the entire test suite.
8. Refactor only after all tests pass.

---

## Definition of Done

IOC-005 is complete when:

- bean creation is fully delegated to `BeanCreator`;
- `DefaultBeanFactory` contains no reflection code;
- IOC-004 behavior remains unchanged;
- all new and existing tests pass;
- no constructor injection or dependency-resolution logic is introduced;
- final review status is `APPROVED`.

---

## Out of Scope

The following belong to future tickets:

- constructor selection;
- constructor dependency resolution;
- lookup by type;
- qualifiers;
- prototype scope;
- circular-dependency detection;
- annotation processing;
- bean lifecycle.

---

## Expected Learning Outcomes

After completing IOC-005, you should be able to explain:

1. The difference between bean orchestration and bean instantiation.
2. Why `BeanFactory` should not directly depend on reflection details.
3. How dependency inversion makes the factory easier to test and extend.
4. Why `BeanCreator` must not manage singleton caching.
5. Why extracting an abstraction is safer after the original behavior is covered by tests.
