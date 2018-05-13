# Hanabi

## Prerequisites
- [Android Studio](https://developer.android.com/studio/) `3.1.2` or higher.
- [Gradle](https://gradle.org/) version `4.4` or higher.
- [Java 8](https://en.wikipedia.org/wiki/Java_version_history#Java_SE_8).

## Gradle Dependencies
- List all [Gradle](https://gradle.org/) dependencies here.

## Game Architecture
- [UpdateThread](./app/src/main/java/com/vincentganneau/hanabi/model/UpdateThread.java)
- [DrawThread](./app/src/main/java/com/vincentganneau/hanabi/model/DrawThread.java)

## Code Coverage
| Classes                                                                               | Methods covered   | Lines covered |
| ------------------------------------------------------------------------------------- | -----------------:| -------------:|
| [GameThread](./app/src/main/java/com/vincentganneau/hanabi/model/GameThread.java)     | 100%              | 97%           |
| [UpdateThread](./app/src/main/java/com/vincentganneau/hanabi/model/UpdateThread.java) | 100%              | 100%          |
| [DrawThread](./app/src/main/java/com/vincentganneau/hanabi/model/DrawThread.java)     | 100%              | 88%           |