package com.gfg.demo.disastermonitor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.varsha.disastermanagement.DisasterEvent;
import com.varsha.disastermanagement.Status;
import com.varsha.disastermanagement.enums.DisasterType;
import com.varsha.disastermanagement.SeverityLevel;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EarthquakeApiService {
    
    private static final Logger logger = LoggerFactory.getLogger(EarthquakeApiService.class);
    private static final String USGS_API_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&limit=10";
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final DisasterEventRepository disasterEventRepository;
    
    @Autowired
    public EarthquakeApiService(RestTemplate restTemplate, ObjectMapper objectMapper, DisasterEventRepository disasterEventRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.disasterEventRepository = disasterEventRepository;
    }
    
    @Cacheable(value = "earthquake-data", unless = "#result.isEmpty()")
    public List<DisasterEvent> fetchEarthquakes() {
        try {
            logger.info("Fetching earthquake data from USGS API");
            String response = restTemplate.getForObject(USGS_API_URL, String.class);
            
            if (response == null || response.isEmpty()) {
                logger.warn("Empty response from USGS API");
                return new ArrayList<>();
            }
            
            JsonNode root = objectMapper.readTree(response);
            JsonNode features = root.path("features");
            
            List<DisasterEvent> events = new ArrayList<>();
            
            for (JsonNode feature : features) {
                try {
                    DisasterEvent event = parseEarthquakeEvent(feature);
                    if (event != null) {
                        events.add(event);
                    }
                } catch (Exception e) {
                    logger.error("Error parsing earthquake event: {}", e.getMessage());
                }
            }
            
            logger.info("Successfully parsed {} earthquake events", events.size());
            return events;
            
        } catch (Exception e) {
            logger.error("Error fetching earthquake data: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    private DisasterEvent parseEarthquakeEvent(JsonNode feature) {
        JsonNode properties = feature.path("properties");
        JsonNode geometry = feature.path("geometry");
        JsonNode coordinates = geometry.path("coordinates");
        
        String title = properties.path("title").asText();
        String description = properties.path("place").asText();
        double magnitude = properties.path("mag").asDouble();
        long time = properties.path("time").asLong();
        
        // Determine severity based on magnitude
        SeverityLevel severity = determineSeverity(magnitude);
        
        // Create event
        DisasterEvent event = new DisasterEvent();
        event.setTitle(title);
        event.setDescription(description);
        event.setDisasterType(DisasterType.EARTHQUAKE);
        event.setSeverity(severity);
        event.setSource("USGS");
        event.setEventTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
        event.setStatus(Status.PENDING); // External events start as PENDING
        
        // Set coordinates if available
        if (coordinates.size() >= 2) {
            event.setLongitude(coordinates.get(0).asDouble());
            event.setLatitude(coordinates.get(1).asDouble());
            event.setLocationName(description);
        }
        
        return event;
    }
    
    private SeverityLevel determineSeverity(double magnitude) {
        if (magnitude >= 7.0) {
            return SeverityLevel.CRITICAL;
        } else if (magnitude >= 5.5) {
            return SeverityLevel.HIGH;
        } else if (magnitude >= 4.0) {
            return SeverityLevel.MEDIUM;
        } else {
            return SeverityLevel.LOW;
        }
    }
    
    public void syncEarthquakes() {
        try {
            List<DisasterEvent> earthquakes = fetchEarthquakes();
            int savedCount = 0;
            
            for (DisasterEvent earthquake : earthquakes) {
                // Check for duplicates
                Optional<DisasterEvent> existing = disasterEventRepository
                    .findByTitleAndEventTime(earthquake.getTitle(), earthquake.getEventTime());
                
                if (existing.isEmpty()) {
                    disasterEventRepository.save(earthquake);
                    savedCount++;
                    logger.info("Saved new earthquake: {}", earthquake.getTitle());
                } else {
                    logger.debug("Duplicate earthquake skipped: {}", earthquake.getTitle());
                }
            }
            
            logger.info("Earthquake sync completed. Saved {} new events", savedCount);
            
        } catch (Exception e) {
            logger.error("Error during earthquake sync: {}", e.getMessage(), e);
        }
    }
}
