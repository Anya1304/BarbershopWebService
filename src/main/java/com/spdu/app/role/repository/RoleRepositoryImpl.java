package com.spdu.app.role.repository;

import com.spdu.app.role.Role;
import com.spdu.app.user.model.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    protected NamedParameterJdbcTemplate jdbcTemplate;

    public RoleRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user, Role role) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("userId", user.getId());
        parameterSource.addValue("roleName", role.toString());
        String query = "INSERT INTO user_role (role_name,user_id) VALUES (:roleName, :userId)";

        jdbcTemplate.update(query, parameterSource);
    }

    @Override
    public List<Role> findAllByUserId(Integer userId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("userId", userId);
        String query = "SELECT role_name FROM user_role LEFT OUTER JOIN users u on u.id = user_role.user_id " +
                "WHERE user_id = :userId";
        return jdbcTemplate.query(query, parameterSource, rs -> {
            List<Role> roleList = new ArrayList<>();
            while (rs.next()) {
                roleList.add(Role.valueOf(rs.getString("role_name")));
            }
            return roleList;
        });
    }
}
