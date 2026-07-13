## Ticket

<!-- Example: IOC-004 — Implement Basic DefaultBeanFactory -->

- Ticket:
- Current ticket document: `docs/learning/current-ticket.md`

## Summary

<!-- Briefly describe what this Pull Request implements. -->

## Scope

### Included

- 

### Out of scope

- 

## Implementation Notes

<!-- Explain important design decisions. Do not repeat the diff line by line. -->

- 

## Acceptance Criteria

<!-- Copy the ticket criteria and check only what is actually complete. -->

- [ ] 
- [ ] 
- [ ] 

## Tests

### Test cases added or updated

- 

### Verification

- [ ] `mvn test` passes
- [ ] Existing tests still pass
- [ ] Negative/error paths are covered
- [ ] No generated build output is committed

## Design Checklist

- [ ] Each class has one clear responsibility
- [ ] Public APIs do not return silent `null` on failure
- [ ] Exceptions preserve useful context and root causes
- [ ] No future-ticket behavior was added accidentally
- [ ] Mutable internal collections are not exposed
- [ ] Naming reflects IoC-container responsibilities
- [ ] The implementation is understandable without Spring-specific magic

## Files Changed

<!-- Mention the important files and why they changed. -->

| File | Reason |
|---|---|
|  |  |

## Known Limitations

<!-- State limitations intentionally left for later tickets. -->

- 

## Review Focus

<!-- Tell the reviewer where careful review is most useful. -->

- [ ] Correctness
- [ ] Responsibility boundaries
- [ ] API design
- [ ] Exception design
- [ ] Test coverage
- [ ] Naming and package structure
- [ ] Spring Framework conceptual alignment

## Self-Review

<!-- Answer before requesting review. -->

1. What is the most important design decision in this PR?
2. Which behavior is most likely to contain a bug?
3. Which acceptance criterion has the weakest test coverage?
4. What did you intentionally leave for the next ticket?

## Reviewer Output Required

The review should include:

- ticket result: `APPROVED` or `CHANGES_REQUESTED`;
- acceptance-criteria evaluation;
- findings grouped by severity;
- required changes;
- lesson learned;
- connection to Spring Framework;
- self-check questions;
- next ticket.