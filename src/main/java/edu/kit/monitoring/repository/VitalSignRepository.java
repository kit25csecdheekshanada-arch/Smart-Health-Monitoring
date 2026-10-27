package edu.kit.monitoring.repository;

import edu.kit.monitoring.entity.Patient;
import edu.kit.monitoring.entity.VitalSign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VitalSignRepository extends JpaRepository<VitalSign, Long> {
    List<VitalSign> findByPatientOrderByRecordedAtDesc(Patient patient);
    Page<VitalSign> findByPatientOrderByRecordedAtDesc(Patient patient, Pageable pageable);
    List<VitalSign> findTop100ByOrderByRecordedAtDesc();
    List<VitalSign> findTop10ByOrderByRecordedAtDesc();
    Optional<VitalSign> findFirstByPatientOrderByRecordedAtDesc(Patient patient);
    List<VitalSign> findTop20ByPatientOrderByRecordedAtDesc(Patient patient);

    List<VitalSign> findByPatientAndRecordedAtBetweenOrderByRecordedAtAsc(
            Patient patient, LocalDateTime start, LocalDateTime end);

    List<VitalSign> findByRecordedAtBetweenOrderByRecordedAtDesc(
            LocalDateTime start, LocalDateTime end);

    long countByStatus(String status);
    long countByStatusNot(String status);

    @Query("SELECT COUNT(DISTINCT v.patient.id) FROM VitalSign v WHERE v.status = 'WARNING' OR v.status = 'CRITICAL'")
    long countPatientsWithAbnormalReadings();
}
