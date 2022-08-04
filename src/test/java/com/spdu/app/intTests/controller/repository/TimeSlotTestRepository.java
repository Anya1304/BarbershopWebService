package com.spdu.app.intTests.controller.repository;

import com.spdu.app.time_slot.mapper.TimeSlotMapper;
import com.spdu.app.time_slot.repository.JdbcTimeSlotRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class TimeSlotTestRepository extends JdbcTimeSlotRepository {
    public TimeSlotTestRepository(TimeSlotMapper timeSlotMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        super(timeSlotMapper, jdbcTemplate);
    }

    public void deleteAll() {
        String query = "DELETE FROM time_slot";
        jdbcTemplate.update(query, new EmptySqlParameterSource());
    }
}
