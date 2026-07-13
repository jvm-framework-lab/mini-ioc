# Mini IoC Learning Journey

This directory tracks the implementation and learning progress of `mini-ioc`.

The project is developed incrementally through small tickets. Each completed ticket should be reviewed before the next ticket begins.

## Repository

- Repository: `jvm-framework-lab/mini-ioc`
- Main module: `miniframe-ioc`
- Main branch: `main`
- Base package: `io.github.ltnanh21.miniframe.ioc`

## Learning Workflow

1. Read and refine `current-ticket.md`.
2. Create a branch from the latest `main`.
3. Implement only the scope defined by the ticket.
4. Add or update tests.
5. Commit meaningful implementation checkpoints.
6. Open a Pull Request into `main`.
7. Review the Pull Request against its acceptance criteria.
8. Record:
   - ticket result;
   - code-review findings;
   - lesson learned;
   - next ticket.
9. Address required changes.
10. Merge only after the ticket is accepted.

## Branch Naming

Use one branch for one ticket.

```text
feature/IOC-xxx-short-description
refactor/IOC-xxx-short-description
fix/IOC-xxx-short-description
test/IOC-xxx-short-description
```

Example:

```text
feature/IOC-004-implement-bean-factory
```

## Commit Convention

Recommended commit format:

```text
<type>(ioc): <description>
```

Common types:

- `feat`: new behavior;
- `fix`: bug fix;
- `refactor`: structural improvement without changing behavior;
- `test`: test changes;
- `docs`: learning or project documentation;
- `chore`: build and repository maintenance.

Examples:

```text
feat(ioc): implement bean lookup by name
test(ioc): cover singleton bean retrieval
fix(ioc): handle missing bean definitions
docs(learning): add IOC-004 review
```

## Review Status

Each ticket uses one of these statuses:

- `PLANNED`
- `IN_PROGRESS`
- `CHANGES_REQUESTED`
- `APPROVED`
- `COMPLETED`

## Progress

| Ticket | Topic | Status | Review |
|---|---|---:|---|
| IOC-001 | Define bean metadata | COMPLETED | Baseline history |
| IOC-002 | Define bean-definition registry | COMPLETED | Baseline history |
| IOC-003 | Define bean-instance registry | COMPLETED | Baseline history |
| IOC-004 | Implement basic `DefaultBeanFactory` | PLANNED | Pending |

The first three rows represent the foundation already present on `main`. They can be adjusted later when the commit history is formally documented.

## Current Architecture

The current source is organized around four primary areas:

```text
bean/
  BeanDefinition
  InitializationMode
  Scope

registry/
  BeanDefinitionRegistry
  DefaultBeanDefinitionRegistry
  BeanRegistry
  DefaultBeanRegistry

factory/
  BeanFactory
  DefaultBeanFactory
  support/

exception/
```

Current responsibility boundaries:

- `BeanDefinition` stores metadata describing a managed bean.
- `BeanDefinitionRegistry` stores bean metadata.
- `BeanRegistry` stores instantiated bean objects.
- `BeanFactory` is the public retrieval API.
- `DefaultBeanFactory` will coordinate lookup and bean creation.

## Review Document Structure

After every ticket, create:

```text
docs/learning/reviews/IOC-xxx-short-description.md
```

Each review should contain:

1. Review context
2. Ticket result
3. Acceptance-criteria evaluation
4. What was done well
5. Review findings
6. Required changes
7. Lesson learned
8. Connection to Spring Framework
9. Self-check questions
10. Next ticket

## Learning Principles

- Implement one concept at a time.
- Do not add future features before their ticket.
- Prefer explicit behavior over framework magic.
- Keep metadata storage, instance storage, creation, and dependency resolution as separate responsibilities.
- Tests should describe container behavior, not implementation details.
- The goal is to understand why Spring uses its abstractions, not to copy Spring source code directly.

## Planned Roadmap

```text
Bean metadata
    ↓
Registries
    ↓
Basic BeanFactory
    ↓
Bean creation
    ↓
Constructor dependency resolution
    ↓
Singleton and prototype scopes
    ↓
Circular-dependency detection
    ↓
Bean lifecycle
    ↓
BeanPostProcessor
    ↓
ApplicationContext
    ↓
Component scanning
    ↓
Annotation-based injection
    ↓
Configuration classes
    ↓
AOP foundation
```