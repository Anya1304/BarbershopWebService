package com.spdu.app.time_slot.service;

import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.hairdressing_salon.service.HairdressingSalonService;
import com.spdu.app.schedule.model.Schedule;
import com.spdu.app.schedule.service.ScheduleService;
import com.spdu.app.user.model.User;
import com.spdu.app.user.service.UserServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotGenerator {

    private final HairdressingSalonService salonService;
    private final ScheduleService scheduleService;
    private final UserServiceImpl userService;

    public TimeSlotGenerator(HairdressingSalonService salonService,
                             ScheduleService scheduleService,
                             UserServiceImpl userService) {
        this.salonService = salonService;
        this.scheduleService = scheduleService;
        this.userService = userService;
    }

    @Scheduled(fixedRateString = "${timeSlotGeneration.period}")
    private void generateSlots() {
        List<HairdressingSalon> salons = salonService.getList();

        salons.stream().parallel().forEach(salon -> {
            List<Schedule> schedules = scheduleService.findAllBySalonId(salon.getId());
            List<User> users = userService.getUsersBySalon(salon);

            for (User user : users) {
                userService.generateTimeSlotForUser(schedules, user);
            }
        });
    }
}
