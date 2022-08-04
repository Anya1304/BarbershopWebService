package com.spdu.app.time_slot.mapper;

import com.spdu.app.time_slot.model.TimeSlot;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class TimeSlotMapper implements RowMapper<TimeSlot> {

    @Override
    public TimeSlot mapRow(ResultSet rs, int rowNum) throws SQLException {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(rs.getInt("id"));

        String day = rs.getString("day");
        timeSlot.setDay(LocalDate.parse(day));

        String timeStart = rs.getString("time_start");
        timeSlot.setTimeStart(LocalTime.parse(timeStart));
        String timeEnd = rs.getString("time_end");
        timeSlot.setTimeEnd(LocalTime.parse(timeEnd));

        timeSlot.setEmployeeId(rs.getObject("employee_id", Integer.class));
        timeSlot.setOrderId(rs.getObject("order_id", Integer.class));

        return timeSlot;
    }
}