package com.gfg.demo.disastermonitor;

import com.varsha.disastermanagement.enums.DisasterType;
import com.varsha.disastermanagement.SeverityLevel;
import com.varsha.disastermanagement.Status;

public class DisasterEventDTO {
    
    private Long id;
    private String title;
    private String description;
    private DisasterType disasterType;
    private SeverityLevel severity;
    private String locationName;
    private String source;
    private Status status;
    private Boolean autoApproved;
    private String approvedBy;
    private String approvedAt;
    private String createdAt;
    
    // Constructors
    public DisasterEventDTO() {}
    
    public DisasterEventDTO(com.varsha.disastermanagement.DisasterEvent event) {
        if (event == null) return;
        
        this.id = event.getId();
        this.title = event.getTitle() != null ? event.getTitle() : "";
        this.description = event.getDescription() != null ? event.getDescription() : "";
        this.disasterType = event.getDisasterType();
        this.severity = event.getSeverity();
        this.locationName = event.getLocationName() != null ? event.getLocationName() : "";
        this.source = event.getSource() != null ? event.getSource() : "";
        this.status = event.getStatus();
        this.autoApproved = event.getAutoApproved() != null ? event.getAutoApproved() : false;
        this.approvedBy = event.getApprovedBy();
        this.approvedAt = event.getApprovedAt() != null ? event.getApprovedAt().toString() : null;
        this.createdAt = event.getCreatedAt() != null ? event.getCreatedAt().toString() : null;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public DisasterType getDisasterType() { return disasterType; }
    public void setDisasterType(DisasterType disasterType) { this.disasterType = disasterType; }
    
    public SeverityLevel getSeverity() { return severity; }
    public void setSeverity(SeverityLevel severity) { this.severity = severity; }
    
    public String getLocationName() { return locationName; }
    public void setLocationName(String locationName) { this.locationName = locationName; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public Boolean getAutoApproved() { return autoApproved; }
    public void setAutoApproved(Boolean autoApproved) { this.autoApproved = autoApproved; }
    
    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }
    
    public String getApprovedAt() { return approvedAt; }
    public void setApprovedAt(String approvedAt) { this.approvedAt = approvedAt; }
    
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
