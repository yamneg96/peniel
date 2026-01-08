# 🏥 Hospital Bed Notification System (BNS) - Desktop Edition

A high-performance **Java Swing Desktop Application** designed for real-time bed management and streamlined patient workflows. 

This project is a direct Java translation of the original MERN-stack BNS, providing a premium, native desktop experience for medical staff to track bed availability, patient admissions, and staff assignments in real-time.

---

## ✨ Features
- 🎨 **Modern "Future of Care" UI:** A high-end, responsive desktop interface built with custom Swing components, featuring deep slate gradients and a "Web-style" layout.
- 🛏 **Real-Time Bed Management:** Monitor ward status, assign beds to yourself, and track patient admission/withdrawal status.
- 🔴 **Dynamic Risk Assessment:** Automated categorization of patient risk (Low, Medium, High) with distinct color-coded UI indicators.
- 🔐 **Secure Authentication:** Integrated Login and Registration system with JWT-based session handling.
- 🛡 **Role-Based Access Control (RBAC):** Distinct permissions and UI views for C1, C2, and Medical Staff (Interns).
- ⚡ **Native Performance:** Faster load times and lower memory overhead compared to web-based alternatives.

---

## 🖥 Tech Stack
- ☕ **Core:** Java 17+
- 🖼 **UI Framework:** Java Swing (AWT) using custom rendering for rounded corners and gradients.
- 📐 **Layouts:** Advanced `GridBagLayout` for responsive-style forms and `GridLayout` for split-pane views.
- 🌐 **Networking:** Java `HttpClient` for seamless communication with the BNS backend API.
- 📦 **Data Handling:** GSON for JSON parsing and serialization.

---

## 📂 Project Structure
```plaintext
bed-notification-system-java/
│── src/ 
│   ├── main/
│   │   ├── Application.java      # Main entry point & Router (CardLayout)
│   │   ├── views/                # UI Components (Modern Swing Panels)
│   │   │   ├── HomePanel.java    # Immersive Landing Page with Backdrop
│   │   │   ├── LoginPanel.java   # Split-pane Login with Validation
│   │   │   ├── RegisterPanel.java# Professional Registration & Roles
│   │   │   └── Dashboard.java    # Ward Grid & Bed Control Logic
│   │   ├── services/             # API Connectivity
│   │   │   ├── AuthService.java  # Authentication & Token Management
│   │   │   └── BedService.java   # Bed Status & Patient Sync
│   │   └── models/               # Data Entities (User, Bed, Patient)
│── assets/                       # UI Resources (MedicalHallway.jpg, Icons)
│── bin/                          # Compiled Class files

```

---

## ⚙️ Installation & Setup

### 1️⃣ Build the Project

Ensure you have the JDK 17+ installed. Compile the source files into the `bin` directory:

```bash
# Create bin directory
mkdir bin

# Compile all modules with UTF-8 support
javac -encoding UTF-8 -d bin src/main/*.java src/main/views/*.java src/main/services/*.java

```

### 2️⃣ Run the Application

Launch the main application class:

```bash
java -cp bin main.Application

```

---

## 🚀 Core Workflows

### Authentication

Users can create accounts with professional roles (C1, C2, Medical Staff). The app validates inputs (Email regex, Phone patterns) locally before sending them to the secure auth service.

### Bed Assignment

The Dashboard provides a visual grid of the ward.

* **Available (Green):** Click to assign the bed to yourself.
* **Occupied (Blue):** Shows active patient initials and risk level.
* **Action Menu:** Admit or withdraw patients directly from the desktop UI.

---

## 📌 Roadmap

✅ **Phase 1:** Core UI Navigation & Page Routing.
✅ **Phase 2:** Responsive Split-Pane Login & Registration Layouts.
🔄 **Phase 3:** Real-time Socket integration for instant bed status updates.
🔄 **Phase 4:** System Tray Notifications for critical risk alerts.
🔄 **Phase 5:** Offline mode with local data caching.

---

## 📜 License

MIT License © 2026.