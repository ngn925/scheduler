package com.vgr.scheduler.controller;

import com.vgr.scheduler.service.SchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @GetMapping("/available-slots")
    public ResponseEntity<?> getAvailableSlots(@RequestParam int duration) {
        try {
            return ResponseEntity.ok(schedulerService.findAvailableSlots(duration));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
