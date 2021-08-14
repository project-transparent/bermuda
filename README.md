# bermuda
Bermuda is a lightweight framework for making powerful Javac compiler plugins.

Modules:
- core
  - ~~Finish source processor implementation~~ It is impossible to implement source processors without injecting instructions into Javac.
- asm
  - Improve creation of class readers/writers
  - Improve modification of bytecode
- ast
  - Improve modification of trees (wrapper around Javac AST??)
