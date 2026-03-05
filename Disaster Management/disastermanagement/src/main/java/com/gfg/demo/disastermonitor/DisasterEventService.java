package com.gfg.demo.disastermonitor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.varsha.disastermanagement.DisasterEvent;
import com.varsha.disastermanagement.Status;
import com.varsha.disastermanagement.enums.DisasterType;
import com.varsha.disastermanagement.SeverityLevel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DisasterEventService {
    
    private static final Logger logger = LoggerFactory.getLogger(DisasterEventService.class);
    
    private final DisasterEventRepository disasterEventRepository;
    
    public DisasterEventService(DisasterEventRepository disasterEventRepository) {
        this.disasterEventRepository = disasterEventRepository;
    }
    
    public List<DisasterEvent> getAllEvents() {
        return disasterEventRepository.findAll();
    }
    
    public Optional<DisasterEvent> getEventById(Long id) {
        return disasterEventRepository.findById(id);
    }
    
    public DisasterEvent saveEvent(DisasterEvent event) {
        return disasterEventRepository.save(event);
    }
    
    public void deleteEvent(Long id) {
        disasterEventRepository.deleteById(id);
    }
    
    public List<DisasterEvent> getEventsByStatus(Status status) {
        return disasterEventRepository.findByStatus(status);
    }
    
    public List<DisasterEvent> getEventsByDisasterType(DisasterType disasterType) {
        return disasterEventRepository.findByDisasterType(disasterType);
    }
    
    public List<DisasterEvent> getEventsBySeverity(SeverityLevel severity) {
        return disasterEventRepository.findBySeverity(severity);
    }
    
    public List<DisasterEvent> getEventsByLocation(String locationName) {
        return disasterEventRepository.findByLocationNameContainingIgnoreCase(locationName);
    }
    
    public DisasterEvent approveEvent(Long id, String approvedBy) {
        try {
            Optional<DisasterEvent> eventOpt = disasterEventRepository.findById(id);
            if (eventOpt.isPresent()) {
                DisasterEvent event = eventOpt.get();
                
                // Business Rule Validation
                if (event.getStatus() == Status.VERIFIED) {
                    throw new RuntimeException("Event is already VERIFIED. Cannot approve again.");
                }
                
                if (event.getStatus() == Status.REJECTED) {
                    throw new RuntimeException("Event is REJECTED. Cannot approve rejected events.");
                }
                
                event.setStatus(Status.VERIFIED);
                event.setApprovedBy(approvedBy);
                event.setApprovedAt(java.time.LocalDateTime.now());
                event.setAutoApproved(false);
                
                DisasterEvent saved = disasterEventRepository.save(event);
                logger.info("Approved disaster event: {} by {}", saved.getTitle(), approvedBy);
                
                return saved;
            }
            throw new RuntimeException("Event not found with id: " + id);
        } catch (RuntimeException e) {
            logger.error("Error approving disaster event: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error approving disaster event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to approve disaster event", e);
        }
    }
    
    public DisasterEvent rejectEvent(Long id, String approvedBy) {
        try {
            Optional<DisasterEvent> eventOpt = disasterEventRepository.findById(id);
            if (eventOpt.isPresent()) {
                DisasterEvent event = eventOpt.get();
                event.setStatus(Status.REJECTED);
                event.setApprovedBy(approvedBy);
                event.setApprovedAt(java.time.LocalDateTime.now());
                event.setAutoApproved(false);
                
                DisasterEvent saved = disasterEventRepository.save(event);
                logger.info("Rejected disaster event: {} by {}", saved.getTitle(), approvedBy);
                
                return saved;
            }
            throw new RuntimeException("Event not found with id: " + id);
        } catch (RuntimeException e) {
            logger.error("Error rejecting disaster event: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error rejecting disaster event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to reject disaster event", e);
        }
    }
    
    public DisasterSummaryDTO getDisasterSummary() {
        try {
            long totalEvents = disasterEventRepository.count();
            long totalPending = disasterEventRepository.countByStatus(Status.PENDING);
            long totalVerified = disasterEventRepository.countByStatus(Status.VERIFIED);
            long totalRejected = disasterEventRepository.countByStatus(Status.REJECTED);
            long totalAutoApproved = disasterEventRepository.countByAutoApprovedTrue();
            
            return new DisasterSummaryDTO(totalEvents, totalPending, totalVerified, totalRejected, totalAutoApproved);
        } catch (Exception e) {
            logger.error("Error generating disaster summary: {}", e.getMessage(), e);
            return new DisasterSummaryDTO(0L, 0L, 0L, 0L, 0L);
        }
    }
    
    public void autoVerifyCriticalEvents() {
        try {
            LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
            List<DisasterEvent> criticalPendingEvents = disasterEventRepository.findCriticalPendingEvents(threshold);
            
            for (DisasterEvent event : criticalPendingEvents) {
                event.setStatus(Status.VERIFIED);
                event.setApprovedBy("SYSTEM_AUTO");
                event.setApprovedAt(LocalDateTime.now());
                event.setAutoApproved(true);
                
                disasterEventRepository.save(event);
                logger.info("Auto-verified critical event: {} ({})", event.getTitle(), event.getSeverity());
            }
            
            if (!criticalPendingEvents.isEmpty()) {
                logger.info("Auto-verified {} critical events", criticalPendingEvents.size());
            }
        } catch (Exception e) {
            logger.error("Error in auto-verification: {}", e.getMessage(), e);
        }
    }
}
