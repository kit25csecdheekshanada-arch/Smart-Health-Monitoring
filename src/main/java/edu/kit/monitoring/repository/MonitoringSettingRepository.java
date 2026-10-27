package edu.kit.monitoring.repository;

import edu.kit.monitoring.entity.MonitoringSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringSettingRepository extends JpaRepository<MonitoringSetting, Long> {
}
