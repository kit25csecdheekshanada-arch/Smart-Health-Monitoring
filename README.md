# Smart Patient Health Monitoring System — Java Full-Stack Web Application

A modern, responsive, and secure healthcare monitoring web application built with **Java 21, Spring Boot 3, Spring Data JPA, Hibernate, MySQL, Thymeleaf, Bootstrap 5, and Chart.js**. Designed for clinical demonstrations, hospital ward telemetries, and final-year / mini-project defense.

---

## 1. Key Features

- **Clinical Command Dashboard:**
  - Real-time physiological telemetry for 5 parameters:
    1. **Heart Rate (BPM)**
    2. **Oxygen Saturation (SpO₂ %)**
    3. **Body Temperature (°C)**
    4. **Blood Pressure (Systolic & Diastolic mmHg)**
    5. **Respiratory Rate (breaths/min)**
  - Summary cards (Total Patients, Active Telemetries, Abnormal Patients, Active Alerts)
  - Interactive vital-sign trend line charts using **Chart.js**
  - Live clock and automated telemetry polling
- **Patient Management (Full CRUD):**
  - Register new patients with Medical Record Number (MRN / Patient Code)
  - Bed / ward allocation and assigned physician assignment
  - Search by name, code, or ward location; filter by monitoring status
  - Edit demographics, clinical notes, and allergy history
  - Safe patient deletion with confirmation
- **Patient Profile & Telemetry History:**
  - Complete vital telemetry timeline with pagination
  - Parameter trend charts specific to individual patients
  - Manual vital sign recording modal for bedside nursing staff
- **Automated Algorithmic Alert System:**
  - Multi-tier alert classification (`NORMAL`, `WARNING`, `CRITICAL`)
  - Detects tachycardia/bradycardia, hypoxemia, fever, hypo/hypertension, tachypnea
  - Unread alert badge counter in header and sidebar
  - Clinician alert acknowledgement workflow with audit tracking
- **Medical Reports & Analytics:**
  - Date-range filtering and patient-specific history views
  - Statistical aggregate calculations (Minimum, Mean, Maximum)
  - Clean printable view (`window.print()`)
  - One-click CSV dataset export (`/reports/export-csv`)
- **Configurable Clinical Limits & Settings:**
  - Custom reference thresholds for all 5 parameters
  - Telemetry auto-refresh interval configuration
  - Audible alert toggle
- **IoT & Sensor Ingestion REST API:**
  - Dedicated endpoint `POST /api/v1/vitals` for **ESP32**, MAX30102, DS18B20, or Raspberry Pi
  - Preserved backward-compatible endpoints (`/api/readings`, `/api/summary`, `/api/demo-reading`)
  - Demonstration mode with simulated telemetries clearly labeled
- **Enterprise Security (Spring Security 6):**
  - BCrypt password hashing
  - Role-based authorization (`ROLE_ADMIN`, `ROLE_DOCTOR`, `ROLE_NURSE`)
  - CSRF protection for browser forms; API endpoints permit microcontroller ingestion

---

## 2. Technology Stack

| Layer | Technologies |
|---|---|
| **Backend Framework** | Java 21, Spring Boot 3.4.2 |
| **Persistence / ORM** | Spring Data JPA, Hibernate 6 |
| **Security** | Spring Security 6, BCrypt Password Encoder |
| **Databases** | MySQL 8.0+ (Production) / H2 Embedded (Development Fallback) |
| **Template Engine** | Thymeleaf, Thymeleaf Extras Spring Security 6 |
| **Frontend Framework** | Bootstrap 5.3.3, Bootstrap Icons 1.11.3 |
| **Data Visualizations**| Chart.js 4.4.2 |
| **Report Generation** | Apache Commons CSV |
| **Build Tool** | Apache Maven 3.8+ (Maven Wrapper included) |

---

## 3. Project Structure

