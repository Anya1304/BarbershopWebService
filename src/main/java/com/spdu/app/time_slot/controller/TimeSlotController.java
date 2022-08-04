package com.spdu.app.time_slot.controller;

import com.spdu.app.time_slot.dto.TimeSlotDto;
import com.spdu.app.time_slot.service.TimeSlotService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.spdu.app.time_slot.converter.TimeSlotConverter.fromDto;
import static com.spdu.app.time_slot.converter.TimeSlotConverter.toDto;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "salon")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @PostMapping("/add-time-slot")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public TimeSlotDto postTimeSlot(@RequestBody @Valid TimeSlotDto timeSlotDto) {
        return toDto(timeSlotService.save(fromDto(timeSlotDto)));
    }

    @GetMapping("/time-slots")
    @PreAuthorize("hasAuthority('USER')")
    public List<TimeSlotDto> getTimeSlot(@RequestParam int employeeId) {
        return toDto(timeSlotService.findAllByUserId(employeeId));
    }

    @DeleteMapping("/time-slots")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public void deleteTimeSlot(@RequestParam int employeeId) {
        timeSlotService.delete(employeeId);
    }
}
