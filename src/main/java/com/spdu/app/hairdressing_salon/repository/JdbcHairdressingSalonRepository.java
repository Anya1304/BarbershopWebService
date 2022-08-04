package com.spdu.app.hairdressing_salon.repository;

import com.spdu.app.hairdressing_salon.mapper.HairdressingSalonMapper;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile(value = "jdbc")
public class JdbcHairdressingSalonRepository implements HairdressingSalonRepository {

    protected HairdressingSalonMapper hairdressingSalonMapper;
    protected NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcHairdressingSalonRepository(HairdressingSalonMapper hairdressingSalonMapper,
                                           NamedParameterJdbcTemplate jdbcTemplate) {
        this.hairdressingSalonMapper = hairdressingSalonMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HairdressingSalon save(HairdressingSalon hairdressingSalon) {
        MapSqlParameterSource parameterSource = getMapSqlParameterSource(hairdressingSalon);
        String query = "INSERT INTO hairdressing_salon (name, email, description, address) " +
                "VALUES (:name, :email, :description, :address)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, parameterSource, keyHolder, new String[]{"id"});

        hairdressingSalon.setId(keyHolder.getKey().intValue());
        return hairdressingSalon;
    }

    @Override
    public Optional<HairdressingSalon> findById(int id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        String query = "SELECT * FROM hairdressing_salon WHERE id = :id";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, parameterSource, hairdressingSalonMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<HairdressingSalon> findAll() {
        String query = "SELECT * FROM hairdressing_salon";
        return jdbcTemplate.query(query, hairdressingSalonMapper);
    }

    @Override
    public void deleteById(int id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        String query = "DELETE FROM hairdressing_salon WHERE id = :id";
        jdbcTemplate.update(query, parameterSource);
    }

    private MapSqlParameterSource getMapSqlParameterSource(HairdressingSalon hairdressingSalon) {
        return new MapSqlParameterSource()
                .addValue("name", hairdressingSalon.getName())
                .addValue("email", hairdressingSalon.getEmail())
                .addValue("description", hairdressingSalon.getDescription())
                .addValue("address", hairdressingSalon.getAddress());
    }
}