```
SmartPatientHealthMonitoring/
├── .mvn/wrapper/                  # Maven Wrapper binaries & properties
├── mvnw, mvnw.cmd                 # Maven execution scripts
├── pom.xml                        # Maven dependencies & build configuration
├── run.bat                        # One-click Windows runner
├── README.md                      # Project documentation & execution guide
└── src/
    ├── main/
    │   ├── java/edu/kit/monitoring/
    │   │   ├── SmartPatientMonitoringApplication.java
    │   │   ├── HealthReading.java           # Compatibility record
    │   │   ├── ReadingRequest.java          # Compatibility record
    │   │   ├── HealthController.java        # Compatibility REST API
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java      # Spring Security configuration
    │   │   │   └── DataInitializer.java     # Pre-seeds demo users, patients, vitals
    │   │   ├── controller/
    │   │   │   ├── PageController.java      # Dashboard & navigation views
    │   │   │   ├── VitalsApiController.java # IoT ingestion REST API (/api/v1/vitals)
    │   │   │   ├── PatientWebController.java# Patient form CRUD actions
    │   │   │   ├── ReportController.java    # Report filtering & CSV export
    │   │   │   └── AuthController.java      # Registration & settings actions
    │   │   ├── dto/                         # Data transfer objects & validation
    │   │   ├── entity/                      # JPA Entities (User, Patient, VitalSign, Alert, Setting)
    │   │   ├── exception/                   # Global exception handling
    │   │   ├── repository/                  # Spring Data JPA Repositories
    │   │   └── service/                     # Business logic, alerts, reports
    │   └── resources/
    │       ├── application.properties       # Core configuration
    │       ├── application-dev.properties   # Embedded persistent database
    │       ├── application-mysql.properties # MySQL configuration
    │       ├── schema.sql                   # MySQL relational DDL script
    │       ├── static/                      # Custom CSS & JavaScript
    │       └── templates/                   # Thymeleaf HTML5 pages
    └── test/                                # JUnit 5 & MockMvc integration tests
```

---

## 4. Default Demonstration Accounts

The system automatically initializes 3 realistic user accounts upon startup:

| Role | Username | Password | Access Level |
|---|---|---|---|
| **Administrator** | `admin` | `Admin@123` | Full access: Settings, Thresholds, Reports |
| **Doctor / Physician** | `doctor` | `Doctor@123` | Patient profiles, Telemetry, Diagnosis, Reports |
| **ICU Nurse / Staff** | `nurse` | `Nurse@123` | Vitals monitoring, Alert acknowledgement |

*(Note: On the login screen, convenient one-click "Demo Account" buttons are provided to instantly populate credentials during evaluations).*

---

## 5. How to Run the Application

### Option A: Quick Run with Maven Wrapper (Recommended)

1. Open a terminal / command prompt in this directory:
   ```bash
   cd SmartPatientHealthMonitoring
   ```

2. Run using Maven Wrapper:
   - **Windows:**
     ```cmd
     run.bat
     ```
     *(or `.\mvnw.cmd spring-boot:run`)*
   - **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```

3. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```
4. Sign in using `doctor` / `Doctor@123` or `admin` / `Admin@123`.

---

### Option B: Running with MySQL Database

1. Ensure MySQL server is running (e.g. through MySQL Community Server or XAMPP).
2. Create the database:
   ```sql
   CREATE DATABASE smart_patient_monitoring;
   ```
   *(Optional: You can execute `src/main/resources/schema.sql` to inspect or create the schema manually, though Spring Boot JPA will automatically create and update tables).*
3. Run the application with the `mysql` profile:
   ```cmd
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
   ```
   *(To customize database username or password, provide environment variables `DB_USER=root` and `DB_PASSWORD=your_password`).*

---

## 6. IoT Microcontroller Integration (ESP32 / Arduino)

The application provides a high-performance REST endpoint for edge IoT devices such as an **ESP32** equipped with a **MAX30102** (Pulse Oximeter) and **DS18B20** (Temperature Sensor).

### REST Ingestion Endpoint:
- **URL:** `POST http://<SERVER_IP>:8080/api/v1/vitals`
- **Content-Type:** `application/json`

### Sample JSON Payload:
```json
{
  "patientId": 1,
  "heartRate": 78.5,
  "temperature": 36.8,
  "spo2": 98.2,
  "systolicBP": 120,
  "diastolicBP": 80,
  "respiratoryRate": 16,
  "isSimulated": false
}
```

### Complete ESP32 Arduino C++ Code Example:
```cpp
#include <WiFi.h>
#include <HTTPClient.h>

const char* ssid = "YOUR_WIFI_SSID";
const char* password = "YOUR_WIFI_PASSWORD";
const char* serverUrl = "http://192.168.1.100:8080/api/v1/vitals"; // Replace with your computer's IP

void setup() {
  Serial.begin(115200);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nWiFi Connected! IP address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(serverUrl);
    http.addHeader("Content-Type", "application/json");

    // Read sensors (or format sample sensor values)
    float heartRate = 76.0;
    float spo2 = 98.4;
    float temperature = 36.7;
    float systolicBP = 118.0;
    float diastolicBP = 78.0;
    float respiratoryRate = 16.0;

    String jsonPayload = String("{\"patientId\":1,") +
                         "\"heartRate\":" + String(heartRate) + "," +
                         "\"spo2\":" + String(spo2) + "," +
                         "\"temperature\":" + String(temperature) + "," +
                         "\"systolicBP\":" + String(systolicBP) + "," +
                         "\"diastolicBP\":" + String(diastolicBP) + "," +
                         "\"respiratoryRate\":" + String(respiratoryRate) + "," +
                         "\"isSimulated\":false}";

    int httpResponseCode = http.POST(jsonPayload);
    Serial.print("HTTP Response code: ");
    Serial.println(httpResponseCode);
    http.end();
  }
  delay(10000); // Send reading every 10 seconds
}
```

