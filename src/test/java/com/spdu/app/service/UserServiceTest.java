package com.spdu.app.service;

import com.spdu.app.confirmation_token.service.TokenService;
import com.spdu.app.mail_sending.MailService;
import com.spdu.app.role.Role;
import com.spdu.app.role.service.RoleService;
import com.spdu.app.schedule.service.ScheduleService;
import com.spdu.app.time_slot.service.TimeSlotService;
import com.spdu.app.user.excepton.UserDontExistException;
import com.spdu.app.user.model.User;
import com.spdu.app.user.repository.UserRepository;
import com.spdu.app.user.service.UserServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleService roleService;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private TimeSlotService timeSlotService;
    @Mock
    private TokenService tokenService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private MailService mailService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void whenGetUserExistingByIdThenShouldReturnUser() {
        User testUser = createUser();
        testUser.setRoleList(List.of(Role.USER, Role.ADMIN));
        given(userRepository.findById(1)).willReturn(java.util.Optional.of(testUser));

        User user = userService.get(1);
        assertThat(user.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(user.getId()).isEqualTo(testUser.getId());
    }

    @Test
    public void whenGetNonExistingUserThenShouldReturnException() {
        given(userRepository.findById(anyInt())).willThrow(UserDontExistException.class);
        assertThatThrownBy(() -> userService.get(1)).isInstanceOf(UserDontExistException.class);
    }

    @Test
    public void whenSaveUserThenShouldReturnSameUser() {
        User testUser = createUser();
        given(userRepository.save(testUser)).willReturn(testUser);
        User user = userService.save(testUser);
        assertThat(user.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(user.getPassword()).isEqualTo(testUser.getPassword());
        verify(roleService).saveWithDefaultRole(testUser);
    }

    @Test
    public void whenSaveUserWithExistingUsernameOrEmailThenShouldThrownException() {
        //GIVEN
        User testUser = createUser();

        //WHEN
        given(userRepository.findByUsername(anyString())).willReturn(java.util.Optional.of(new User()));

        //THEN
        assertThatThrownBy(() -> userService.save(testUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User with this username already exist");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByUsername(captor.capture());
        assertThat(captor.getValue()).isEqualTo("test");
    }

    @Test
    public void whenSaveUserWithExistingEmailThenShouldThrownException() {
        //GIVEN
        User testUser = createUser();

        //WHEN
        given(userRepository.findByEmail(anyString())).willReturn(java.util.Optional.of(new User()));

        //THEN
        assertThatThrownBy(() -> userService.save(testUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User with this email already exist");

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail(captor.capture());
        assertThat(captor.getValue()).isEqualTo("test@test.com");
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
