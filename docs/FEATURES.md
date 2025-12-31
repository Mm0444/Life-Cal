# LIFE CAL System - Features Documentation

## 📋 Table of Contents
- [User Management](#user-management)
- [Health Metrics](#health-metrics)
- [Food Management](#food-management)
- [Exercise Management](#exercise-management)
- [Weight Tracking](#weight-tracking)
- [Reports & Analytics](#reports--analytics)

---

## 🔐 User Management

### Registration
- **Username**: Unique, 3-20 characters
- **Email**: Optional, validated format
- **Password**: Secure BCrypt hashing
- **Profile Setup**: Weight, height, target weight, activity level, exercise frequency

### Login
- **Authentication**: BCrypt password verification
- **Session Management**: Singleton pattern
- **Security**: No plain-text password storage

### Profile Management
- **View Profile**: Display all user information
- **Edit Profile**: Update health metrics and goals
- **Goal Reset**: Automatically resets progress when target weight changes

---

## 📊 Health Metrics

### BMI (Body Mass Index)
```
BMI = weight (kg) / (height (m))²
```

**Categories:**
- < 18.5: น้ำหนักน้อย (Underweight)
- 18.5-24.9: ปกติ (Normal)
- 25.0-29.9: น้ำหนักเกิน (Overweight)
- ≥ 30.0: อ้วน (Obese)

### BMR (Basal Metabolic Rate)
```
BMR = 10W + 6.25H - 5A - 161 (Mifflin-St Jeor Equation)
```
Where:
- W = Weight in kg
- H = Height in cm
- A = Age in years

### TDEE (Total Daily Energy Expenditure)
```
TDEE = BMR × Activity Multiplier
```

**Activity Levels:**
- Sedentary: BMR × 1.2
- Light Active: BMR × 1.375
- Moderately Active: BMR × 1.55
- Very Active: BMR × 1.725
- Extremely Active: BMR × 1.9

### Weight Progress
```
Progress = (Current - Start) / (Target - Start) × 100%
```

**Visual Indicators:**
- Progress bar on dashboard
- Color-coded status
- Goal reached notification

---

## 🍽️ Food Management

### Food Database
**7 Categories with 150+ items:**

1. **เนื้อสัตว์ (Meat)** - 35 items
   - Chicken, pork, beef, seafood
   - Nutritional info per serving

2. **ผัก (Vegetables)** - 25 items
   - Fresh vegetables
   - Low calorie options

3. **ผลไม้ (Fruits)** - 20 items
   - Seasonal fruits
   - Natural sugars tracked

4. **ธัญพืช (Grains)** - 25 items
   - Rice, noodles, bread
   - Complex carbohydrates

5. **นม (Dairy)** - 15 items
   - Milk, yogurt, cheese
   - Protein sources

6. **ขนม (Snacks)** - 20 items
   - Healthy and indulgent options
   - Portion controlled

7. **เครื่องดื่ม (Beverages)** - 10 items
   - Coffee, tea, juices
   - Calorie tracking

### Food Logging
- **Date Selection**: Any past or present date
- **Meal Time**: เช้า, กลางวัน, เย็น, ของว่าง
- **Quantity**: Flexible serving sizes
- **Automatic Calculation**: Total calories = kcal/serving × quantity

### Daily Food Log
- **View by Date**: Filter by specific date
- **Total Calories**: Sum of all meals
- **Delete Entries**: Remove logged items
- **Sorting**: Organized by meal time

---

## 🏋️ Exercise Management

### Exercise Database
**5 Categories with 60+ activities:**

1. **การเดิน (Walking)** - 5 types
   - Different speeds and intensities
   - Calories per minute

2. **การวิ่ง (Running)** - 6 types
   - Jogging to sprinting
   - Treadmill and outdoor

3. **การบริหารร่างกาย (Conditioning)** - 20 types
   - Strength training
   - Flexibility exercises

4. **กีฬา (Sports)** - 20 types
   - Team and individual sports
   - Competitive and recreational

5. **การเต้น (Dancing)** - 9 types
   - Various dance styles
   - Social and fitness dancing

### Exercise Logging
- **Date Selection**: Record any date
- **Duration**: Minutes of activity
- **Automatic Calculation**: Calories burned = kcal/min × duration

### Daily Exercise Log
- **View by Date**: Filter by specific date
- **Total Burned**: Sum of all activities
- **Delete Entries**: Remove logged exercises
- **Progress Tracking**: Monitor exercise habits

---

## ⚖️ Weight Tracking

### Weight Logging
- **Daily Records**: One entry per day
- **Automatic Profile Sync**: Updates user's current weight
- **Duplicate Prevention**: Cannot log twice for same date

### Weight Statistics
- **Start Weight**: First recorded weight
- **Current Weight**: Latest recorded weight
- **Weight Change**: Difference with color coding
  - 🔴 Red: Weight increased
  - 🟢 Green: Weight decreased
  - ⚪ Gray: No change
- **Total Entries**: Number of records

### Weight History
- **Chronological List**: All weight entries
- **Delete Option**: Remove any entry
- **Auto-Update**: Recalculates stats on delete

---

## 📈 Reports & Analytics

### Calorie Report
**Features:**
- **Date Range Selection**: Custom start and end dates
- **Quick Filters**:
  - 7 วันล่าสุด (Last 7 days)
  - 30 วันล่าสุด (Last 30 days)
  - เดือนนี้ (This month)

**Statistics:**
- Total Calorie Intake
- Total Calories Burned
- Net Calories (Intake - Burned)
- Average Daily Intake

**Visualization:**
- Line chart with 2 series
- Intake vs. Burned comparison
- Interactive date labels

### Weight Trend Report
**Features:**
- **All-time View**: Complete weight history
- **Automatic Loading**: Shows all data on open

**Statistics:**
- Start Weight
- Current Weight
- Weight Change (with color)
- Total Entries

**Visualization:**
- Line chart showing weight progression
- Date labels (MM/dd/yy format)
- Symbol markers for data points

---

## 🎨 UI/UX Features

### Design Elements
- **Modern Cards**: Clean, card-based layout
- **Color Coding**: Status-based colors
- **Icons**: Emoji icons for quick recognition
- **Progress Bars**: Visual progress tracking

### Navigation
- **Dashboard Central**: Quick access to all features
- **Breadcrumb Trail**: Easy back navigation
- **Consistent Layout**: Familiar patterns

### Responsiveness
- **Fixed Window**: 1200×800 optimal size
- **Scroll Support**: ScrollPane for long content
- **Table Pagination**: Efficient data display

### Localization
- **Thai Language**: Full Thai interface
- **Date Formats**: DD/MM/YYYY
- **Number Formats**: Locale-aware

---

## 🔒 Security Features

### Password Security
- **BCrypt Hashing**: Industry-standard encryption
- **Salt Generation**: Unique per password
- **No Plain-text**: Never stored unencrypted

### Data Validation
- **Input Sanitization**: Prevent SQL injection
- **Type Checking**: Proper data types
- **Range Validation**: Min/max values

### Session Management
- **Singleton Pattern**: Single user session
- **Logout Support**: Clear session data
- **Timeout**: Auto-logout on close

---

## 📱 Additional Features

### Admin Panel (Optional)
- **User Management**: View all users
- **Food Management**: Add/edit/delete foods
- **Exercise Management**: Add/edit/delete exercises
- **Admin Role**: Special permissions

### Data Export (Future)
- CSV export for reports
- PDF generation
- Backup/restore functionality

### Reminders (Future)
- Daily logging reminders
- Goal achievement notifications
- Weekly summary emails

---

**For more information, see the [README](../README.md) or [Installation Guide](INSTALLATION.md).**
