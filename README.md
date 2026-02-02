# IEE IHU

![Kotlin](https://img.shields.io/badge/kotlin-100%25-blue?style=for-the-badge&logo=kotlin)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![License](https://img.shields.io/github/license/kastik/IEE?style=for-the-badge)

## 📱 Overview

This application retrieves and displays announcements from the **Department of Information and
Electronic Engineering (IEE)** at the **International Hellenic University (IHU)**. It is built using
modern Android development practices, emphasizing modularization and clean architecture.

### 📂 Module Structure

The codebase is organized into the following logical groups:

#### 🚀 Application & Build

* **[:app](app)** - The application entry point and dependency graph root.
* **[:build-logic](build-logic)** - Custom Gradle Convention Plugins for centralized build
  configuration management.
* **[:benchmark](benchmark)** - Macrobenchmark implementation for performance monitoring and
  Baseline Profile generation.

#### ✨ Features

* **[:feature/announcement](feature/announcement)** - Announcement details and interaction.
* **[:feature/auth](feature/auth)** - Authentication flows (Login).
* **[:feature/home](feature/home)** - Primary dashboard and feed aggregation.
* **[:feature/profile](feature/profile)** - User profile and subscription management.
* **[:feature/search](feature/search)** - Querying and filtering functionality.
* **[:feature/settings](feature/settings)** - Application configuration and preferences.
* **[:feature/licenses](feature/licenses)** - Open Source license attribution.

#### 🧩 Core

* **[:core/model](core/model)** - Canonical domain entities.
* **[:core/domain](core/domain)** - Shared business use cases.
* **[:core/data](core/data)** - Repository implementations and data arbitration
* **[:core/network](core/network)** - Retrofit clients and API definitions.
* **[:core/database](core/database)** -Local persistence (Room).
* **[:core/datastore](core/datastore)** - Proto DataStore implementation.
* **[:core/designsystem](core/designsystem)** - Material 3 Theme, iconography, and atomic UI
  components.
* **[:core/ui](core/ui)** - Generic UI widgets and Compose extensions.
* **[:core/common](core/common)** - Utility classes and extensions.
* **[:core/analytics](core/analytics)** - Analytics reporting implementations.
* **[:core/notifications](core/notifications)** - Push notification handling.
* **[:core/downloader](core/downloader)** - Attachment download management.
* **[:core/tesing](core/testing)** - Shared test rules, fixtures, and utilities.

## 🛠 Tech Stack

**Architecture**

* **Pattern:** MVVM / MVI (Clean Architecture)
* **DI:** Hilt (Dagger)
* **Concurrency:** Kotlin Coroutines & Flow

**User Interface**

* **Toolkit:** Jetpack Compose
* **Design:** Material Design 3

**Data & Networking**

* **Network:** Retrofit / OkHttp
* **Serialization:** Kotlinx Serialization
* **Persistence:** Room (SQLite) & Proto DataStore

**Build & Tooling**

* **Build System:** Gradle Kotlin DSL
* **Dependency Management:** Version Catalogs (`libs.versions.toml`)
* **CI/CD:** GitHub Actions (Implied)

## 🚀 Getting Started

### Prerequisites

* **Android Studio:** Panda.
* **JDK:** Java 21 or higher.

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/kastik/IEE.git](https://github.com/kastik/IEE.git)
   ```

2. **Open in Android Studio:**
   Allow Gradle to download dependencies and configure the included builds.

3. **Run:**
   Select the `app` configuration and run on an emulator or physical device.

## Contributing

1. Fork the project.
2. Create a feature branch (`git checkout -b feature/NewFeature`).
3. Commit changes.
4. Push to the branch.
5. Open a Pull Request.