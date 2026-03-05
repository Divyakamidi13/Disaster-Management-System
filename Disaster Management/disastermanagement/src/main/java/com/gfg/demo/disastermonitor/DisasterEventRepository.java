package com.gfg.demo.disastermonitor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.varsha.disastermanagement.DisasterEvent;
import com.varsha.disastermanagement.Status;
import com.varsha.disastermanagement.enums.DisasterType;
import com.varsha.disastermanagement.SeverityLevel;

@Repository
public interface DisasterEventRepository extends JpaRepository<DisasterEvent, Long> {
    
    Optional<DisasterEvent> findByTitleAndEventTime(String title, java.time.LocalDateTime eventTime);
    
    List<DisasterEvent> findByStatus(Status status);
    
    List<DisasterEvent> findByDisasterType(DisasterType disasterType);
    
    List<DisasterEvent> findBySeverity(SeverityLevel severity);
    
    List<DisasterEvent> findByLocationNameContainingIgnoreCase(String locationName);
    
    // Auto-verification methods
    @Query("SELECT d FROM DisasterEvent d WHERE d.severity = 'CRITICAL' AND d.status = 'PENDING' AND d.createdAt < :threshold")
    List<DisasterEvent> findCriticalPendingEvents(@Param("threshold") java.time.LocalDateTime threshold);
    
    // Summary methods
    long countByStatus(Status status);
    
    long countByAutoApprovedTrue();
}
