package edu.kit.monitoring.service;

import edu.kit.monitoring.dto.ThresholdSettingsDto;
import edu.kit.monitoring.entity.MonitoringSetting;
import edu.kit.monitoring.repository.MonitoringSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsService {

    private final MonitoringSettingRepository settingRepository;

    public SettingsService(MonitoringSettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public MonitoringSetting getSettings() {
        return settingRepository.findAll().stream().findFirst().orElseGet(() -> {
            MonitoringSetting defaultSetting = new MonitoringSetting();
            return settingRepository.save(defaultSetting);
        });
    }

    public ThresholdSettingsDto getSettingsDto() {
        MonitoringSetting s = getSettings();
        ThresholdSettingsDto dto = new ThresholdSettingsDto();
        dto.setId(s.getId());
        dto.setHrMin(s.getHrMin());
        dto.setHrMax(s.getHrMax());
        dto.setSpo2Min(s.getSpo2Min());
        dto.setTempMin(s.getTempMin());
        dto.setTempMax(s.getTempMax());
        dto.setSysBpMin(s.getSysBpMin());
        dto.setSysBpMax(s.getSysBpMax());
        dto.setDiaBpMin(s.getDiaBpMin());
        dto.setDiaBpMax(s.getDiaBpMax());
        dto.setRespRateMin(s.getRespRateMin());
        dto.setRespRateMax(s.getRespRateMax());
        dto.setAutoRefreshInterval(s.getAutoRefreshInterval());
        dto.setSoundAlertEnabled(s.isSoundAlertEnabled());
        return dto;
    }

    @Transactional
    public MonitoringSetting updateSettings(ThresholdSettingsDto dto) {
        MonitoringSetting s = getSettings();
        s.setHrMin(dto.getHrMin());
        s.setHrMax(dto.getHrMax());
        s.setSpo2Min(dto.getSpo2Min());
        s.setTempMin(dto.getTempMin());
        s.setTempMax(dto.getTempMax());
        s.setSysBpMin(dto.getSysBpMin());
        s.setSysBpMax(dto.getSysBpMax());
        s.setDiaBpMin(dto.getDiaBpMin());
        s.setDiaBpMax(dto.getDiaBpMax());
        s.setRespRateMin(dto.getRespRateMin());
        s.setRespRateMax(dto.getRespRateMax());
        if (dto.getAutoRefreshInterval() != null) {
            s.setAutoRefreshInterval(dto.getAutoRefreshInterval());
        }
        s.setSoundAlertEnabled(dto.isSoundAlertEnabled());
        return settingRepository.save(s);
    }
}
