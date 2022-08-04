package com.spdu.app.intTests.controller.repository;

import com.spdu.app.hairdressing_salon.mapper.HairdressingSalonMapper;
import com.spdu.app.hairdressing_salon.repository.JdbcHairdressingSalonRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class SalonTestRepository extends JdbcHairdressingSalonRepository {
    public SalonTestRepository(HairdressingSalonMapper hairdressingSalonMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        super(hairdressingSalonMapper, jdbcTemplate);
    }

    public void deleteAll() {
        String deleteSalons = "DELETE FROM hairdressing_salon";
        jdbcTemplate.update(deleteSalons, new EmptySqlParameterSource());
    }
}
