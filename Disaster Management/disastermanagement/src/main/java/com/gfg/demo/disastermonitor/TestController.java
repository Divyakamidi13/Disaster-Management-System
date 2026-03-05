package com.gfg.demo.disastermonitor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.varsha.disastermanagement.DisasterEvent;

@RestController
@RequestMapping("/api/test")
public class TestController {
    
    private final DisasterEventRepository disasterEventRepository;
    
    public TestController(DisasterEventRepository disasterEventRepository) {
        this.disasterEventRepository = disasterEventRepository;
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Backend is running!");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicTest() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Public endpoint working!");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/simple")
    public ResponseEntity<Map<String, String>> simpleTest() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Simple test working!");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/disasters-simple")
    public ResponseEntity<Map<String, Object>> disastersSimple() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== DEBUG: disastersSimple called ===");
            List<DisasterEvent> events = disasterEventRepository.findByStatus(com.varsha.disastermanagement.Status.VERIFIED);
            System.out.println("=== DEBUG: Found " + events.size() + " verified events ===");
            
            response.put("count", events.size());
            response.put("status", "OK");
            
            // Show all verified events (not just first 3)
            for (int i = 0; i < events.size(); i++) {
                DisasterEvent event = events.get(i);
                response.put("event_" + i, event.getTitle());
            }
            
            System.out.println("=== DEBUG: Returning response ===");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("=== ERROR in disastersSimple: " + e.getMessage() + " ===");
            e.printStackTrace();
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @GetMapping("/data-count")
    public ResponseEntity<Map<String, Object>> dataCount() {
        Map<String, Object> response = new HashMap<>();
        try {
            long totalEvents = disasterEventRepository.count();
            response.put("totalEvents", totalEvents);
            response.put("status", "OK");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            response.put("status", "ERROR");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
