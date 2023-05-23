Micro UI with Vaadin
====================

This project shows a micro UI with Vaadin (and Kotlin).
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