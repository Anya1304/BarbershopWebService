package com.spdu.app.schedule.repository;

import com.spdu.app.schedule.mapper.ScheduleMapper;
import com.spdu.app.schedule.model.Schedule;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
@Profile(value = "jdbc")
public class JdbcScheduleRepository implements ScheduleRepository {

    protected ScheduleMapper scheduleMapper;
    protected NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcScheduleRepository(ScheduleMapper scheduleMapper,
                                  NamedParameterJdbcTemplate jdbcTemplate) {
        this.scheduleMapper = scheduleMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Schedule save(Schedule schedule) {
        MapSqlParameterSource parameterSource = getMapSqlParameterSource(schedule);
        String query = "INSERT INTO schedule (time_from, time_to, day,hairdressing_salon_id) " +
                "VALUES (:timeFrom, :timeTo, :day, :salonId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, parameterSource, keyHolder, new String[]{"id"});

        schedule.setId(keyHolder.getKey().intValue());
        return schedule;
    }

    @Override
    public Schedule findById(int id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("scheduleId", id);
        String query = "SELECT * FROM schedule WHERE id = :scheduleId";
        return jdbcTemplate.queryForObject(query, parameterSource, scheduleMapper);
    }

    @Override
    public List<Schedule> findAllBySalonId(Integer id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("salonId", id);
        String query = "SELECT * FROM schedule WHERE hairdressing_salon_id = :salonId";
        return jdbcTemplate.query(query, parameterSource, scheduleMapper);
    }

    @Override
    public void deleteAllBySalonId(int salonId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("salonId", salonId);
        String query = "DELETE FROM schedule WHERE hairdressing_salon_id = :salonId";
        jdbcTemplate.update(query, parameterSource);
    }

    @Override
    public void updateWorkingHoursById(int id, int salonId, LocalTime timeFrom, LocalTime timeTo) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        parameterSource.addValue("timeFrom", timeFrom);
        parameterSource.addValue("timeTo", timeTo);
        parameterSource.addValue("salonId", salonId);
        String query = "UPDATE schedule SET time_from = :timeFrom, time_to = :timeTo WHERE id = :id AND hairdressing_salon_id = :salonId";

        jdbcTemplate.update(query, parameterSource);
    }

    private MapSqlParameterSource getMapSqlParameterSource(Schedule schedule) {
        return new MapSqlParameterSource()
                .addValue("timeFrom", schedule.getTimeFrom())
                .addValue("timeTo", schedule.getTimeTo())
                .addValue("day", schedule.getDay().toString())
                .addValue("salonId", schedule.getHairdressingSalonId());
    }
}
