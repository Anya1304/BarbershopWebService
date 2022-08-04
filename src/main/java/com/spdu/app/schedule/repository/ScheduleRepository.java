package com.spdu.app.schedule.repository;

import com.spdu.app.schedule.model.Schedule;

import java.time.LocalTime;
import java.util.List;

public interface ScheduleRepository {
    Schedule save(Schedule obj);

    Schedule findById(int id);

    List<Schedule> findAllBySalonId(Integer salonId);

    void deleteAllBySalonId(int salonId);

    void updateWorkingHoursById(int id, int salonId, LocalTime timeFrom, LocalTime timeTo);
}
