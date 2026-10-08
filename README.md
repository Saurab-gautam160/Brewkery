# Brewkery ☕🍰

Brewkery is a modern Android application built for an artisan coffee shop and bakery. Built natively with **Kotlin** and **Jetpack Compose**, it provides a smooth, responsive, and delightful user experience for browsing fresh roasts, customizing coffee orders, and managing cart items.

---

download link [ https://drive.google.com/file/d/1r1-nTy37Yr274AoxAobdZEmpDqDRARcl/view?usp=drive_link]

## 📱 Features

* **Artisan Menu Display**: Seamlessly view coffee and bakery offerings with custom badge tags, price formatting, and image loading.
* **Top Bar & Dynamic Badge**: Live shopping cart badge updates dynamically as items are added.
* **Customization**: Interactive detail screens allowing users to customize drink sizes, milk preferences, and extras.
* **Reactive State Management**: Built using Kotlin `StateFlow` and unidirectional data flow (UDF) patterns to keep the UI perfectly synchronized with application state.
* **Clean UI**: Custom warm theme palette matching artisanal coffee shop aesthetics.

---

## 🛠 Tech Stack & Libraries

* **Language**: [Kotlin](https://kotlinlang.org/)
* **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Architecture**: MVVM (Model-View-ViewModel) + Unidirectional Data Flow
* **Async & Streams**: Kotlin Coroutines & `StateFlow`
* **Image Loading**: [Coil for Compose](https://coil-kt.github.io/coil/compose/)
* **Navigation**: Jetpack Compose Navigation

---

## 🤖 AI Tools & Assistance

This project leveraged AI tools (**Google Gemini**) throughout the development lifecycle as an interactive pairing partner. AI was utilized across several key areas:

1. **Jetpack Compose UI & Layouts**:
   * Assisted in designing declarative Compose screens, custom cards, and layout hierarchy (`MenuScreen`, `MainTopBar`, and `MenuItemCard`).
   * Designed custom visual elements, including the warm theme palette (`0xFFFAF7F2`), rounded card shapes, and custom notification badges.

2. **State Management & ViewModel Integration**:
   * Guided the implementation of reactive UI updates using `StateFlow` and `collectAsState`.
   * Ensured unidirectional data flow between `BrewkeryViewModel` state classes (`UiState`) and Compose composables.

3. **Debugging & Refactoring**:
   * Diagnosed and resolved compiler errors such as Kotlin type inference issues, missing state property delegates (`getValue`), and parameter mismatches across navigation routes.
   * Replaced local drawable XML dependency bottlenecks with native Compose Material Icons for improved portability.

4. **Documentation & Best Practices**:
   * Assisted in structuring project documentation, writing clean code comments, and establishing standardized commit setups.

---

## 🚀 Getting Started

### Prerequisites
* **Android Studio**: Ladybug or newer recommended.
* **JDK**: Version 17 or higher.
* **Min SDK**: API 24 (Android 7.0)
* **Target SDK**: API 34 / 35

### Building & Running

1. **Clone the Repository**:
   ```bash
   git clone [https://github.com/YOUR_GITHUB_USERNAME/Brewkery.git](https://github.com/YOUR_GITHUB_USERNAME/Brewkery.git)
   cd Brewkery
