
- **UI Layer (Compose)** → Displays state from ViewModel  
- **ViewModel Layer** → Business logic, async testing using coroutines  
- **Core Layer** → Utility classes for network checks, ping, and evaluations  

---

## 🧩 Tech Stack

| Category | Library / Tool |
|-----------|----------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| DI | Dagger Hilt |
| Async | Kotlin Coroutines, StateFlow |
| Networking | OkHttp |
| Navigation | Compose Navigation |
| Testing | JUnit, Espresso |
| Build System | Gradle (KTS) |
| Version Control | Git Flow branching model |

---

## ⚙️ How It Works

1. User opens the app → `SplashScreen` runs for 3 seconds  
2. Navigates automatically to `HomeScreen`  
3. Presses the **“eye” icon** to test connection speed  
4. `HomeViewModel`:
   - Detects connection type (Wi-Fi, Mobile, etc.)
   - Measures ping via TCP socket
   - Uploads sample data with OkHttp
   - Calculates download estimate
   - Evaluates and updates `UiState`
5. `HomeScreen` observes `StateFlow` and updates UI reactively

---

## 🧪 Example UI States

| State | Description |
|--------|--------------|
| `Idle` | Waiting for user action |
| `Scanning` | Test in progress |
| `Connected` | Connection successful, displaying results |
| `Disconnected` | No internet detected |

---

## 🖼️ App Preview

| Splash | Home (Idle) | Home (Connected) |
|--------|--------------|------------------|
| ![Splash](docs/splash.png) | ![Idle](docs/idle.png) | ![Connected](docs/connected.png) |

---

## 🏗️ Project Setup

### 1️⃣ Prerequisites
- Android Studio **Ladybug | 2024.1+**
- JDK **11**
- Gradle **8.0+**
- Android SDK **33+**

### 2️⃣ Clone the repo
```bash
git clone https://github.com/<your-username>/InternetSpeedAnalyzer.git
cd InternetSpeedAnalyzer
