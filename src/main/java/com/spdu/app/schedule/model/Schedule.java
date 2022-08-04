package com.spdu.app.schedule.model;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "time_from")
    private LocalTime timeFrom;

    @Column(name = "time_to")
    private LocalTime timeTo;

    @Column(name = "day")
    private DayOfWeek day;

    @Column(name = "hairdressing_salon_id")
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
        Schedule schedule = (Schedule) o;
        return id == schedule.id
                && timeFrom == schedule.timeFrom
                && timeTo == schedule.timeTo
                && hairdressingSalonId == schedule.hairdressingSalonId
                && Objects.equals(day, schedule.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeFrom, timeTo, day, hairdressingSalonId);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", timeFrom=" + timeFrom +
                ", timeTo=" + timeTo +
                ", day='" + day + '\'' +
                ", hairdressingSalonId=" + hairdressingSalonId +
                '}';
    }
}
