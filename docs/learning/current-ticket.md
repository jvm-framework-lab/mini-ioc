# IOC-004 — Implement Basic Name-Based Singleton BeanFactory

## Status

`CHANGES_REQUESTED`

## Review Context

- Repository: `jvm-framework-lab/mini-ioc`
- Pull Request: `#2`
- Branch: `feature/IOC-004-basic-bean-factory`
- Requested latest commit: `85d3229c388d4773c69f0686d2d514af3e236d3c`
- Review scope: basic name-based singleton bean retrieval
- Final merge condition: all acceptance criteria checked and `mvn clean test` passes

> This ticket deliberately excludes type-based lookup and dependency injection.
> IOC-004 should establish one complete, tested bean retrieval flow before new abstractions are introduced.

---

## Context

The project already provides:

- `BeanDefinition` for bean metadata;
- `BeanDefinitionRegistry` for metadata storage;
- `BeanRegistry` for instantiated bean storage;
- `BeanFactory` as the public bean retrieval API;
- `DefaultBeanFactory` as the default implementation.

IOC-004 connects these components into the first working IoC-container workflow.

The core behavior is:

```text
getBean(beanName)
    ↓
validate beanName
    ↓
check BeanRegistry
    ├── found     → return existing singleton
    └── not found
            ↓
      get BeanDefinition
            ↓
      instantiate by no-argument constructor
            ↓
      register singleton
            ↓
      return managed instance
```

---

## Goal

Implement a minimal but complete `DefaultBeanFactory` that can:

1. retrieve an existing singleton by bean name;
2. create a missing bean from its registered `BeanDefinition`;
3. cache the created instance in `BeanRegistry`;
4. return the same managed instance for subsequent lookups;
5. expose IoC-specific failures instead of reflection implementation details.

---

## Functional Requirements

### 1. Collaborator wiring

`DefaultBeanFactory` must receive these collaborators from outside:

- `BeanDefinitionRegistry`
- `BeanRegistry`

The factory must not silently create private registry instances that registration code cannot access.

Example target shape:

```java
public DefaultBeanFactory(
        BeanRegistry beanRegistry,
        BeanDefinitionRegistry beanDefinitionRegistry
) {
    this.beanRegistry = beanRegistry;
    this.beanDefinitionRegistry = beanDefinitionRegistry;
}
```

---

### 2. Lookup by explicit bean name

The public API for this ticket is:

```java
Object getBean(String beanName);
```

Required behavior:

1. reject invalid input;
2. return an existing registered singleton;
3. retrieve the matching `BeanDefinition` when no instance exists;
4. create the bean;
5. register the created singleton;
6. return it.

The public API must not return `null` for unresolved beans.

---

### 3. Bean creation

IOC-004 supports only a no-argument constructor.

Expected reflection flow:

```java
beanClass.getDeclaredConstructor().newInstance();
```

Do not select a constructor through:

```java
getDeclaredConstructors()[0]
```

Constructor selection and constructor injection belong to later tickets.

---

### 4. Singleton behavior

Every bean is treated as a singleton in this ticket.

Required behavior:

- the first lookup creates the object;
- the object is registered once;
- later lookups return the same object reference.

The primary assertion is:

```java
assertSame(firstLookup, secondLookup);
```

---

### 5. Bean-name validation

The factory must reject:

```text
null
""
"   "
```

A suitable failure for invalid caller input is `IllegalArgumentException`, with a meaningful message.

Example:

```java
if (beanName == null || beanName.isBlank()) {
    throw new IllegalArgumentException(
            "Bean name must not be null or blank"
    );
}
```

---

### 6. Exception boundary

`BeanFactory` must not expose reflection-specific checked exceptions such as:

- `NoSuchMethodException`
- `InvocationTargetException`
- `InstantiationException`
- `IllegalAccessException`

The interface should remain:

```java
Object getBean(String beanName);
```

Reflection failures must be wrapped in an IoC-domain exception such as:

```java
BeanCreationException
```

The exception must preserve:

- bean name;
- bean class;
- original root cause.

Example message:

```text
Failed to create bean 'userService' of type com.example.UserService
```

Missing definitions must produce a meaningful lookup exception, not a silent `null`.

---

## Technical Constraints

Do not implement the following in IOC-004:

