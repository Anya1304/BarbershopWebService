package com.spdu.app.time_slot.controller;

import com.spdu.app.spring_security.SecurityUser;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.time_slot.service.TimeSlotService;
import com.spdu.app.user.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TimeSlotContr {

    private final TimeSlotService timeSlotService;
    private final UserRepository userRepository;

    public TimeSlotContr(TimeSlotService timeSlotService, UserRepository userRepository) {
        this.timeSlotService = timeSlotService;
        this.userRepository = userRepository;
    }

    @GetMapping("/slots")
    @PreAuthorize("hasAuthority('USER')")
    public String getTimeSlots(@RequestParam int employeeId, Model model) {
        List<TimeSlot> allByUserId = timeSlotService.findAllByUserId(employeeId);
        Map<String, List<TimeSlot>> map = new HashMap<>();
        for (TimeSlot timeSlot : allByUserId) {
            map.computeIfPresent(timeSlot.getDay().getDayOfWeek().name() + "\n" +
                    timeSlot.getDay().format(DateTimeFormatter.ofPattern("dd.MM")), (key, value) -> {
                value.add(timeSlot);
                return value;
            });
            map.computeIfAbsent(timeSlot.getDay().getDayOfWeek().name() + "\n" +
                    timeSlot.getDay().format(DateTimeFormatter.ofPattern("dd.MM")), (key) -> {
                List<TimeSlot> times = new ArrayList<>();
                times.add(timeSlot);
                return times;
            });
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("map", map);
        model.addAttribute("userId", ((SecurityUser) principal).getUser().getId());
        return "slots";
    }
}
