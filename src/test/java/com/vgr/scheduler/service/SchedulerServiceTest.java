package com.vgr.scheduler.service;

// SchedulerServiceTest.java

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

class SchedulerServiceTest {

    private SchedulerService schedulerService;

    @BeforeEach
    void setUp() {
        schedulerService = new SchedulerService();
    }

    @Test
    void testValidDuration() {
        List<String> slots = schedulerService.findAvailableSlots(30);
        assertFalse(slots.isEmpty());
        assertTrue(slots.contains("08:00-08:30"));
        assertFalse(slots.contains("12:00-12:30")); // Busy slot
        assertTrue(slots.contains("13:00-13:30"));  // After lunch
    }

    @Test
    void testMinimumDuration() {
        List<String> slots = schedulerService.findAvailableSlots(15);
        assertFalse(slots.isEmpty());
        assertTrue(slots.contains("08:00-08:15"));
    }

    @Test
    void testInvalidDuration() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> schedulerService.findAvailableSlots(10)
        );
        assertEquals("Minimum meeting duration is 15 minutes", exception.getMessage());
    }

    @Test
    void testTimeSlotOverlap() {
        SchedulerService.TimeSlot slot = new SchedulerService.TimeSlot("10:00", "10:30");
        assertTrue(slot.overlaps(LocalTime.parse("10:15"), LocalTime.parse("10:45")));
        assertFalse(slot.overlaps(LocalTime.parse("10:31"), LocalTime.parse("11:00")));
    }

    @Test
    void testAllQuarterAvailable() {
        List<String> slots = schedulerService.findAvailableSlots(15);
        assertFalse(slots.contains("07:00-07:15")); // 07
        assertFalse(slots.contains("07:15-07:30"));
        assertFalse(slots.contains("07:30-07:45"));
        assertFalse(slots.contains("07:45-08:00"));
        assertTrue(slots.contains("08:00-08:15")); // 08
        assertTrue(slots.contains("08:15-08:30"));
        assertTrue(slots.contains("08:30-08:45"));
        assertTrue(slots.contains("08:45-09:00"));
        assertTrue(slots.contains("09:00-09:15")); // 09
        assertFalse(slots.contains("09:15-09:30"));
        assertFalse(slots.contains("09:30-09:45"));
        assertFalse(slots.contains("09:45-10:00"));
        assertFalse(slots.contains("10:00-10:15")); // 10
        assertFalse(slots.contains("10:15-10:30"));
        assertFalse(slots.contains("10:30-10:45"));
        assertTrue(slots.contains("10:45-11:00"));
        assertTrue(slots.contains("11:00-11:15")); // 11
        assertTrue(slots.contains("11:15-11:30"));
        assertTrue(slots.contains("11:30-11:45"));
        assertTrue(slots.contains("11:45-12:00"));
        assertFalse(slots.contains("12:00-12:15")); // 12
        assertTrue(slots.contains("12:15-12:30"));
        assertTrue(slots.contains("12:30-12:45"));
        assertTrue(slots.contains("12:45-13:00"));
        assertTrue(slots.contains("13:00-13:15")); // 13
        assertTrue(slots.contains("13:15-13:30"));
        assertTrue(slots.contains("13:30-13:45"));
        assertTrue(slots.contains("13:45-14:00"));
        assertTrue(slots.contains("14:00-14:15")); // 14
        assertTrue(slots.contains("14:15-14:30"));
        assertTrue(slots.contains("14:30-14:45"));
        assertTrue(slots.contains("14:45-15:00"));
        assertFalse(slots.contains("15:00-15:15")); // 15
        assertFalse(slots.contains("15:15-15:30"));
        assertTrue(slots.contains("15:30-15:45"));
        assertTrue(slots.contains("15:45-16:00"));
        assertTrue(slots.contains("16:00-16:15")); // 16
        assertTrue(slots.contains("16:15-16:30"));
        assertTrue(slots.contains("16:30-16:45"));
        assertTrue(slots.contains("16:45-17:00"));
        assertFalse(slots.contains("17:00-17:15")); // 17
        assertFalse(slots.contains("17:15-17:30"));
        assertFalse(slots.contains("17:30-17:45"));
        assertFalse(slots.contains("17:45-18:00"));
    }

    @Test
    void testLongMeetingAvailable() {
        List<String> slots = schedulerService.findAvailableSlots(120);
        assertTrue(slots.contains("12:15-14:15"));
        assertTrue(slots.contains("12:30-14:30"));
        assertTrue(slots.contains("12:45-14:45"));
        assertTrue(slots.contains("13:00-15:00"));
    }

    @Test
    void testLunchTimeAvailable() {
        List<String> slots = schedulerService.findAvailableSlots(30);
        assertTrue(slots.contains("12:15-12:45"));
    }
}