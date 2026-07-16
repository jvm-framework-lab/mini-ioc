# IOC-004 Review — Basic Name-Based Singleton BeanFactory

## Final Status

`APPROVED — COMPLETED`

---

## Review Context

- Repository: `jvm-framework-lab/mini-ioc`
- Pull Request: `#2`
- Ticket: `IOC-004`
- Topic: Basic name-based singleton `BeanFactory`
- Final result: Approved after scope cleanup and unit-test completion

---

## Ticket Goal

Implement the first complete bean retrieval flow of the mini IoC container.

```text
getBean(beanName)
    ↓
validate bean name
    ↓
check BeanRegistry
    ├── found → return existing singleton
    └── missing
            ↓
      obtain BeanDefinition
            ↓
      instantiate using no-argument constructor
            ↓
      register singleton
            ↓
      return managed instance
```

IOC-004 intentionally focused on one explicit lookup path and excluded dependency injection, constructor selection and
type-based resolution.

---

## Final Implementation Summary

The completed implementation provides:

- bean lookup by explicit bean name;
- separation between bean metadata and bean instances;
- retrieval of an existing singleton from `BeanRegistry`;
- creation of a missing bean from `BeanDefinition`;
- reflective creation through a no-argument constructor;
- registration of the created singleton;
- repeated lookup returning the same object instance;
- validation of invalid bean names;
- IoC-specific exception handling;
- unit tests for positive and negative behavior.

---

## Acceptance Criteria Result

### Factory Design

- [x] `DefaultBeanFactory` coordinates `BeanRegistry` and `BeanDefinitionRegistry`.
- [x] Registry collaborators are supplied through constructor injection.
- [x] The factory does not create inaccessible private registries.
- [x] The public `BeanFactory` API does not expose reflection-specific checked exceptions.
- [x] Dependency-resolution and constructor-selection logic are excluded from the final PR scope.

### Lookup Behavior

- [x] Beans can be retrieved by explicit bean name.
- [x] Existing singleton instances are returned from `BeanRegistry`.
- [x] Missing instances are created from their `BeanDefinition`.
- [x] Created instances are registered in `BeanRegistry`.
- [x] Repeated lookup returns the same object reference.
- [x] Missing beans fail explicitly instead of returning `null`.

### Input Validation

- [x] Null bean names are rejected.
- [x] Empty bean names are rejected.
- [x] Blank bean names are rejected.
- [x] Invalid input fails early with a meaningful message.

### Exception Handling

- [x] Reflection failures are wrapped in `BeanCreationException`.
- [x] The original reflection exception is preserved as the root cause.
- [x] Creation failures contain useful bean context.
- [x] Callers do not need to understand reflection implementation details.

### Testing

- [x] `DefaultBeanFactory` has focused unit tests.
- [x] Existing-singleton behavior is covered.
- [x] Bean creation is covered.
- [x] Singleton registration is covered.
- [x] Singleton identity is verified with `assertSame`.
- [x] Missing-bean behavior is covered.
- [x] Null, empty and blank names are covered.
- [x] Missing no-argument constructor behavior is covered.
- [x] Nine tests pass.
- [x] Maven build succeeds.

Final verification:

```text
Tests run: 9
Failures: 0
Errors: 0
Skipped: 0
BUILD SUCCESS
```

---

## What Was Done Well

### 1. Correct separation of metadata and instances

```text
BeanDefinitionRegistry
    → stores descriptions of beans

BeanRegistry
    → stores created bean objects
```

### 2. BeanFactory acts as an orchestrator

`DefaultBeanFactory` coordinates input validation, instance lookup, metadata lookup, object creation, singleton
registration and failure translation.

### 3. Singleton behavior is tested by identity

```java
assertSame(firstLookup, secondLookup);
```

This correctly verifies that the same object reference is reused.

### 4. Reflection details are hidden

The public API no longer forces callers to handle reflection-specific checked exceptions.

### 5. Scope was corrected before merge

Future abstractions such as `BeanCreator`, `DependencyResolver` and constructor selection were removed from IOC-004 so
the final PR represents one coherent architectural step.

---

## Review Findings Resolved

- Private registry ownership was replaced with constructor injection.
- Reflection checked exceptions no longer leak from `BeanFactory`.
- Invalid bean names are rejected.
- Generated placeholder tests were replaced with behavior-focused tests.
- Dependency-resolution and constructor-selection changes were removed from the final PR.

---

## Lesson Learned

### 1. BeanFactory and BeanRegistry are different

```text
BeanFactory
    → orchestration

BeanRegistry
    → instance storage
```

### 2. BeanDefinition and bean instance have different lifecycles

```text
registration time
    → BeanDefinition exists

first getBean call
    → bean instance is created

later getBean calls
    → cached singleton is returned
```

### 3. Framework APIs should expose domain language

Callers should reason about bean lookup and bean creation failures, not low-level reflection exceptions.

### 4. Tests describe behavior, not implementation

```text
Given a registered BeanDefinition
When getBean is called twice
Then both calls return the same object
```

This contract remains valid even when creation is later extracted into `BeanCreator`.

### 5. Scope discipline is part of code quality

A PR should represent one reviewable architectural step. Correct future code can still be inappropriate when it expands
the current ticket unnecessarily.

### 6. Extract abstractions after behavior becomes clear

IOC-004 established stable and tested behavior first. IOC-005 can now safely extract `BeanCreator`.

---

## Connection to Spring Framework

IOC-004 introduces several foundational ideas also present in Spring:

- bean metadata is separate from instantiated singleton objects;
- bean lookup is coordinated through a factory;
- singleton objects are cached and reused;
- creation failures are translated into container-specific exceptions;
- callers depend on a stable container API rather than reflection directly.

```text
bean definition
    ↓
bean factory
    ↓
bean creation
    ↓
singleton storage
    ↓
managed bean
```

---

## Self-Check Questions

1. Why should `DefaultBeanFactory` receive registries instead of constructing them internally?
2. What is the difference between a `BeanDefinition` and a bean instance?
3. Why is `assertSame` more appropriate than `assertEquals` for singleton tests?
4. Why should `BeanFactory#getBean` not expose `NoSuchMethodException`?
5. Which component should own singleton caching?
6. Why was `DependencyResolver` excluded from IOC-004?
7. What behavior must remain unchanged after extracting `BeanCreator`?
8. What happens if a newly created bean is not registered in `BeanRegistry`?

---

## Architecture After IOC-004

```text
BeanFactory
└── DefaultBeanFactory
    ├── BeanDefinitionRegistry
    └── BeanRegistry
```

Current responsibilities:

```text
DefaultBeanFactory
    → validate lookup request
    → coordinate registries
    → create no-argument bean
    → register singleton
    → return managed object

BeanDefinitionRegistry
    → store bean metadata

BeanRegistry
    → store instantiated singleton beans
```

---

## Next Ticket

`IOC-005 — Extract Bean Creation Responsibility`

IOC-005 will extract reflection-based instantiation from `DefaultBeanFactory` into:

```text
BeanCreator
└── ReflectionBeanCreator
```

It must preserve all IOC-004 behavior and will not introduce constructor injection, dependency resolution, constructor
selection, lookup by type or prototype scope.

---

## Final Decision

```text
APPROVED
```

IOC-004 has achieved its implementation and learning goals. The project now has a complete, tested name-based singleton
retrieval flow and is ready for IOC-005.