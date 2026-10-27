package edu.kit.monitoring.repository;

import edu.kit.monitoring.entity.Alert;
import edu.kit.monitoring.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByAcknowledgedFalseOrderByCreatedAtDesc();
    List<Alert> findTop50ByOrderByCreatedAtDesc();
    List<Alert> findTop10ByOrderByCreatedAtDesc();
    List<Alert> findByPatientOrderByCreatedAtDesc(Patient patient);
    List<Alert> findTop10ByPatientOrderByCreatedAtDesc(Patient patient);
    long countByAcknowledgedFalse();
}
