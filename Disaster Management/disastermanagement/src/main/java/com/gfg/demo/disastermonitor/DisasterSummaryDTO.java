package com.gfg.demo.disastermonitor;

public class DisasterSummaryDTO {
    private Long totalEvents;
    private Long totalPending;
    private Long totalVerified;
    private Long totalRejected;
    private Long totalAutoApproved;
    
    // Constructors
    public DisasterSummaryDTO() {}
    
    public DisasterSummaryDTO(Long totalEvents, Long totalPending, Long totalVerified, Long totalRejected, Long totalAutoApproved) {
        this.totalEvents = totalEvents;
        this.totalPending = totalPending;
        this.totalVerified = totalVerified;
        this.totalRejected = totalRejected;
        this.totalAutoApproved = totalAutoApproved;
    }
    
    // Getters and Setters
    public Long getTotalEvents() { return totalEvents; }
    public void setTotalEvents(Long totalEvents) { this.totalEvents = totalEvents; }
    
    public Long getTotalPending() { return totalPending; }
    public void setTotalPending(Long totalPending) { this.totalPending = totalPending; }
    
    public Long getTotalVerified() { return totalVerified; }
    public void setTotalVerified(Long totalVerified) { this.totalVerified = totalVerified; }
    
    public Long getTotalRejected() { return totalRejected; }
    public void setTotalRejected(Long totalRejected) { this.totalRejected = totalRejected; }
    
    public Long getTotalAutoApproved() { return totalAutoApproved; }
    public void setTotalAutoApproved(Long totalAutoApproved) { this.totalAutoApproved = totalAutoApproved; }
}
