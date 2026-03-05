package com.varsha.disastermanagement.repository;

import com.varsha.disastermanagement.entity.DisasterAlert;
import com.varsha.disastermanagement.enums.AlertLevel;
import com.varsha.disastermanagement.enums.DisasterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DisasterAlertRepository extends JpaRepository<DisasterAlert, Long> {
    
    List<DisasterAlert> findByIsActiveOrderByCreatedAtDesc(Boolean isActive);
    
    List<DisasterAlert> findByDisasterTypeOrderByCreatedAtDesc(DisasterType disasterType);
    
    List<DisasterAlert> findByAlertLevelOrderByCreatedAtDesc(AlertLevel alertLevel);
    
    List<DisasterAlert> findByLocationContainingIgnoreCaseOrderByCreatedAtDesc(String location);
    
    @Query("SELECT d FROM DisasterAlert d WHERE d.createdAt >= :since ORDER BY d.createdAt DESC")
    List<DisasterAlert> findRecentAlerts(@Param("since") LocalDateTime since);
    
    @Query("SELECT d FROM DisasterAlert d WHERE d.isActive = true AND d.alertLevel = :level ORDER BY d.createdAt DESC")
    List<DisasterAlert> findActiveAlertsByLevel(@Param("level") AlertLevel alertLevel);
}
