# Installation Guide - LIFE CAL System

## System Requirements

### Minimum Requirements
- **OS**: Windows 10/11, macOS 10.14+, or Linux
- **RAM**: 4 GB
- **Storage**: 500 MB free space
- **Display**: 1280×720 resolution

### Software Requirements
- **Java Development Kit (JDK)**: 17 or higher
- **Maven**: 3.9 or higher

---

## Installation Steps

### 1. Install Java JDK 17

#### Windows
1. Download JDK 17 from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://adoptium.net/)
2. Run the installer
3. Set `JAVA_HOME` environment variable:
   ```
   JAVA_HOME=C:\Program Files\Java\jdk-17
   ```
4. Add to PATH:
   ```
   %JAVA_HOME%\bin
   ```
5. Verify installation:
   ```cmd
   java -version
   ```

#### macOS
```bash
# Using Homebrew
brew install openjdk@17

# Verify
java -version
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install openjdk-17-jdk

# Verify
java -version
```

---

### 2. Install Maven

#### Windows
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to `C:\Program Files\apache-maven-3.9.x`
3. Set `MAVEN_HOME` environment variable
4. Add to PATH: `%MAVEN_HOME%\bin`
5. Verify:
   ```cmd
   mvn -version
   ```

#### macOS
```bash
brew install maven
mvn -version
```

#### Linux
```bash
sudo apt install maven
mvn -version
```

---

### 3. Clone the Repository

```bash
git clone https://github.com/yourusername/lifecal-system.git
cd lifecal-system
```

Or download ZIP and extract:
```
https://github.com/yourusername/lifecal-system/archive/refs/heads/main.zip
```

---

### 4. Build the Project

```bash
# Clean and build
mvn clean install

# Skip tests (faster)
mvn clean install -DskipTests
```

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

---

### 5. Run the Application

```bash
mvn javafx:run
```

The application window should open automatically.

---

## Troubleshooting

### Issue: "JAVA_HOME is not set"
**Solution**: Set the JAVA_HOME environment variable to your JDK installation directory.

### Issue: "mvn: command not found"
**Solution**: Ensure Maven is installed and added to PATH.

### Issue: "JavaFX runtime components are missing"
**Solution**: Use `mvn javafx:run` instead of `java -jar` command.

### Issue: Database connection error
**Solution**: Ensure the `database/` directory exists and is writable.

### Issue: Application won't start
**Solution**: 
1. Check Java version: `java -version` (must be 17+)
2. Check Maven: `mvn -version`
3. Rebuild: `mvn clean install`
4. Check logs in console

---

## Development Setup (Optional)

### IntelliJ IDEA
1. Open IntelliJ IDEA
2. File → Open → Select `lifecal-system` folder
3. Wait for Maven import
4. Run → Edit Configurations
5. Add new "Maven" configuration
6. Command line: `javafx:run`
7. Click Run

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Browse to `lifecal-system`
3. Right-click project → Run As → Maven Build
4. Goals: `javafx:run`
5. Run

### VS Code
1. Install "Extension Pack for Java"
2. Open folder `lifecal-system`
3. Open integrated terminal
4. Run: `mvn javafx:run`

---

## Database Setup

The application automatically creates the SQLite database on first run:
```
database/lifecal.db
```

No manual setup required!

---

## First Run

1. Start the application
2. Click "สร้างบัญชี" (Register)
3. Fill in your information
4. Click "ลงทะเบียน" (Register)
5. Login with your credentials
6. Start tracking your health!

---

## Updating

To update to the latest version:

```bash
git pull origin main
mvn clean install
mvn javafx:run
```

---

## Uninstalling

1. Delete the `lifecal-system` folder
2. Delete the database: `database/lifecal.db`
3. (Optional) Uninstall Java and Maven

---

## Support

If you encounter any issues:
1. Check the [Troubleshooting](#troubleshooting) section
2. Open an issue on GitHub
3. Contact support: support@lifecal.example.com

---

**Happy tracking! 🏃‍♀️💪**
