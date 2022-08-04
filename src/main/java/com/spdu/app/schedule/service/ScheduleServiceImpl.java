package com.spdu.app.schedule.service;

import com.spdu.app.schedule.exception.DayDuplicationException;
import com.spdu.app.schedule.model.Schedule;
import com.spdu.app.schedule.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Schedule save(Schedule schedule) {
        checkDayForExistingInSchedule(schedule);
        scheduleRepository.save(schedule);
        logger.info("Saved into db schedule for day of week: {}", schedule.getDay());
        return schedule;
    }

    @Override
    public List<Schedule> findAllBySalonId(Integer salonId) {
        return scheduleRepository.findAllBySalonId(salonId);
    }

    @Override
    public void deleteAllBySalonId(int salonId) {
        scheduleRepository.deleteAllBySalonId(salonId);
        logger.warn("Schedule for salon with id {} was deleted", salonId);
    }

    private void checkDayForExistingInSchedule(Schedule schedule) {
        int salonId = schedule.getHairdressingSalonId();
        List<Schedule> schedules = findAllBySalonId(salonId);

        if (schedules.stream().anyMatch(schedule1 -> schedule1.getDay().equals(schedule.getDay()))) {
            throw new DayDuplicationException("this day of week already exist in schedule");
        }
    }
}
