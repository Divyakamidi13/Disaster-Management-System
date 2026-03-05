package com.varsha.disastermanagement.controller;

import com.varsha.disastermanagement.entity.DisasterAlert;
import com.varsha.disastermanagement.enums.AlertLevel;
import com.varsha.disastermanagement.enums.DisasterType;
import com.varsha.disastermanagement.service.DisasterAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DisasterAlertController {

    @Autowired
    private DisasterAlertService disasterAlertService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONDER', 'CLIENT')")
    public ResponseEntity<List<DisasterAlert>> getAllActiveAlerts() {
        List<DisasterAlert> alerts = disasterAlertService.getAllActiveAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DisasterAlert>> getAllAlerts() {
        List<DisasterAlert> alerts = disasterAlertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONDER', 'CLIENT')")
    public ResponseEntity<DisasterAlert> getAlertById(@PathVariable Long id) {
        Optional<DisasterAlert> alert = disasterAlertService.getAlertById(id);
        return alert.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONDER')")
    public ResponseEntity<DisasterAlert> createAlert(@RequestBody DisasterAlert alert) {
        try {
            DisasterAlert createdAlert = disasterAlertService.createAlert(alert);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAlert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DisasterAlert> updateAlert(@PathVariable Long id, @RequestBody DisasterAlert alert) {
        try {
            DisasterAlert updatedAlert = disasterAlertService.updateAlert(id, alert);
            return ResponseEntity.ok(updatedAlert);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteAlert(@PathVariable Long id) {
        try {
            disasterAlertService.deleteAlert(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Alert deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/type/{disasterType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONDER', 'CLIENT')")
    public ResponseEntity<List<DisasterAlert>> getAlertsByType(@PathVariable DisasterType disasterType) {
        List<DisasterAlert> alerts = disasterAlertService.getAlertsByType(disasterType);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/level/{alertLevel}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONDER', 'CLIENT')")
    public ResponseEntity<List<DisasterAlert>> getAlertsByLevel(@PathVariable AlertLevel alertLevel) {
        List<DisasterAlert> alerts = disasterAlertService.getAlertsByLevel(alertLevel);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/location/{location}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONDER', 'CLIENT')")
    public ResponseEntity<List<DisasterAlert>> getAlertsByLocation(@PathVariable String location) {
        List<DisasterAlert> alerts = disasterAlertService.getAlertsByLocation(location);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/recent/{hours}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONDER', 'CLIENT')")
    public ResponseEntity<List<DisasterAlert>> getRecentAlerts(@PathVariable int hours) {
        List<DisasterAlert> alerts = disasterAlertService.getRecentAlerts(hours);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/critical")
    @PreAuthorize("hasAnyRole('ADMIN', 'RESPONDER', 'CLIENT')")
    public ResponseEntity<List<DisasterAlert>> getCriticalAlerts() {
        List<DisasterAlert> alerts = disasterAlertService.getCriticalAlerts();
        return ResponseEntity.ok(alerts);
    }
}
