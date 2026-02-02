# IEE App

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

* **[:app](app)** - The main application entry point.
* **[:build-logic](build-logic)** - Custom Gradle convention plugins for build configuration
  sharing.
* **[:benchmark](benchmark)** - Macrobenchmark tests for measuring application performance and
  generation of baseline profiles..

#### ✨ Features

Independent functional modules containing UI and specific business logic:

* **[:feature/announcement](feature/announcement)** - Announcement viewing.
* **[:feature/auth](feature/auth)** - User authentication and login.
* **[:feature/home](feature/home)** - Main announcement feed.
* **[:feature/licenses](feature/licenses)** - Open source license display.
* **[:feature/profile](feature/profile)** - User profile management.
* **[:feature/search](feature/search)** - Search functionality.
* **[:feature/settings](feature/settings)** - App configuration screens.

#### 🧩 Core

Shared components used across different features:

* **[:core/analytics](core/analytics)** - Analytics reporting implementations.
* **[:core/common](core/common)** - Utility classes and extensions.
* **[:core/data](core/data)** - Repositories and data coordination.
* **[:core/database](core/database)** - Room database storage.
* **[:core/datastore](core/datastore)** - Proto Datastore storage.
* **[:core/datastore-proto](core/datastore-proto)** - Module for compiling proto models.
* **[:core/designsystem](core/designsystem)** - Theme, typography, colors and domain components.
* **[:core/domain](core/domain)** - Shared use cases and business rules.
* **[:core/downloader](core/downloader)** - Attachment download logic implementation.
* **[:core/model](core/model)** - Domain entities and data classes.
* **[:core/network](core/network)** - API clients and networking logic.
* **[:core/notifications](core/notifications)** - Push notification handling.
* **[:core/tesing](core/testing)** - Push notification handling.
* **[:core/ui](core/ui)** - Common UI widgets and composables.

## 🛠 Tech Stack

* **Language:** Kotlin
* **UI:** Jetpack Compose (Material 3)
* **Dependency Injection:** Hilt
* **Asynchronicity:** Coroutines & Flow
* **Networking:** Retrofit / OkHttp
* **Persistence:** Room & DataStore
* **Build System:** Gradle Kotlin DSL (with Version Catalogs)

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/kastik/IEE.git](https://github.com/kastik/IEE.git)
   ```

2. **Open in Android Studio:**
   Ensure you are using the latest stable version of Android Studio.

3. **Sync Gradle:**
   Allow Gradle to download dependencies and configure the included builds.

4. **Run:**
   Select the `app` configuration and run on an emulator or physical device.

## 🤝 Contributing

1. Fork the project.
2. Create a feature branch (`git checkout -b feature/NewFeature`).
3. Commit changes.
4. Push to the branch.
5. Open a Pull Request.

## 📄 License

This project is licensed under the MIT License – see the [LICENSE](LICENSE) file for details.