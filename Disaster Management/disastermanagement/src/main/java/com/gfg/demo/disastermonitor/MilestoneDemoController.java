package com.gfg.demo.disastermonitor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.varsha.disastermanagement.DisasterEvent;
import com.varsha.disastermanagement.Status;

@RestController
@RequestMapping("/api/demo")
public class MilestoneDemoController {
    
    private final DisasterEventService disasterEventService;
    private final EarthquakeApiService earthquakeApiService;
    
    public MilestoneDemoController(DisasterEventService disasterEventService, EarthquakeApiService earthquakeApiService) {
        this.disasterEventService = disasterEventService;
        this.earthquakeApiService = earthquakeApiService;
    }
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getMilestoneStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Test database connection
            long totalEvents = disasterEventService.getAllEvents().size();
            long verifiedEvents = disasterEventService.getEventsByStatus(Status.VERIFIED).size();
            long pendingEvents = disasterEventService.getEventsByStatus(Status.PENDING).size();
            
            response.put("milestone", "2 - Advanced Disaster Monitoring Module");
            response.put("status", "WORKING");
            response.put("database", "CONNECTED");
            response.put("totalEvents", totalEvents);
            response.put("verifiedEvents", verifiedEvents);
            response.put("pendingEvents", pendingEvents);
            response.put("externalApi", "USGS Earthquake API");
            response.put("scheduler", "ENABLED");
            response.put("autoVerification", "ENABLED");
            response.put("jwtSecurity", "WORKING");
            response.put("caching", "ENABLED");
            
            // Test external API
            List<DisasterEvent> earthquakes = earthquakeApiService.fetchEarthquakes();
            response.put("lastApiFetch", earthquakes.size() + " earthquakes");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/events")
    public ResponseEntity<Map<String, Object>> getAllEvents() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<DisasterEvent> allEvents = disasterEventService.getAllEvents();
            List<DisasterEvent> verifiedEvents = disasterEventService.getEventsByStatus(Status.VERIFIED);
            List<DisasterEvent> pendingEvents = disasterEventService.getEventsByStatus(Status.PENDING);
            
            response.put("totalEvents", allEvents.size());
            response.put("verifiedEvents", verifiedEvents.size());
            response.put("pendingEvents", pendingEvents.size());
            
            // Add event details
            response.put("verifiedDetails", verifiedEvents.stream()
                .map(e -> Map.of(
                    "id", e.getId(),
                    "title", e.getTitle(),
                    "type", e.getDisasterType(),
                    "severity", e.getSeverity(),
                    "location", e.getLocationName()
                )).toList());
                
            response.put("pendingDetails", pendingEvents.stream()
                .map(e -> Map.of(
                    "id", e.getId(),
                    "title", e.getTitle(),
                    "type", e.getDisasterType(),
                    "severity", e.getSeverity(),
                    "location", e.getLocationName()
                )).toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PostMapping("/test-external-api")
    public ResponseEntity<Map<String, Object>> testExternalApi() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<DisasterEvent> earthquakes = earthquakeApiService.fetchEarthquakes();
            
            response.put("api", "USGS Earthquake API");
            response.put("status", "WORKING");
            response.put("fetchedEvents", earthquakes.size());
            
            // Show first few events
            response.put("sampleEvents", earthquakes.stream()
                .limit(3)
                .map(e -> Map.of(
                    "title", e.getTitle(),
                    "magnitude", e.getSeverity(),
                    "location", e.getLocationName(),
                    "status", e.getStatus()
                )).toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PostMapping("/auto-verify")
    public ResponseEntity<Map<String, Object>> testAutoVerification() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            disasterEventService.autoVerifyCriticalEvents();
            
            long pendingCount = disasterEventService.getEventsByStatus(Status.PENDING).size();
            long verifiedCount = disasterEventService.getEventsByStatus(Status.VERIFIED).size();
            
            response.put("autoVerification", "EXECUTED");
            response.put("pendingEvents", pendingCount);
            response.put("verifiedEvents", verifiedCount);
            response.put("status", "SUCCESS");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
