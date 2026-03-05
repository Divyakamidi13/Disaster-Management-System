package com.gfg.demo.disastermonitor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.varsha.disastermanagement.DisasterEvent;
import com.varsha.disastermanagement.Status;
import com.varsha.disastermanagement.enums.DisasterType;
import com.varsha.disastermanagement.SeverityLevel;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final DisasterEventRepository disasterEventRepository;
    
    public DataInitializer(DisasterEventRepository disasterEventRepository) {
        this.disasterEventRepository = disasterEventRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (disasterEventRepository.count() == 0) {
            // Add sample verified disasters
            DisasterEvent event1 = new DisasterEvent();
            event1.setTitle("Earthquake in Chennai");
            event1.setDescription("Moderate earthquake detected in Chennai region");
            event1.setDisasterType(DisasterType.EARTHQUAKE);
            event1.setSeverity(SeverityLevel.MEDIUM);
            event1.setLocationName("Chennai, Tamil Nadu");
            event1.setLatitude(13.0827);
            event1.setLongitude(80.2707);
            event1.setSource("USGS");
            event1.setEventTime(LocalDateTime.now().minusHours(2));
            event1.setStatus(Status.VERIFIED);
            event1.setApprovedBy("ADMIN");
            event1.setApprovedAt(LocalDateTime.now().minusHours(1));
            event1.setAutoApproved(false);
            
            DisasterEvent event2 = new DisasterEvent();
            event2.setTitle("Flood Warning in Mumbai");
            event2.setDescription("Heavy rainfall causing flooding in low-lying areas");
            event2.setDisasterType(DisasterType.FLOOD);
            event2.setSeverity(SeverityLevel.HIGH);
            event2.setLocationName("Mumbai, Maharashtra");
            event2.setLatitude(19.0760);
            event2.setLongitude(72.8777);
            event2.setSource("IMD");
            event2.setEventTime(LocalDateTime.now().minusHours(4));
            event2.setStatus(Status.VERIFIED);
            event2.setApprovedBy("ADMIN");
            event2.setApprovedAt(LocalDateTime.now().minusHours(3));
            event2.setAutoApproved(false);
            
            // Add sample pending disaster
            DisasterEvent pendingEvent = new DisasterEvent();
            pendingEvent.setTitle("Cyclone Formation in Bay of Bengal");
            pendingEvent.setDescription("Low pressure area developing into cyclone");
            pendingEvent.setDisasterType(DisasterType.HURRICANE);
            pendingEvent.setSeverity(SeverityLevel.CRITICAL);
            pendingEvent.setLocationName("Bay of Bengal");
            pendingEvent.setLatitude(15.0);
            pendingEvent.setLongitude(85.0);
            pendingEvent.setSource("IMD");
            pendingEvent.setEventTime(LocalDateTime.now().minusMinutes(30));
            pendingEvent.setStatus(Status.PENDING);
            
            disasterEventRepository.save(event1);
            disasterEventRepository.save(event2);
            disasterEventRepository.save(pendingEvent);
            
            System.out.println("Sample data initialized successfully!");
        }
    }
}
