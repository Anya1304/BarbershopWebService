package com.spdu.app.user.mapper;

import com.spdu.app.role.Role;
import com.spdu.app.user.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();
        User user = null;

        while (rs.next()) {
            int id = rs.getInt("id");
            if (users.isEmpty() || !Objects.equals(users.get(users.size() - 1).getId(), id)) {
                user = createUser(rs);
                users.add(user);
            } else {
                user.getRoleList().add(Role.valueOf(rs.getString("role_name")));
            }
        }
        return users;
    }

    private User createUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getObject("id", Integer.class));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setSalonId(rs.getObject("salon_id", Integer.class));
        if (user.getRoleList() == null) {
            user.setRoleList(new ArrayList<>());
        }
        user.getRoleList().add(Role.valueOf(rs.getString("role_name")));

        return user;
    }
}
