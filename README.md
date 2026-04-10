# 🏃 LIFE CAL System
### Health Tracking & Calorie Management Desktop Application

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![JavaFX](https://img.shields.io/badge/JavaFX-23.0.1-blue.svg)](https://openjfx.io/)
[![SQLite](https://img.shields.io/badge/SQLite-3.x-green.svg)](https://www.sqlite.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📌 Overview

**LIFE CAL System** is a comprehensive desktop application built with JavaFX for tracking health metrics, managing calorie intake, monitoring exercise activities, and visualizing weight progress. Designed for individuals who want to maintain a healthy lifestyle through systematic tracking and data-driven insights.

---

## ✨ Key Features

### 🔐 User Management
- **Registration & Login** with secure password hashing (BCrypt)
- **Profile Management** with customizable health information
- **Session Management** for secure user data access

### 📊 Health Tracking
- **BMI Calculator** - Body Mass Index calculation and categorization
- **BMR Calculator** - Basal Metabolic Rate estimation
- **Weight Progress Tracking** - Visual progress toward weight goals
- **TDEE Calculation** - Total Daily Energy Expenditure based on activity level

### 🍽️ Food Management
- **Food Database** with 150+ food items across 7 categories
- **Food Logging** - Record daily meals with date, meal time, and quantity
- **Nutrition Tracking** - Track calories, protein, carbohydrates, and fat intake
- **Daily Food Log** - View and manage food consumption history

### 🏋️ Exercise Management
- **Exercise Database** with 60+ activities across 5 categories
- **Exercise Logging** - Record workouts with duration and calories burned
- **Daily Exercise Log** - Track and review exercise history

### ⚖️ Weight Management
- **Weight Logging** - Record daily weight measurements
- **Weight History** - Track weight changes over time
- **Automatic Profile Update** - Sync latest weight with user profile

### 📈 Analytics & Reports
- **Calorie Report** - Compare calorie intake vs. calories burned with interactive charts
- **Weight Trend Report** - Visualize weight progress with line graphs
- **Custom Date Ranges** - Filter reports by specific time periods
- **Quick Filters** - 7 days, 30 days, or current month views

---

## 🛠️ Technology Stack

- **Java 17** - Core programming language
- **JavaFX 23.0.1** - Modern UI framework
- **SQLite 3.x** - Embedded database
- **Maven 3.9+** - Build and dependency management
- **BCrypt** - Password hashing for security

### Design Patterns
- **Singleton Pattern** - Session and Database management
- **Repository Pattern** - Data access layer
- **MVC Architecture** - Clean separation of concerns

---

## 📦 Project Structure

```
lifecal-system/
├── src/
│   ├── main/
│   │   ├── java/com/lifecal/
│   │   │   ├── controller/     # UI Controllers
│   │   │   ├── model/          # Data Models
│   │   │   ├── service/        # Business Logic
│   │   │   ├── repository/     # Database Access
│   │   │   ├── common/         # Utilities & Managers
│   │   │   └── util/           # Helper Classes
│   │   └── resources/
│   │       ├── fxml/           # UI Layouts
│   │       └── css/            # Stylesheets
│   └── test/                   # Unit Tests
├── database/                   # SQLite Database
├── pom.xml                     # Maven Configuration
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.9 or higher
- Git (optional)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Mm0444/lifecal-system.git
   cd lifecal-system
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### First-Time Setup

1. Register a new account on the registration page
2. Complete your profile with health information
3. Start tracking your food, exercise, and weight!

### Default Admin Account (Optional)
- **Username:** `admin`
- **Password:** `admin123`

---

## 📊 Database Schema

### Core Tables
- `users` - User profiles and credentials
- `foods` - Food database with nutritional information
- `exercises` - Exercise database with calorie rates
- `food_logs` - Daily food intake records
- `exercise_logs` - Daily exercise records
- `weight_logs` - Weight measurement history

---

## 🎨 Features in Detail

### 1. User Registration & Authentication
- Secure password storage with BCrypt hashing
- Email validation (optional field)
- Comprehensive health profile setup

### 2. Health Metrics Calculation
- **BMI (Body Mass Index)**: `weight (kg) / height (m)²`
- **BMR (Basal Metabolic Rate)**: Mifflin-St Jeor equation
- **TDEE**: BMR × Activity Level multiplier
- **Progress**: `(current - start) / (target - start) × 100%`

### 3. Food & Exercise Tracking
- **7 Food Categories**: Meat, Vegetables, Fruits, Grains, Dairy, Snacks, Beverages
- **5 Exercise Categories**: Walking, Running, Conditioning, Sports, Dancing
- Real-time calorie calculation
- Duplicate prevention (one entry per day for weight)

### 4. Reporting & Analytics
- Interactive line charts (JavaFX LineChart)
- Date range filtering
- Quick period selections
- Summary statistics

---

## 🔧 Configuration

### Database Location
The SQLite database is stored in:
```
database/lifecal.db
```

### Application Settings
- Window size: 1200×800 (default)
- Theme: Light mode only
- Language: Thai (ภาษาไทย)

---

## 📝 Usage Examples

### Adding a Food Entry
1. Navigate to **Food Management** from Dashboard
2. Select a food category
3. Choose meal time, date, and quantity
4. Click **Log** button
5. View in **Daily Food Log**

### Tracking Weight
1. Click **Weight Log** from Dashboard
2. Select date and enter weight
3. Click **Add Entry**
4. View progress in **Weight Report**

### Generating Reports
1. Go to **Reports** section
2. Select date range or use quick filters
3. View statistics and charts
4. Use refresh button to update data

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---


