Micro UI with Vaadin
====================

- This repo: https://github.com/tuhrig/micro-ui-with-vaadin
- Blog post: https://tuhrig.de/micro-ui-with-vaadin

---

This project shows a micro UI with Vaadin (+ Spring Boot + Kotlin).
It contains 3 projects:

- A "main-view" which includes 2 micro UIs (on http://localhost:8080/languages)
- The "left-view" (on http://localhost:8081/languages)
- The "right-view" (on http://localhost:8082/languages)

```
   +=======================================================+
   |                    << Browser Tab >>         x - *    |
   +=======================================================+
   |   Main-View                                           |
   |                                                       |
   |         << IFrame >>             << IFrame >>         |
   |   +---------------------+   +---------------------+   |
   |   | Left-View           |   | Right-View          |   |
   |   |                     |   |                     |   |
   |   |                     |   |                     |   |
   |   |                     |   |                     |   |  
   |   +---------------------+   +---------------------+   |
   |                                                       |
   +=======================================================+
```

## Run locally

From the root folder of this project you can start all 3 services:

    #
    # Starts "left-view" on http://localhost:8081/languages
    #
    ./left-view/gradlew -p ./left-view bootrun

    #
    # Starts "right-view" on http://localhost:8082/languages
    #
    ./right-view/gradlew -p ./right-view bootrun

    #
    # Starts "main-view" on http://localhost:8080/languages
    #    
    ./main-view/gradlew -p ./main-view bootrun

## Example Use-Case

- The "left-app" lets you choose a programming language from a list.
- The "right-app" shows a description for a programming language (from Wikipedia).
- The "main-app" contains both other apps and provides a nice heading on top of it.
