package com.spdu.app.service;

import com.spdu.app.role.Role;
import com.spdu.app.role.repository.RoleRepository;
import com.spdu.app.role.service.RoleServiceImpl;
import com.spdu.app.user.model.User;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    public void whenAddRoleForNewUserThenShouldCreateListOfRolesWithUserRole() {
        //GIVEN
        User user = createUser();
        //WHEN
        roleService.saveWithDefaultRole(user);
        //THEN
        verify(roleRepository).save(user, Role.USER);
        assertThat(user.getRoleList()).isNotNull();
    }

    @Test
    public void whenAddRoleForExistingUserThenShouldAddNewRoleToList() {
        //GIVEN
        User user = createUser();
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        user.setRoleList(roles);
        //WHEN
        roleService.save(user, Role.ADMIN);
        //THEN
        verify(roleRepository).save(user, Role.ADMIN);
        assertThat(user.getRoleList()).contains(Role.ADMIN);
    }

    @Test
    public void whenGetAllRolesByUserIdThenShouldCallRepositoryMethod() {
        //GIVEN
        int userId = 2;
        //WHEN
        roleService.findAllByUserId(userId);
        //THEN
        verify(roleRepository).findAllByUserId(userId);
    }

    @NotNull
    private User createUser() {
        User testUser = new User();
        testUser.setId(1);
        testUser.setUsername("test");
        testUser.setPassword("test");
        testUser.setEmail("test@test.com");
        return testUser;
    }
}
