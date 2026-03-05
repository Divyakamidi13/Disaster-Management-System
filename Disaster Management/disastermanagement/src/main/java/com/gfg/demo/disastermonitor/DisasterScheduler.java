package com.gfg.demo.disastermonitor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DisasterScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(DisasterScheduler.class);
    
    @Autowired
    private EarthquakeApiService earthquakeApiService;
    
    @Autowired
    private DisasterEventService disasterEventService;
    
    // Fetch earthquake data every 5 minutes
    @Scheduled(fixedRate = 300000) // 5 minutes in milliseconds
    public void fetchAndStoreEarthquakes() {
        try {
            logger.info("Starting scheduled earthquake data fetch");
            earthquakeApiService.syncEarthquakes();
            logger.info("Completed scheduled earthquake data fetch");
        } catch (Exception e) {
            logger.error("Error in scheduled earthquake fetch: {}", e.getMessage(), e);
        }
    }
    
    // Auto-verify critical events every 10 minutes
    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void autoVerifyCriticalEvents() {
        try {
            logger.info("Starting auto-verification of critical events");
            disasterEventService.autoVerifyCriticalEvents();
            logger.info("Completed auto-verification of critical events");
        } catch (Exception e) {
            logger.error("Error in auto-verification: {}", e.getMessage(), e);
        }
    }
}
