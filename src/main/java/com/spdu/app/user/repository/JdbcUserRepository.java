package com.spdu.app.user.repository;

import com.spdu.app.user.mapper.UserExtractor;
import com.spdu.app.user.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
@Profile(value = "jdbc")
public class JdbcUserRepository implements UserRepository {

    protected NamedParameterJdbcTemplate jdbcTemplate;
    private final UserExtractor userExtractor;

    public JdbcUserRepository(NamedParameterJdbcTemplate jdbcTemplate, UserExtractor userExtractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.userExtractor = userExtractor;
    }

    @Override
    public User save(User user) {
        MapSqlParameterSource parameterSource = getMapSqlParameterSource(user);
        String query = "INSERT INTO users (username, password, email, salon_id) " +
                "VALUES (:username, :password,:email , :salonId)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, parameterSource, keyHolder, new String[]{"id"});
        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        String query = "SELECT * FROM users INNER JOIN user_role ur on users.id = ur.user_id where id = :id";
        try {
            return Optional.of(jdbcTemplate.query(query, parameterSource, new UserExtractor())
                    .stream()
                    .findFirst()
                    .orElseThrow());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users INNER JOIN user_role ur on users.id = ur.user_id ORDER BY users.id";
        return jdbcTemplate.query(query, userExtractor);
    }

    @Override
    public List<User> findBySalonId(Integer salonId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("salonId", salonId);
        String query = "SELECT * FROM users INNER JOIN user_role ur on users.id = ur.user_id  WHERE salon_id = :salonId";
        return jdbcTemplate.query(query, parameterSource, userExtractor);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("username", username);
        String query = "SELECT * FROM users INNER JOIN user_role ur on users.id = ur.user_id  WHERE username = :username";
        try {
            return Optional.of(jdbcTemplate.query(query, parameterSource, new UserExtractor())
                    .stream()
                    .findFirst()
                    .get());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("email", email);
        String query = "SELECT * FROM users INNER JOIN user_role ur on users.id = ur.user_id  WHERE users.email = :email";
        try {
            return Optional.of(jdbcTemplate.query(query, parameterSource, new UserExtractor())
                    .stream()
                    .findFirst()
                    .get());
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findCustomersBySalonId(Integer salonId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("salonId", salonId);
        String query = "SELECT customer.id, customer.username, customer.password, customer.email, customer.salon_id, ur.role_name FROM time_slot " +
                "INNER JOIN orders ord on ord.id = time_slot.order_id " +
                "LEFT OUTER JOIN users customer on customer.id = ord.user_id " +
                "INNER JOIN user_role ur on customer.id = ur.user_id " +
                "LEFT OUTER JOIN users employee on employee.id = time_slot.employee_id " +
                "WHERE employee.salon_id = :salonId";
        return jdbcTemplate.query(query, parameterSource, userExtractor);
    }

    @Override
    public void deleteById(int id) {
        jdbcTemplate.getJdbcOperations().batchUpdate(
                "DELETE FROM user_role WHERE user_id = " + id,
                "DELETE FROM users WHERE id = " + id);
    }

    private MapSqlParameterSource getMapSqlParameterSource(User user) {
        return new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("password", user.getPassword())
                .addValue("email", user.getEmail())
                .addValue("salonId", user.getSalonId());
    }
}
