Joinfaces rewrite autoconfiguration issue showcase
===================================================

Example simplified and isolated of issues found in the default rewrite configuration provided by joinfaces.

This showcase contains 3 different maven projects with the following relationship:
- `custom-starter` does not depend on any other project.
- `other-module` depends on `custom-starter` to simplify dependency management and  to allow define JSF beans an rewrite rules
- `app` depends on `custom-starter` and `other-module`. It has it's own jsf beans, rewrite rules and xhtml views, and includes those defined in `other-module`

`app` must have the following urls working: 
- http://localhost:8080/
- http://localhost:8080/magic
- http://localhost:8080/debug
- http://localhost:8080/words