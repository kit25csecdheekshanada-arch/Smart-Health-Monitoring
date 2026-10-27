package edu.kit.monitoring.dto;

import java.util.List;

public class DashboardSummaryDto {
    private long totalPatients;
    private long monitoredPatients;
    private long abnormalPatients;
    private long activeAlerts;
    private long totalReadings;
    private HealthReading latestReading;
    private List<HealthReading> recentReadings;

    public DashboardSummaryDto() {}

    public long getTotalPatients() { return totalPatients; }
    public void setTotalPatients(long totalPatients) { this.totalPatients = totalPatients; }

    public long getMonitoredPatients() { return monitoredPatients; }
    public void setMonitoredPatients(long monitoredPatients) { this.monitoredPatients = monitoredPatients; }

    public long getAbnormalPatients() { return abnormalPatients; }
    public void setAbnormalPatients(long abnormalPatients) { this.abnormalPatients = abnormalPatients; }

    public long getActiveAlerts() { return activeAlerts; }
    public void setActiveAlerts(long activeAlerts) { this.activeAlerts = activeAlerts; }

    public long getTotalReadings() { return totalReadings; }
    public void setTotalReadings(long totalReadings) { this.totalReadings = totalReadings; }

    public HealthReading getLatestReading() { return latestReading; }
    public void setLatestReading(HealthReading latestReading) { this.latestReading = latestReading; }

    public List<HealthReading> getRecentReadings() { return recentReadings; }
    public void setRecentReadings(List<HealthReading> recentReadings) { this.recentReadings = recentReadings; }
}
