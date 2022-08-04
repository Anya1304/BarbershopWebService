package com.spdu.app.mapper;

import com.spdu.app.role.Role;
import com.spdu.app.user.mapper.UserExtractor;
import com.spdu.app.user.model.User;
import org.h2.tools.SimpleResultSet;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    private UserExtractor userExtractor;

    @BeforeEach
    void setUp() {
        userExtractor = new UserExtractor();
    }

    @Test
    public void whenExtractDataOfResultSetThenShouldCreateUser() throws Exception {
        //GIVEN
        List<Role> roles = createRoleList();
        User userExpected = createUser(1, "test", "test@gmail.com", "test", null, roles);
        SimpleResultSet rs = createResultSet();
        for (Role role : roles) {
            rs.addRow(userExpected.getId(), userExpected.getUsername(),
                    userExpected.getPassword(), userExpected.getEmail(),
                    userExpected.getSalonId(), role.name());
        }
        //WHEN
        List<User> userList = userExtractor.extractData(rs);
        //THEN
        assertThat(userList).isNotNull();
        Optional<User> userActual = userList.stream().findFirst();
        assertThat(userActual).isPresent();
        assertThat(userActual.get()).isEqualTo(userExpected);
    }

    @NotNull
    private User createUser(int id, String username, String email, String password, Integer salonId, List<Role> roles) {
        User userExpected = new User();
        userExpected.setId(id);
        userExpected.setUsername(username);
        userExpected.setEmail(email);
        userExpected.setPassword(password);
        userExpected.setSalonId(salonId);
        userExpected.setRoleList(roles);
        return userExpected;
    }

    @Test
    public void whenExtractDataOfResultSetThenShouldReturnListOfUsers() throws Exception {
        //GIVEN
        List<Role> roles = List.of(Role.USER, Role.ADMIN);
        User userExpected1 = createUser(1, "test1", "test1@gmail.com",
                "test1", null, roles);
        User userExpected2 = createUser(2, "test2", "test2@gmail.com",
                "test2", null, roles);
        User userExpected3 = createUser(3, "test3", "test3@gmail.com",
                "test3", null, roles);
        List<User> usersExpected = List.of(userExpected1, userExpected2, userExpected3);
        SimpleResultSet rs = createResultSet();
        for (User user : usersExpected) {
            for (Role role : roles) {
                rs.addRow(user.getId(), user.getUsername(),
                        user.getPassword(), user.getEmail(),
                        user.getSalonId(), role.name());
            }
        }
        //WHEN
        List<User> usersActual = userExtractor.extractData(rs);
        //THEN
        assertThat(usersActual).isNotNull();
        assertThat(usersActual).isEqualTo(usersExpected);
    }

    @NotNull
    private SimpleResultSet createResultSet() {
        SimpleResultSet rs = new SimpleResultSet();

        rs.addColumn("id", Types.INTEGER, 0, 0);
        rs.addColumn("username", Types.VARCHAR, 0, 0);
        rs.addColumn("password", Types.VARCHAR, 0, 0);
        rs.addColumn("email", Types.VARCHAR, 0, 0);
        rs.addColumn("salon_id", Types.VARCHAR, 0, 0);
        rs.addColumn("role_name", Types.INTEGER, 0, 0);
        return rs;
    }

    @NotNull
    private List<Role> createRoleList() {
        return List.of(Role.USER, Role.ADMIN, Role.EMPLOYEE);
    }
}
