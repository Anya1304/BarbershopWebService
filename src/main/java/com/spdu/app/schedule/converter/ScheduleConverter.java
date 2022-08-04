package com.spdu.app.schedule.converter;

import com.spdu.app.schedule.dto.ScheduleDto;
import com.spdu.app.schedule.model.Schedule;

import java.util.List;

public final class ScheduleConverter {

    private ScheduleConverter() {
    }

    public static ScheduleDto toDto(Schedule schedule) {
        ScheduleDto scheduleDto = new ScheduleDto();

        scheduleDto.setId(schedule.getId());
        scheduleDto.setTimeFrom(schedule.getTimeFrom());
        scheduleDto.setTimeTo(schedule.getTimeTo());
        scheduleDto.setDay(schedule.getDay());
        scheduleDto.setHairdressingSalonId(schedule.getHairdressingSalonId());

        return scheduleDto;
    }

    public static Schedule fromDto(ScheduleDto scheduleDto) {
        Schedule schedule = new Schedule();

        schedule.setId(scheduleDto.getId());
        schedule.setDay(scheduleDto.getDay());
        schedule.setTimeFrom(scheduleDto.getTimeFrom());
        schedule.setTimeTo(scheduleDto.getTimeTo());
        schedule.setHairdressingSalonId(scheduleDto.getHairdressingSalonId());

        return schedule;
    }

    public static List<ScheduleDto> toDto(List<Schedule> scheduleList) {
        return scheduleList.stream()
                .map(ScheduleConverter::toDto)
                .toList();
    }
}

