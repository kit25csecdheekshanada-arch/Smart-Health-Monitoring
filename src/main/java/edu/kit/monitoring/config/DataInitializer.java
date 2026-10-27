package edu.kit.monitoring.config;

import edu.kit.monitoring.entity.MonitoringSetting;
import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.User;
import edu.kit.monitoring.entity.VitalSign;
import edu.kit.monitoring.repository.MonitoringSettingRepository;
import edu.kit.monitoring.repository.PatientRepository;
import edu.kit.monitoring.repository.UserRepository;
import edu.kit.monitoring.repository.VitalSignRepository;
import edu.kit.monitoring.service.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final VitalSignRepository vitalSignRepository;
    private final MonitoringSettingRepository settingRepository;
    private final AlertService alertService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PatientRepository patientRepository,
                           VitalSignRepository vitalSignRepository,
                           MonitoringSettingRepository settingRepository,
                           AlertService alertService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.vitalSignRepository = vitalSignRepository;
        this.settingRepository = settingRepository;
        this.alertService = alertService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initUsers();
        initSettings();
        initPatientsAndVitals();
        log.info("Smart Patient Health Monitoring System data initialization completed successfully.");
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            log.info("Seeding default application users...");
            // Admin user
            User admin = new User(
                    "admin",
                    passwordEncoder.encode("Admin@123"),
                    "Hospital Administrator",
                    "admin@smarthealth.hospital",
                    "ROLE_ADMIN",
                    "IT & Operations"
            );
            userRepository.save(admin);

            // Doctor user
            User doctor = new User(
                    "doctor",
                    passwordEncoder.encode("Doctor@123"),
                    "Dr. Sarah Johnson, MD",
                    "dr.johnson@smarthealth.hospital",
                    "ROLE_DOCTOR",
                    "Cardiology & Critical Care"
            );
            userRepository.save(doctor);

            // Nurse user
            User nurse = new User(
                    "nurse",
                    passwordEncoder.encode("Nurse@123"),
                    "Nurse Emily Clark, RN",
                    "nurse.clark@smarthealth.hospital",
                    "ROLE_NURSE",
                    "Intensive Care Unit (ICU)"
            );
            userRepository.save(nurse);
            log.info("Default users created: admin/Admin@123, doctor/Doctor@123, nurse/Nurse@123");
        }
    }

    private void initSettings() {
        if (settingRepository.count() == 0) {
            MonitoringSetting setting = new MonitoringSetting();
            setting.setHrMin(60.0);
            setting.setHrMax(100.0);
            setting.setSpo2Min(95.0);
            setting.setTempMin(36.0);
            setting.setTempMax(37.5);
            setting.setSysBpMin(90.0);
            setting.setSysBpMax(120.0);
            setting.setDiaBpMin(60.0);
            setting.setDiaBpMax(80.0);
            setting.setRespRateMin(12.0);
            setting.setRespRateMax(20.0);
            setting.setAutoRefreshInterval(10);
            setting.setSoundAlertEnabled(true);
            settingRepository.save(setting);
            log.info("Default monitoring settings initialized.");
        }
    }

    private void initPatientsAndVitals() {
        if (patientRepository.count() == 0) {
            log.info("Seeding initial patient records and vital signs...");

            // Patient 1: ICU
            Patient p1 = new Patient("P-1001", "John", "Doe", 54, "Male", "O+", "+1-555-0101", "ICU-Bed-01", "Dr. Sarah Johnson");
            p1.setEmergencyContact("+1-555-0102 (Spouse)");
            p1.setMedicalHistory("Hypertension diagnosed 2021; Post-cardiac observation.");
            p1.setNotes("Continuous ECG & SpO2 monitoring required. Check every 2 hours.");
            p1 = patientRepository.save(p1);

            // Patient 2: Critical Care
            Patient p2 = new Patient("P-1002", "Eleanor", "Vance", 68, "Female", "A-", "+1-555-0201", "ICU-Bed-03", "Dr. Sarah Johnson");
            p2.setEmergencyContact("+1-555-0202 (Son)");
            p2.setMedicalHistory("Mild COPD, Type 2 Diabetes; Oxygen therapy titration.");
            p2.setNotes("Maintain SpO2 above 92%. Monitor respiratory rate for tachypnea.");
            p2 = patientRepository.save(p2);

            // Patient 3: Ward
            Patient p3 = new Patient("P-1003", "Marcus", "Chen", 32, "Male", "B+", "+1-555-0301", "Ward-204", "Dr. Robert Davis");
            p3.setEmergencyContact("+1-555-0302 (Parent)");
            p3.setMedicalHistory("Post-appendectomy Day 2 recovery.");
            p3.setNotes("Vitals stable. Oral analgesics prescribed.");
            p3 = patientRepository.save(p3);

            // Patient 4: Post-Op
            Patient p4 = new Patient("P-1004", "Sophia", "Martinez", 45, "Female", "AB+", "+1-555-0401", "Ward-105", "Dr. Sarah Johnson");
            p4.setEmergencyContact("+1-555-0402 (Partner)");
            p4.setMedicalHistory("Orthopedic surgery recovery. No prior chronic conditions.");
            p4.setNotes("Scheduled for discharge review tomorrow.");
            p4 = patientRepository.save(p4);

            LocalDateTime now = LocalDateTime.now();

            // Seed historical readings for P-1001 over last few hours
            createVital(p1, 74.0, 36.6, 98.2, 118.0, 78.0, 16.0, "NORMAL", null, false, now.minusMinutes(50));
            createVital(p1, 76.0, 36.7, 98.0, 120.0, 80.0, 16.0, "NORMAL", null, false, now.minusMinutes(40));
            createVital(p1, 82.0, 36.8, 97.5, 124.0, 82.0, 17.0, "NORMAL", null, false, now.minusMinutes(30));
            createVital(p1, 88.0, 37.0, 96.8, 128.0, 84.0, 18.0, "NORMAL", null, false, now.minusMinutes(20));
            createVital(p1, 78.0, 36.8, 98.0, 120.0, 80.0, 16.0, "NORMAL", null, false, now.minusMinutes(10));
            createVital(p1, 75.0, 36.7, 98.5, 119.0, 79.0, 15.0, "NORMAL", null, false, now.minusMinutes(1));

            // Seed abnormal reading for P-1002 to demonstrate Alert System
            createVital(p2, 85.0, 37.2, 94.0, 132.0, 86.0, 22.0, "WARNING",
                    "SpO2 (94.0%) dropped below threshold (95.0%); Respiratory rate (22 bpm) elevated", false, now.minusMinutes(45));
            createVital(p2, 92.0, 37.4, 91.5, 138.0, 90.0, 24.0, "WARNING",
                    "SpO2 (91.5%) dropped below threshold (95.0%); Blood Pressure (138/90) elevated", false, now.minusMinutes(25));
            VitalSign critVital = createVital(p2, 108.0, 37.8, 89.2, 148.0, 95.0, 26.0, "CRITICAL",
                    "Heart rate (108 bpm) outside normal threshold; SpO2 (89.2%) critical hypoxemia; High Blood Pressure (148/95)", false, now.minusMinutes(5));

            // Generate alerts for P-1002
            alertService.createAlert(p2, critVital, "CRITICAL", "SpO2", 89.2, ">= 95.0%",
                    "SpO2 (89.2%) dropped below critical threshold. Immediate clinician review advised.");
            alertService.createAlert(p2, critVital, "WARNING", "Heart Rate", 108.0, "60-100 bpm",
                    "Heart rate (108 bpm) elevated above upper limit (100 bpm).");

            // Seed normal readings for P-1003 & P-1004
            createVital(p3, 70.0, 36.5, 99.0, 115.0, 75.0, 14.0, "NORMAL", null, false, now.minusMinutes(35));
            createVital(p3, 72.0, 36.6, 98.8, 116.0, 76.0, 15.0, "NORMAL", null, false, now.minusMinutes(5));

            createVital(p4, 68.0, 36.4, 98.5, 112.0, 74.0, 15.0, "NORMAL", null, false, now.minusMinutes(15));
        }
    }

    private VitalSign createVital(Patient patient, double hr, double temp, double spo2,
                                  double sys, double dia, double rr, String status,
                                  String alertMsg, boolean simulated, LocalDateTime time) {
        VitalSign v = new VitalSign(patient, hr, temp, spo2, sys, dia, rr, status, alertMsg, simulated);
        v.setRecordedAt(time);
        return vitalSignRepository.save(v);
    }
}
