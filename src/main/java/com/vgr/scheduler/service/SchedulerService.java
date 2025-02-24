package com.vgr.scheduler.service;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SchedulerService {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final LocalTime WORKING_START = LocalTime.parse("08:00", TIME_FORMATTER);
    private static final LocalTime WORKING_END = LocalTime.parse("17:00", TIME_FORMATTER);

    private final List<TimeSlot> busySlots = new ArrayList<>();

    public SchedulerService() {
        // Initialize with predefined busy slots
        busySlots.add(new TimeSlot("09:15", "10:45"));
        busySlots.add(new TimeSlot("12:00", "12:15"));
        busySlots.add(new TimeSlot("15:00", "15:30"));
    }

    public record TimeSlot(LocalTime start, LocalTime end) {
        public TimeSlot(String start, String end) {
            this(LocalTime.parse(start, TIME_FORMATTER), LocalTime.parse(end, TIME_FORMATTER));
        }

        public boolean overlaps(LocalTime start, LocalTime end) {
            return !(this.end.isBefore(start) || this.end.equals(start)) &&
                    !(this.start.isAfter(end) || this.start.equals(end));
        }
    }

    public List<String> findAvailableSlots(int durationMinutes) {
        if (durationMinutes < 15) {
            throw new IllegalArgumentException("Minimum meeting duration is 15 minutes");
        }

        List<String> availableSlots = new ArrayList<>();
        LocalTime currentStart = WORKING_START;

        while (currentStart.plusMinutes(durationMinutes).isBefore(WORKING_END) ||
                currentStart.plusMinutes(durationMinutes).equals(WORKING_END)) {
            LocalTime currentEnd = currentStart.plusMinutes(durationMinutes);

            LocalTime finalCurrentStart = currentStart;
            boolean isAvailable = busySlots.stream()
                    .noneMatch(slot -> slot.overlaps(finalCurrentStart, currentEnd));

            if (isAvailable) {
                availableSlots.add("%s-%s".formatted(
                        currentStart.format(TIME_FORMATTER),
                        currentEnd.format(TIME_FORMATTER)));
            }
            currentStart = currentStart.plusMinutes(15); // Move to next possible slot
        }

        if (availableSlots.isEmpty()) {
            throw new IllegalStateException("No available slots found for " + durationMinutes + " minutes");
        }

        return availableSlots;
    }
}