---

## 7. Testing & Verification

Comprehensive JUnit 5 and Spring Boot integration tests are included:

```cmd
.\mvnw.cmd test
```

Tests verify:
- Authentication and security access control
- Patient registration, validation, and search
- Vital sign saving and automated threshold alert generation
- Alert acknowledgement workflow
- IoT REST API ingestion and input validation
- CSV dataset generation and statistical summaries

---

## 8. Beginner Step-by-Step Execution Guide & Troubleshooting

### 1. Prerequisites (JDK & MySQL)
- **Java 21:** Microsoft OpenJDK 21 or Eclipse Adoptium Temurin 21.
- **Maven:** Built-in via `./mvnw` / `mvnw.cmd` wrapper (no external Maven installation required).
- **MySQL 8.0+ (Optional):** Required only if running with the `mysql` profile. In default `dev` profile, a persistent embedded H2 database is used automatically.

### 2. Opening in your IDE
- **IntelliJ IDEA:** File -> Open -> Select `SmartPatientHealthMonitoring` folder -> Open as Maven Project.
- **VS Code:** File -> Open Folder -> Select `SmartPatientHealthMonitoring`.
- **Eclipse:** File -> Import -> Existing Maven Projects -> Select root directory.

### 3. Setting Up the Database
- **For H2 (Default):** No configuration needed! Spring Boot automatically creates `./data/patientdb.mv.db` upon launch.
- **For MySQL:**
  1. Start MySQL Server / XAMPP.
  2. Open MySQL Workbench or CLI and run:
     ```sql
     CREATE DATABASE smart_patient_monitoring;
     ```
  3. (Optional) Run `src/main/resources/schema.sql` if you prefer manual DDL execution.

### 4. Configuring Database Credentials
Set environment variables or pass properties via command line:
```cmd
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=smart_patient_monitoring
set DB_USER=root
set DB_PASSWORD=your_password
```
Or edit `src/main/resources/application-mysql.properties`.

### 5. Running the Application
- **Windows:** Double-click `run.bat` or run:
  ```cmd
  .\mvnw.cmd spring-boot:run
  ```
- **Linux/macOS:**
  ```bash
  ./mvnw spring-boot:run
  ```
- **Running with MySQL Profile:**
  ```cmd
  .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
  ```

### 6. Accessing the Web Portal
Open your web browser and navigate to:
```
http://localhost:8080
```

### 7. Logging In
Use any of the pre-seeded clinical accounts (or click the quick-fill buttons on the login screen):
- **Doctor:** `doctor` / `Doctor@123`
- **Administrator:** `admin` / `Admin@123`
- **ICU Nurse:** `nurse` / `Nurse@123`

### 8. Testing Key Workflows
1. **Clinical Command Dashboard:** View 4 summary cards, vital sign trends in Chart.js, recent readings, and active alerts.
2. **5-Parameter Telemetry:** Visit `/monitoring` to observe real-time heart rate, SpO₂, temperature, BP, and respiratory rate.
3. **Patient Management:** Go to `/patients` -> Register a new patient -> Search by name/code -> Edit details.
4. **Trigger Simulated IoT Reading:** Click the **"Simulate IoT Reading"** button in the header or dashboard.
5. **Acknowledge Alerts:** Visit `/alerts` -> Click **"Acknowledge"** on any active warning or critical alert.
6. **Generate Reports:** Visit `/reports/view` -> Select date range and patient -> View aggregate statistics -> Click **"Export to CSV"** or **"Print Report"**.

### 9. Submitting Test IoT Sensor Readings via cURL
Run in another terminal:
```bash
curl -X POST http://localhost:8080/api/v1/vitals \
  -H "Content-Type: application/json" \
  -d "{\"patientId\":1,\"heartRate\":140,\"temperature\":39.2,\"spo2\":89.0,\"systolicBP\":165,\"diastolicBP\":105,\"respiratoryRate\":28,\"isSimulated\":false}"
```

### 10. Troubleshooting Common Issues
- **Port 8080 already in use:**
  - Find process: `netstat -ano | findstr :8080`
  - Or change port in `application.properties`: `server.port=8081`
- **Access Denied for MySQL user 'root':**
  - Verify credentials with `mysql -u root -p`. Provide password via `set DB_PASSWORD=...`.
- **H2 file lock error:**
  - Ensure only one instance of the application is running at a time, or delete `./data/patientdb.mv.db` to reset state.
