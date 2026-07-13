# IOC-004 — Implement Basic DefaultBeanFactory

## Status

`PLANNED`

## Context

The project already defines:

- bean metadata through `BeanDefinition`;
- a `BeanDefinitionRegistry` for metadata;
- a `BeanRegistry` for instantiated objects;
- a `BeanFactory` contract with lookup by name and type;
- a `DefaultBeanFactory` skeleton whose methods currently return `null`.

This ticket connects the existing abstractions into the first working retrieval flow.

## Goal

Implement the minimum useful behavior of `DefaultBeanFactory` so that it can retrieve already-created beans and create simple singleton beans from registered `BeanDefinition` metadata.

The objective is to understand the coordination role of a bean factory without introducing constructor dependency injection yet.

## Functional Requirements

### 1. Factory dependencies

`DefaultBeanFactory` must work with:

- one `BeanDefinitionRegistry`;
- one `BeanRegistry`.

These collaborators should be supplied to the factory rather than created invisibly inside lookup methods.

### 2. Lookup by bean name

Implement:

```java
Object getBean(String name)
```

Expected flow:

1. Validate the requested bean name.
2. Check `BeanRegistry`.
3. Return the existing instance when found.
4. Otherwise, obtain its `BeanDefinition`.
5. Create the bean using a no-argument constructor.
6. Register the created instance in `BeanRegistry`.
7. Return the created instance.

### 3. Lookup by required type

Implement:

```java
<T> T getBean(Class<T> requiredType)
```

Expected behavior:

1. Validate `requiredType`.
2. Find matching bean definitions by type.
3. If exactly one definition matches, resolve it through the name-based flow.
4. Return the result cast to the required type.
5. Fail explicitly when no candidate exists.
6. Fail explicitly when multiple candidates exist.

Do not silently choose the first matching definition.

### 4. Singleton behavior

For this ticket, every created bean is treated as a singleton:

- the first lookup creates the instance;
- later lookups return the same object reference;
- creation must not happen again after registration.

`Scope.PROTOTYPE` behavior is intentionally excluded from this ticket even if the enum already exists.

### 5. Failure behavior

Do not return `null` for failed bean resolution.

Use existing domain exceptions where suitable. Add a minimal IoC-specific exception only when the current exception package does not already provide an appropriate type.

Failure messages should identify at least:

- requested bean name or required type;
- the operation that failed;
- the underlying cause when reflection fails.

## Technical Constraints

- Do not implement constructor dependency injection.
- Do not select constructors with parameters.
- Do not implement annotation scanning.
- Do not implement bean lifecycle callbacks.
- Do not implement lazy/eager startup processing.
- Do not implement circular-dependency handling.
- Do not redesign all existing registries in the same ticket unless a blocking issue is demonstrated.
- Do not catch exceptions and replace them with an uninformative generic message.
- Do not expose mutable internal registry maps.

## Required Tests

Replace or supplement the generated `AppTest` with focused unit tests.

At minimum, cover:

1. Existing bean is returned from `BeanRegistry`.
2. Missing instance is created from its `BeanDefinition`.
3. The created bean is registered.
4. Two lookups by name return the same instance.
5. Lookup by type returns the registered bean.
6. Missing name throws an explicit exception.
7. Missing type throws an explicit exception.
8. Multiple beans matching one type throw an explicit exception.
9. Bean without an accessible no-argument constructor fails with a meaningful exception.
10. Null or blank lookup input is rejected.

## Acceptance Criteria

- [ ] `DefaultBeanFactory` receives both registries as collaborators.
- [ ] `getBean(String)` returns an existing registered instance.
- [ ] `getBean(String)` creates a missing instance from `BeanDefinition`.
- [ ] A newly created instance is stored in `BeanRegistry`.
- [ ] Repeated lookup returns the same singleton instance.
- [ ] `getBean(Class<T>)` resolves exactly one matching bean.
- [ ] Missing beans never result in a silent `null`.
- [ ] Ambiguous type lookup is rejected.
- [ ] Reflection failures preserve the original cause.
- [ ] The required behavior is covered by unit tests.
- [ ] `mvn test` passes.

## Suggested Implementation Order

1. Inspect registry APIs and identify missing operations needed by the factory.
2. Add tests for returning an existing bean.
3. Implement name-based lookup from `BeanRegistry`.
4. Add tests for creating a no-argument bean.
5. Implement metadata lookup and reflective instantiation.
6. Register created singleton instances.
7. Add type-based lookup tests.
8. Implement type-based candidate resolution.
9. Add negative and ambiguity tests.
10. Refactor only after all behavior is covered.

## Definition of Done

The ticket is done when:

- all acceptance criteria are checked;
- all tests pass;
- the PR contains no unrelated feature;
- review findings marked as blocking are addressed;
- the final review status is `APPROVED`.

## Out of Scope

The following belong to later tickets:

- constructor selection;
- recursive dependency resolution;
- constructor injection;
- prototype scope;
- circular-dependency detection;
- annotation scanning;
- bean post-processors;
- lifecycle callbacks;
- eager initialization;
- application context.

## Expected Lesson Learned

After completing this ticket, you should be able to explain:

1. Why `BeanFactory` does more than call `new`.
2. The difference between bean metadata and a bean instance.
3. Why the factory coordinates registries instead of replacing them.
4. Why singleton caching belongs to instance management.
5. Why returning `null` from a container API hides configuration errors.
6. Why type-based lookup must handle ambiguity explicitly.