# Unit Tests

These JUnit tests follow the ZOMBIES guide from the testing slides:

- `Z` - Zero: simple post-conditions of a newly created object or module.
- `O` - One: behavior for a single item.
- `M` - Many / more complex: behavior with multiple values or a more involved scenario.
- `B` - Boundary behavior.
- `I` - Interface definition: the tests describe the public methods the modules need.
- `E` - Exceptional behavior.
- `S` - Simple scenarios, tested one by one.

This folder is named `unit-test` because these tests do not require PostgreSQL, JavaFX screens, or a running server.
Database/server/client workflow tests should go in a separate `integration-test` folder.
