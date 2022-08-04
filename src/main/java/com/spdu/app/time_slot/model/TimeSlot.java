package com.spdu.app.time_slot.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class TimeSlot {

    private int id;

    private LocalDate day;

    private LocalTime timeStart;

    private LocalTime timeEnd;

    private Integer employeeId;

    private Integer orderId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSlot timeSlot = (TimeSlot) o;
        return id == timeSlot.id
                && Objects.equals(day, timeSlot.day)
                && Objects.equals(timeStart, timeSlot.timeStart)
                && Objects.equals(timeEnd, timeSlot.timeEnd)
                && Objects.equals(employeeId, timeSlot.employeeId)
                && Objects.equals(orderId, timeSlot.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, timeStart, timeEnd, employeeId, orderId);
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "id=" + id +
                ", day=" + day +
                ", timeStart=" + timeStart +
                ", timeEnd=" + timeEnd +
                ", employeeId=" + employeeId +
                ", orderId=" + orderId +
                '}';
    }
}
