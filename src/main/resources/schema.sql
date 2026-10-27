-- ===================================================================
-- Database: smart_patient_monitoring
-- MySQL Relational Schema for Smart Patient Health Monitoring System
-- ===================================================================

CREATE DATABASE IF NOT EXISTS smart_patient_monitoring;
USE smart_patient_monitoring;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(30) NOT NULL DEFAULT 'ROLE_STAFF',
    department VARCHAR(100),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Patients Table
CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_code VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(15) NOT NULL,
    blood_group VARCHAR(10),
    contact_number VARCHAR(25),
    emergency_contact VARCHAR(25),
    room_bed_number VARCHAR(30),
    assigned_doctor VARCHAR(100),
    monitoring_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    medical_history TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_patient_code (patient_code),
    INDEX idx_monitoring_status (monitoring_status)
);

-- 3. Monitoring Settings Table
CREATE TABLE IF NOT EXISTS monitoring_settings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    hr_min DOUBLE NOT NULL DEFAULT 60.0,
    hr_max DOUBLE NOT NULL DEFAULT 100.0,
    spo2_min DOUBLE NOT NULL DEFAULT 95.0,
    temp_min DOUBLE NOT NULL DEFAULT 36.0,
    temp_max DOUBLE NOT NULL DEFAULT 37.5,
    sys_bp_min DOUBLE NOT NULL DEFAULT 90.0,
    sys_bp_max DOUBLE NOT NULL DEFAULT 120.0,
    dia_bp_min DOUBLE NOT NULL DEFAULT 60.0,
    dia_bp_max DOUBLE NOT NULL DEFAULT 80.0,
    resp_rate_min DOUBLE NOT NULL DEFAULT 12.0,
    resp_rate_max DOUBLE NOT NULL DEFAULT 20.0,
    auto_refresh_interval INT NOT NULL DEFAULT 10,
    sound_alert_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 4. Vital Signs Table
CREATE TABLE IF NOT EXISTS vital_signs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    heart_rate DOUBLE NOT NULL,
    temperature DOUBLE NOT NULL,
    spo2 DOUBLE NOT NULL,
    systolic_bp DOUBLE,
    diastolic_bp DOUBLE,
    respiratory_rate DOUBLE,
    status VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    alert_message VARCHAR(500),
    is_simulated BOOLEAN NOT NULL DEFAULT FALSE,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_vital_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    INDEX idx_vital_patient_time (patient_id, recorded_at DESC),
    INDEX idx_vital_status (status)
);

-- 5. Alerts Table
CREATE TABLE IF NOT EXISTS alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    vital_sign_id BIGINT,
    alert_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL DEFAULT 'WARNING',
    parameter_name VARCHAR(50) NOT NULL,
    measured_value DOUBLE NOT NULL,
    threshold_value VARCHAR(100) NOT NULL,
    message VARCHAR(500) NOT NULL,
    acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledged_by VARCHAR(50),
    acknowledged_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_alert_patient FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE,
    CONSTRAINT fk_alert_vital FOREIGN KEY (vital_sign_id) REFERENCES vital_signs(id) ON DELETE SET NULL,
    INDEX idx_alert_patient (patient_id),
    INDEX idx_alert_status (acknowledged, severity)
);
