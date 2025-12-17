# Gemini Project: E-shop

## Project Overview

This is a native Android application named "E-shop". It is built using the Kotlin programming language and the modern Jetpack Compose toolkit for the user interface. The project follows a standard Android application structure and uses Gradle for dependency management and build automation.

The main entry point for the application is the `MainActivity.kt` file, located in `app/src/main/java/com/example/e_shop/`.

## Remote Repository

The remote Git repository for this project is located at: [https://github.com/shfaysal/E-shop.git](https://github.com/shfaysal/E-shop.git)

## Building and Running

This project uses Gradle as its build system.

### Building the Project

To build the project from the command line, you can use the `gradlew` wrapper script:

```bash
./gradlew build
```

### Running the Application

To run the application, it is recommended to use Android Studio. You can open the project in Android Studio and run the `app` configuration on an Android emulator or a physical device.

### Running Tests

To run the unit tests for this project, you can use the following command:

```bash
./gradlew test
```

To run instrumented tests, use:

```bash
./gradlew connectedAndroidTest
```

## Development Conventions

*   **Language:** The project is written entirely in Kotlin. New code should also be in Kotlin.
*   **UI:** The user interface is built with Jetpack Compose. Follow Compose best practices for building UI components.
*   **Styling:** The application's theme is defined in `app/src/main/java/com/example/e_shop/ui/theme/`.
*   **Dependencies:** Project dependencies are managed in the `gradle/libs.versions.toml` file. Follow this convention for adding or updating dependencies.

# Android App Rules (Kotlin + Compose)

You are working in an Android app using:
- Kotlin, Jetpack Compose, Material 3
- MVVM + Clean-ish layers (ui, domain, data)
- Retrofit + OkHttp
- Kotlinx Serialization
- Hilt for DI
- Navigation Compose
- Room (optional) + DataStore
- Coroutines + MutableStateFlow or Flow (best)

Coding rules:
- No “god” ViewModels. One per screen or per feature.
- UI state as immutable data classes.
- Use sealed interface for UiEvent/UiEffect.
- Always update tests when adding logic.
- Prefer small, reviewable commits.

## API Documentation

- [Dummy JSON API](https://api.escuelajs.co/docs)