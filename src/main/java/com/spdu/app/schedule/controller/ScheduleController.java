package com.spdu.app.schedule.controller;

import com.spdu.app.schedule.dto.ScheduleDto;
import com.spdu.app.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.spdu.app.schedule.converter.ScheduleConverter.fromDto;
import static com.spdu.app.schedule.converter.ScheduleConverter.toDto;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "salon")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/add-schedule")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ScheduleDto postSchedule(@RequestBody @Valid ScheduleDto scheduleDto) {
        return toDto(scheduleService.save(fromDto(scheduleDto)));
    }

    @GetMapping("/schedule")
    @PreAuthorize("hasAuthority('USER')")
    public List<ScheduleDto> getSchedule(@RequestParam int salonId) {
        return toDto(scheduleService.findAllBySalonId(salonId));
    }

    @DeleteMapping("/schedule")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public void deleteSchedule(@RequestParam int salonId) {
        scheduleService.deleteAllBySalonId(salonId);
    }
}
