package com.spdu.app.intTests.controller.repository;

import com.spdu.app.schedule.mapper.ScheduleMapper;
import com.spdu.app.schedule.repository.JdbcScheduleRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class ScheduleTestRepository extends JdbcScheduleRepository {
    public ScheduleTestRepository(ScheduleMapper scheduleMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        super(scheduleMapper, jdbcTemplate);
    }

    public void deleteAll() {
        String query = "DELETE FROM schedule";
        jdbcTemplate.update(query, new EmptySqlParameterSource());
    }
}
