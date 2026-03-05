package com.gfg.demo.disastermonitor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.varsha.disastermanagement.DisasterEvent;
import com.varsha.disastermanagement.Status;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class DisasterSSEController {
    
    private static final Logger logger = LoggerFactory.getLogger(DisasterSSEController.class);
    
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    
    @Autowired
    private DisasterEventService disasterEventService;
    
    @GetMapping(path = "/disasters/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamDisasters() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        
        try {
            // Send initial data
            List<DisasterEvent> verifiedEvents = disasterEventService.getEventsByStatus(Status.VERIFIED);
            emitter.send(SseEmitter.event()
                .name("initial-data")
                .data(verifiedEvents));
        } catch (IOException e) {
            logger.error("Error sending initial data: {}", e.getMessage());
            emitter.completeWithError(e);
        }
        
        return emitter;
    }
    
    public void broadcastNewEvent(DisasterEvent event) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                    .name("new-event")
                    .data(event));
            } catch (IOException e) {
                logger.error("Error broadcasting event: {}", e.getMessage());
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
    
    public void broadcastEventUpdate(DisasterEvent event) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                    .name("event-update")
                    .data(event));
            } catch (IOException e) {
                logger.error("Error broadcasting update: {}", e.getMessage());
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }
}
