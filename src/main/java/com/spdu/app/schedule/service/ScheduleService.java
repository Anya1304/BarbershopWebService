package com.spdu.app.schedule.service;

import com.spdu.app.schedule.model.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule save(Schedule obj);

    List<Schedule> findAllBySalonId(Integer salonId);

    void deleteAllBySalonId(int salonId);
}
