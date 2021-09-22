# Lesson learn

1. Compose: Only pass state that needed
2. Compose: Embrace loose coupling and high cohesion
3. Navigation: navigation compose popup flow

```
-> login screen
-> dashboard screen
 
// pop up to login screen inclusive false
-> login screen
-> login screen new

// pop up to login screen inclusive true
->login new
```

4. DI: tips create module for dep that we don't own
5. Unit test: always inject dispatcher https://medium.com/swlh/unit-testing-with-kotlin-coroutines-the-android-way-19289838d257
6. Data store: we can use chiper or cryptop with data store as data encryption https://blog.stylingandroid.com/datastore-security/
7. Compose: any composable enters the composition when materialized on screen, and finally leaves the composition when removed from the UI tree
8. Room: use @Transaction for operation more than 1 to ensure atomicity
9. Profiling build `gradlew --profile assembleDebug`
10. when vm oncleared called in a compose?
11. What the behavior nav composed?
12. Multiple theme, separate dialog and screen
