package com.spdu.app.intTests.controller.repository;

import com.spdu.app.user.mapper.UserExtractor;
import com.spdu.app.user.repository.JdbcUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class UserTestRepository extends JdbcUserRepository {

    public UserTestRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, new UserExtractor());
    }

    public void deleteAll() {
        String deleteUsers = "DELETE FROM users";
        jdbcTemplate.update(deleteUsers, new EmptySqlParameterSource());
    }
}
