# Helping Hands

**Helping Hands** is an Android application designed to bridge the gap between people in need (**Beneficiaries**) and willing donors (**Donors**), moderated and managed by System Administrators (**Admins**).

---

## 🌟 Key Features

- **Multi-Role User Authentication & Registration**
  - **Beneficiary**: Create, view, and track personal help/donation requests.
  - **Donor**: Browse active community requests and offer donations or assistance.
  - **Admin**: Oversee platform activity, manage user roles, and handle request moderation.

- **Request Management**
  - Create, view, update, and manage help requests.
  - Dedicated dashboards tailored for Admin, Beneficiary, and Donor user roles.

- **Local Data Persistence**
  - Built-in SQLite database (`DatabaseHelper`) for managing user accounts, role-based access, and request records.

---

## 🛠️ Tech Stack

- **Language**: Java
- **UI Framework**: Android XML Layouts & Material Components
- **Database**: SQLite (`DatabaseHelper`)
- **Build System**: Gradle with Kotlin DSL (`build.gradle.kts`)

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Hedgehog / Ladybug or newer recommended)
- JDK 17 or higher
- Android SDK & Build Tools installed

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/Helping_Hands.git
   cd Helping_Hands
   ```

2. **Open in Android Studio**
   - Open Android Studio, select **Open**, and select the `Helping_Hands` project directory.

3. **Sync Gradle**
   - Let Android Studio sync the project dependencies automatically.

4. **Run the App**
   - Select an Android Virtual Device (Emulator) or a physical Android device connected via ADB and click **Run** (`Shift + F10`).

---

## 📁 Project Architecture

```
app/src/main/java/com/example/helpinghands/
├── activities/            # UI Activities (Login, Register, Dashboards, Requests)
├── database/              # SQLite Database Helper & Schema Management
├── models/                # Data Models (User, Request, etc.)
├── adapters/              # RecyclerView Adapters
└── utils/                 # Utility & Helper Classes
```

---

## 📄 License

This project is available for use under standard open-source project guidelines.
