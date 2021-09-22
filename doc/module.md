# Module

There is no plan to modularized all the things since there is no urgency. But it is ok to introduce new module if there is clear reason. For example `:test-debug` module introduced to separate between
related debugging code, so that we can release the APK without included debugging code by creating `:test-debug:no-op`

## Module convention

The dependency flow is from top layer to child layer. Top layer can depend to child layer but not vice versa

- `feature-xxx` - each feature cant depend
- `lib-xxx` - each lib can depend
- `core-xxx` - each core cant depend
- `test-xxx` - each test cant depend

## Current module

- `:app` - host application
- `:core-logger` - logger functionality
- `:test-debug` - testing related code. Used for debug flavor
- `:test-debug:no-op` - no op version test-debug. Used for release flavor