- `getBean(Class<T>)`
- lookup by type;
- multiple-candidate resolution;
- `BeanCreator` abstraction;
- `ConstructorResolver`;
- `DependencyResolver`;
- constructor injection;
- prototype scope;
- circular-dependency detection;
- bean lifecycle callbacks;
- `BeanPostProcessor`;
- component scanning;
- annotation processing;
- `ApplicationContext`.

Unused future abstractions should not participate in the IOC-004 execution flow.

---

## Required Tests

Create focused tests for `DefaultBeanFactory`.

Recommended file:

```text
src/test/java/.../factory/DefaultBeanFactoryTest.java
```

Required test cases:

1. `shouldReturnExistingBeanFromRegistry`
2. `shouldCreateBeanFromDefinition`
3. `shouldRegisterCreatedBean`
4. `shouldReturnSameSingletonOnRepeatedLookup`
5. `shouldThrowWhenBeanDefinitionDoesNotExist`
6. `shouldRejectNullBeanName`
7. `shouldRejectBlankBeanName`
8. `shouldWrapCreationFailureWhenDefaultConstructorDoesNotExist`

A generated test such as `assertTrue(true)` does not count as ticket coverage.

---

## Acceptance Criteria

### Factory design

- [x] `DefaultBeanFactory` coordinates `BeanRegistry` and `BeanDefinitionRegistry`.
- [x] Registry collaborators are supplied through the constructor.
- [ ] `DefaultBeanFactory` contains no unused dependency-resolution field or flow.
- [ ] The public `BeanFactory` API exposes no reflection-specific checked exceptions.

### Lookup behavior

- [x] Lookup is supported by explicit bean name.
- [x] An existing singleton is returned from `BeanRegistry`.
- [x] A missing instance is created from its `BeanDefinition`.
- [x] A created bean is registered in `BeanRegistry`.
- [ ] Repeated lookup is verified to return the same object reference.
- [ ] Missing bean definitions fail with a meaningful exception.
- [ ] Lookup never silently returns `null`.

### Input and failure handling

- [ ] Null bean names are rejected.
- [ ] Empty bean names are rejected.
- [ ] Blank bean names are rejected.
- [ ] Bean-creation failures are wrapped in `BeanCreationException`.
- [ ] `BeanCreationException` preserves the original cause.
- [ ] Exception messages include useful bean context.

### Testing and verification

- [ ] `DefaultBeanFactory` behavior is covered by focused unit tests.
- [ ] Positive singleton paths are covered.
- [ ] Negative and reflection-failure paths are covered.
- [ ] `mvn clean test` passes.
- [ ] No generated build output is committed.

---

## Suggested Completion Order

1. Remove unused or future-ticket logic from `DefaultBeanFactory`.
2. Ensure `BeanFactory#getBean(String)` exposes no checked reflection exceptions.
3. Add bean-name validation.
4. Implement meaningful `BeanNotFoundException` behavior.
5. Implement `BeanCreationException` with message and cause.
6. Wrap reflection failures inside `DefaultBeanFactory`.
7. Add behavior-focused unit tests.
8. Run `mvn clean test`.
9. Check the completed acceptance criteria.
10. Request final review before merge.

---

## Definition of Done

IOC-004 is complete only when:

- all required behavior is implemented;
- all acceptance criteria are checked;
- the factory API hides reflection internals;
- no constructor injection or dependency-resolution behavior is included;
- all unit tests pass;
- the Pull Request description accurately reflects the code;
- final review status is `APPROVED`.

---

## Expected Learning Outcomes

After IOC-004, the developer should be able to explain:

1. Why `BeanDefinitionRegistry` and `BeanRegistry` store different things.
2. Why `BeanFactory` coordinates the flow instead of replacing the registries.
3. Why a managed singleton must be cached after creation.
4. Why framework APIs should hide raw reflection exceptions.
5. Why returning `null` hides container configuration errors.
6. Why dependency resolution should be introduced only after basic retrieval is stable.

---

## Review Result

Current result:

```text
CHANGES_REQUESTED
```

The core orchestration direction is correct. Before merge, the ticket still requires verified input validation, domain exception handling, removal of reflection exceptions from the public API, and focused unit tests.

---

## Next Review Input

For final review, provide:

- the latest commit hash;
- output of `mvn clean test`;
- updated PR acceptance-criteria checkboxes;
- source archive or patch if GitHub does not expose the latest commit contents.
