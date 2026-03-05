package com.gfg.demo.disastermonitor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;
import com.varsha.disastermanagement.DisasterEvent;
import com.varsha.disastermanagement.Status;

@RestController
@RequestMapping("/api/admin/disasters")
// @PreAuthorize("hasRole('ADMIN')") // Temporarily disabled for debugging
public class AdminDisasterController {
    
    private final DisasterEventService disasterEventService;
    private final DisasterSSEController sseController;
    
    public AdminDisasterController(DisasterEventService disasterEventService, DisasterSSEController sseController) {
        this.disasterEventService = disasterEventService;
        this.sseController = sseController;
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<DisasterEventDTO>> getPendingDisasters() {
        try {
            System.out.println("=== DEBUG: getPendingDisasters called ===");
            List<DisasterEvent> pending = disasterEventService.getEventsByStatus(Status.PENDING);
            System.out.println("=== DEBUG: Found " + pending.size() + " pending events ===");
            List<DisasterEventDTO> pendingDTOs = pending.stream()
                .map(DisasterEventDTO::new)
                .toList();
            System.out.println("=== DEBUG: Returning " + pendingDTOs.size() + " DTOs ===");
            return ResponseEntity.ok(pendingDTOs);
        } catch (Exception e) {
            System.err.println("=== ERROR in getPendingDisasters: " + e.getMessage() + " ===");
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<DisasterEventDTO> approveDisaster(@PathVariable Long id) {
        try {
            DisasterEvent approved = disasterEventService.approveEvent(id, "ADMIN");
            DisasterEventDTO approvedDTO = new DisasterEventDTO(approved);
            
            // Broadcast the approved event to all connected clients
            sseController.broadcastEventUpdate(approved);
            
            return ResponseEntity.ok(approvedDTO);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<DisasterEventDTO> rejectDisaster(@PathVariable Long id) {
        try {
            DisasterEvent rejected = disasterEventService.rejectEvent(id, "ADMIN");
            DisasterEventDTO rejectedDTO = new DisasterEventDTO(rejected);
            return ResponseEntity.ok(rejectedDTO);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/summary")
    public ResponseEntity<DisasterSummaryDTO> getDisasterSummary() {
        try {
            DisasterSummaryDTO summary = disasterEventService.getDisasterSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
