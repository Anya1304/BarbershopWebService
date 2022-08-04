package com.spdu.app.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

public class ScheduleDto {

    private int id;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime timeFrom;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime timeTo;

    @NotNull
    private DayOfWeek day;

    @NotNull
    private Integer hairdressingSalonId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Integer getHairdressingSalonId() {
        return hairdressingSalonId;
    }

    public void setHairdressingSalonId(Integer hairdressingSalonId) {
        this.hairdressingSalonId = hairdressingSalonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScheduleDto that = (ScheduleDto) o;
        return id == that.id
                && hairdressingSalonId == that.hairdressingSalonId
                && Objects.equals(timeFrom, that.timeFrom)
                && Objects.equals(timeTo, that.timeTo)
                && day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeFrom, timeTo, day, hairdressingSalonId);
    }
}
