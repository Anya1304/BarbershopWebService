package com.spdu.app.time_slot.repository;

import com.spdu.app.time_slot.mapper.TimeSlotMapper;
import com.spdu.app.time_slot.model.TimeSlot;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("jdbc")
public class JdbcTimeSlotRepository implements TimeSlotRepository {

    protected TimeSlotMapper timeSlotMapper;
    protected NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTimeSlotRepository(TimeSlotMapper timeSlotMapper,
                                  NamedParameterJdbcTemplate jdbcTemplate) {
        this.timeSlotMapper = timeSlotMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveIntoTimeSlot(int orderId, int slotId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("slotId", slotId);
        parameterSource.addValue("orderId", orderId);

        String query = "UPDATE time_slot SET order_id = :orderId WHERE id = :slotId";
        jdbcTemplate.update(query, parameterSource);
    }

    @Override
    public TimeSlot save(TimeSlot timeSlot) {
        MapSqlParameterSource parameterSource = getMapSqlParameterSource(timeSlot);
        String query = "INSERT INTO time_slot (day, time_start, time_end,employee_id) " +
                "VALUES (:day, :timeStart, :timeEnd, :employeeId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, parameterSource, keyHolder, new String[]{"id"});
        timeSlot.setId(keyHolder.getKey().intValue());

        return timeSlot;
    }

    @Override
    public TimeSlot findById(Integer id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        String query = "SELECT * FROM time_slot WHERE id = :id";
        return jdbcTemplate.queryForObject(query, parameterSource, timeSlotMapper);
    }

    @Override
    public List<TimeSlot> findAllByUserId(Integer userId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("employeeId", userId);
        String query = "SELECT * FROM time_slot" +
                " WHERE employee_id = :employeeId ORDER BY  time_slot.day";

        return jdbcTemplate.query(query, parameterSource, timeSlotMapper);
    }

    @Override
    public void deleteById(int id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("employeeId", id);
        String query = "DELETE FROM time_slot WHERE employee_id = :employeeId";
        jdbcTemplate.update(query, parameterSource);
    }

    private MapSqlParameterSource getMapSqlParameterSource(TimeSlot timeSlot) {
        return new MapSqlParameterSource()
                .addValue("day", timeSlot.getDay())
                .addValue("timeStart", timeSlot.getTimeStart())
                .addValue("timeEnd", timeSlot.getTimeEnd())
                .addValue("employeeId", timeSlot.getEmployeeId());
    }
}
