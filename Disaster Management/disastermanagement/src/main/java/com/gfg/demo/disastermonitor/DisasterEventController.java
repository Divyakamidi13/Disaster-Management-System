package com.gfg.demo.disastermonitor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import com.varsha.disastermanagement.DisasterEvent;
import com.varsha.disastermanagement.Status;
import com.varsha.disastermanagement.enums.DisasterType;
import com.varsha.disastermanagement.SeverityLevel;

@RestController
@RequestMapping("/api/disasters")
public class DisasterEventController {
    
    private final DisasterEventService disasterEventService;
    
    public DisasterEventController(DisasterEventService disasterEventService) {
        this.disasterEventService = disasterEventService;
    }
    
    @GetMapping
    @Cacheable(value = "verified-disasters", key = "#disasterType?.toString() + '-' + #severity?.toString() + '-' + #locationName + '-' + #fromDate + '-' + #toDate", unless = "#result.isEmpty()")
    public ResponseEntity<List<DisasterEventDTO>> getAllVerifiedDisasters(
            @RequestParam(required = false) DisasterType disasterType,
            @RequestParam(required = false) SeverityLevel severity,
            @RequestParam(required = false) String locationName,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        
        try {
            List<DisasterEvent> events = disasterEventService.getEventsByStatus(Status.VERIFIED);
            
            // Apply filters if provided
            if (disasterType != null) {
                events = events.stream()
                    .filter(e -> e.getDisasterType() == disasterType)
                    .toList();
            }
            
            if (severity != null) {
                events = events.stream()
                    .filter(e -> e.getSeverity() == severity)
                    .toList();
            }
            
            if (locationName != null && !locationName.isEmpty()) {
                events = events.stream()
                    .filter(e -> e.getLocationName() != null && 
                             e.getLocationName().toLowerCase().contains(locationName.toLowerCase()))
                    .toList();
            }
            
            // Convert to proper DTOs
            List<DisasterEventDTO> eventDTOs = events.stream()
                .map(event -> new DisasterEventDTO(event))
                .toList();
            
            return ResponseEntity.ok(eventDTOs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DisasterEventDTO> getDisasterById(@PathVariable Long id) {
        try {
            Optional<DisasterEvent> event = disasterEventService.getEventById(id);
            if (event.isPresent() && event.get().getStatus() == Status.VERIFIED) {
                return ResponseEntity.ok(new DisasterEventDTO(event.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getPublicSummary() {
        try {
            List<DisasterEvent> allEvents = disasterEventService.getAllEvents();
            long totalEvents = allEvents.size();
            long verifiedEvents = allEvents.stream()
                .filter(e -> e.getStatus() == Status.VERIFIED)
                .count();
            long pendingEvents = allEvents.stream()
                .filter(e -> e.getStatus() == Status.PENDING)
                .count();
            long rejectedEvents = allEvents.stream()
                .filter(e -> e.getStatus() == Status.REJECTED)
                .count();
            long autoApprovedEvents = allEvents.stream()
                .filter(e -> e.getAutoApproved() != null && e.getAutoApproved())
                .count();
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalEvents", totalEvents);
            summary.put("totalVerified", verifiedEvents);
            summary.put("totalPending", pendingEvents);
            summary.put("totalRejected", rejectedEvents);
            summary.put("totalAutoApproved", autoApprovedEvents);
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
