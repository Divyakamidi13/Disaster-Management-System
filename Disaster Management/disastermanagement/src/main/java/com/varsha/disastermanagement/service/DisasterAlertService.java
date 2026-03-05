package com.varsha.disastermanagement.service;

import com.varsha.disastermanagement.entity.DisasterAlert;
import com.varsha.disastermanagement.enums.AlertLevel;
import com.varsha.disastermanagement.enums.DisasterType;
import com.varsha.disastermanagement.repository.DisasterAlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DisasterAlertService {

    @Autowired
    private DisasterAlertRepository disasterAlertRepository;

    public DisasterAlert createAlert(DisasterAlert alert) {
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());
        alert.setIsActive(true);
        return disasterAlertRepository.save(alert);
    }

    public List<DisasterAlert> getAllActiveAlerts() {
        return disasterAlertRepository.findByIsActiveOrderByCreatedAtDesc(true);
    }

    public List<DisasterAlert> getAllAlerts() {
        return disasterAlertRepository.findAll();
    }

    public Optional<DisasterAlert> getAlertById(Long id) {
        return disasterAlertRepository.findById(id);
    }

    public DisasterAlert updateAlert(Long id, DisasterAlert alertDetails) {
        DisasterAlert alert = disasterAlertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        alert.setTitle(alertDetails.getTitle());
        alert.setDescription(alertDetails.getDescription());
        alert.setDisasterType(alertDetails.getDisasterType());
        alert.setAlertLevel(alertDetails.getAlertLevel());
        alert.setLocation(alertDetails.getLocation());
        alert.setLatitude(alertDetails.getLatitude());
        alert.setLongitude(alertDetails.getLongitude());
        alert.setIsActive(alertDetails.getIsActive());
        alert.setUpdatedAt(LocalDateTime.now());

        return disasterAlertRepository.save(alert);
    }

    public void deleteAlert(Long id) {
        DisasterAlert alert = disasterAlertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));
        alert.setIsActive(false);
        disasterAlertRepository.save(alert);
    }

    public List<DisasterAlert> getAlertsByType(DisasterType disasterType) {
        return disasterAlertRepository.findByDisasterTypeOrderByCreatedAtDesc(disasterType);
    }

    public List<DisasterAlert> getAlertsByLevel(AlertLevel alertLevel) {
        return disasterAlertRepository.findByAlertLevelOrderByCreatedAtDesc(alertLevel);
    }

    public List<DisasterAlert> getAlertsByLocation(String location) {
        return disasterAlertRepository.findByLocationContainingIgnoreCaseOrderByCreatedAtDesc(location);
    }

    public List<DisasterAlert> getRecentAlerts(int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return disasterAlertRepository.findRecentAlerts(since);
    }

    public List<DisasterAlert> getCriticalAlerts() {
        return disasterAlertRepository.findActiveAlertsByLevel(AlertLevel.CRITICAL);
    }
}
