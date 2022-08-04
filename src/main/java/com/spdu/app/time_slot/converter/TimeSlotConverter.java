package com.spdu.app.time_slot.converter;

import com.spdu.app.time_slot.dto.TimeSlotDto;
import com.spdu.app.time_slot.model.TimeSlot;

import java.util.List;

public final class TimeSlotConverter {

    private TimeSlotConverter() {
    }

    public static TimeSlotDto toDto(TimeSlot timeSlot) {
        TimeSlotDto timeSlotDto = new TimeSlotDto();

        timeSlotDto.setId(timeSlot.getId());
        timeSlotDto.setDay(timeSlot.getDay());
        timeSlotDto.setTimeStart(timeSlot.getTimeStart());
        timeSlotDto.setTimeEnd(timeSlot.getTimeEnd());
        timeSlotDto.setEmployeeId(timeSlot.getEmployeeId());
        timeSlotDto.setOrderId(timeSlot.getOrderId());

        return timeSlotDto;
    }

    public static TimeSlot fromDto(TimeSlotDto timeSlotDto) {
        TimeSlot timeSlot = new TimeSlot();

        timeSlot.setId(timeSlotDto.getId());
        timeSlot.setDay(timeSlotDto.getDay());
        timeSlot.setTimeStart(timeSlotDto.getTimeStart());
        timeSlot.setTimeEnd(timeSlotDto.getTimeEnd());
        timeSlot.setEmployeeId(timeSlotDto.getEmployeeId());
        timeSlot.setOrderId(timeSlotDto.getOrderId());

        return timeSlot;
    }

    public static List<TimeSlotDto> toDto(List<TimeSlot> timeSlotList) {
        return timeSlotList.stream()
                .map(TimeSlotConverter::toDto)
                .toList();
    }
}

