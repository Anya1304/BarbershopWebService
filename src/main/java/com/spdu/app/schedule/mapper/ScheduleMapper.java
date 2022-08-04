package com.spdu.app.schedule.mapper;

import com.spdu.app.schedule.model.Schedule;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Component
public class ScheduleMapper implements RowMapper<Schedule> {

    @Override
    public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
        Schedule schedule = new Schedule();
        schedule.setId(rs.getInt("id"));

        String timeFrom = rs.getString("time_from");
        if (timeFrom != null) {
            schedule.setTimeFrom(LocalTime.parse(timeFrom));
        }

        String timeTo = rs.getString("time_to");
        if (timeTo != null) {
            schedule.setTimeTo(LocalTime.parse(timeTo));
        }
        String dayOfWeek = rs.getString("day");
        schedule.setDay(DayOfWeek.valueOf(dayOfWeek));

        schedule.setHairdressingSalonId(rs.getObject("hairdressing_salon_id", Integer.class));

        return schedule;
    }
}
