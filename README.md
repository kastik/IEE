# IEE IHU

![Kotlin](https://img.shields.io/badge/kotlin-100%25-blue?style=for-the-badge&logo=kotlin)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![License](https://img.shields.io/github/license/kastik/IEE?style=for-the-badge)

## 📱 Overview

This application retrieves and displays announcements from the **Department of Information and
Electronic Engineering (IEE)** at the **International Hellenic University (IHU)**. It is built using
modern Android development practices, emphasizing modularization and clean architecture.

## 🛠 Tech Stack

| Category         | Technology                |
|------------------|---------------------------|
| **Language**     | Kotlin                    |
| **UI Toolkit**   | Jetpack Compose           |
| **DI**           | Hilt                      |
| **Architecture** | MVVM (Clean Architecture) |
| **Design**       | Material You              |
| **Networking**   | Retrofit / OkHttp         |
| **Persistence**  | Room & DataStore          |
| **Build System** | Gradle                    |
| **CI/CD**        | GitHub Actions            |

## 🚀 Getting Started

### Prerequisites

* **Android Studio:** Meerkat or newer (AGP 9.1.0+).
* **JDK:** 21 or higher.
* **Android SDK:** API 36 or higher

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kastik/IEE.git
   ```

2. **Open in Android Studio:**
   Allow Gradle to download dependencies and configure the included builds.

3. **Run:**
   Select the **app** configuration and run on an emulator or physical device.

## ⚙️ Environment Variables

The release signing configuration can be provided via `local.properties` or environment variables:

| Property (`local.properties`) | Environment Variable     | Description                       |
|-------------------------------|--------------------------|-----------------------------------|
| `store.file`                  | `RELEASE_STORE_FILE`     | Path to the release keystore file |
| `store.password`              | `RELEASE_STORE_PASSWORD` | Keystore password                 |
| `key.alias`                   | `RELEASE_KEY_ALIAS`      | Signing key alias                 |
| `key.password`                | `RELEASE_KEY_PASSWORD`   | Signing key password              |

> **Note:** `local.properties` values take precedence over environment variables.

## 🧪 Tests

The project uses the following testing libraries:

* **Unit Tests:** JUnit 5 (Platform Suite Engine 6.0.3), MockK 1.14.9, Robolectric 4.16.1
* **UI / Instrumented Tests:** Espresso 3.7.0, UI Automator 2.4.0
* **Benchmarks:** AndroidX Macro Benchmark 1.5.0

## 📂 Project Structure

```
IEE/
├── app/                          # Application entry point and dependency graph root
├── benchmark/                    # Macrobenchmark & Baseline Profile generation
├── build-logic/                  # Gradle Convention Plugins for build configuration
├── core/
│   ├── analytics/                # Analytics reporting implementations
│   ├── common/                   # Utility classes and extensions
│   ├── config/                   # Remote/app configuration
│   ├── crashlytics/              # Crashlytics integration
│   ├── data/                     # Repository implementations and data arbitration
│   ├── database/                 # Local persistence (Room)
│   ├── datastore/                # Proto DataStore implementation
│   ├── datastore-proto/          # Protobuf schema definitions for DataStore
│   ├── designsystem/             # Material 3 theme, iconography, and atomic UI components
│   ├── domain/                   # Shared business use cases
│   ├── downloader/               # Attachment download management
│   ├── model/                    # Canonical domain entities
│   ├── network/                  # Retrofit clients and API definitions
│   ├── notifications/            # Push notification handling
│   ├── ui/                       # Generic UI widgets and Compose extensions
│   └── work/                     # WorkManager background tasks
├── dev-tools/                    # Debug-only developer tools and utilities
├── feature/
│   ├── announcement/             # Announcement details and interaction
│   ├── auth/                     # Authentication flows (Login)
│   ├── home/                     # Primary dashboard and feed aggregation
│   ├── licenses/                 # Open Source license attribution
│   ├── profile/                  # User profile and subscription management
│   ├── search/                   # Querying and filtering functionality
│   └── settings/                 # Application configuration and preferences
├── gradle/
│   └── libs.versions.toml        # Version catalog
├── http/                         # HTTP request files for API testing
├── build.gradle.kts              # Root build script
├── settings.gradle.kts           # Module inclusion and repository config
├── gradle.properties             # Gradle and Android build properties
└── stability_config.conf         # Compose stability configuration
```

## 🤝 Contributing

1. Fork the project.
2. Create a feature branch (`git checkout -b feature/NewFeature`).
3. Commit changes.
4. Push to the branch.
5. Open a Pull Request.

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.
