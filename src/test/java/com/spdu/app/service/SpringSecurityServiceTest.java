package com.spdu.app.service;

import com.spdu.app.confirmation_token.model.ConfirmationToken;
import com.spdu.app.confirmation_token.repository.TokenRepository;
import com.spdu.app.spring_security.service.SecurityUserDetailsService;
import com.spdu.app.user.model.User;
import com.spdu.app.user.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SpringSecurityServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private SecurityUserDetailsService securityService;

    @Test
    public void whenFindUserByDoesntExistingUsernameThenShouldThrowException() {
        //THEN
        assertThatThrownBy(() -> securityService.loadUserByUsername(""))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not present");
    }

    @Test
    public void whenFindUserByExistingUsernameThenShouldCallRepositoryMethod() {
        //GIVEN
        User user = createUser();
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(tokenRepository.findByUserId(user.getId())).willReturn(new ConfirmationToken());
        //WHEN
        securityService.loadUserByUsername(user.getUsername());
        //THEN
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    public void whenFindUserByExistingUsernameThenShouldCallTokenRepositoryMethod() {
        //GIVEN
        User user = createUser();
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(tokenRepository.findByUserId(user.getId())).willReturn(new ConfirmationToken());
        //WHEN
        securityService.loadUserByUsername(user.getUsername());
        //THEN
        verify(tokenRepository).findByUserId(user.getId());
    }

    @NotNull
    private User createUser() {
        User user = new User();
        user.setId(1);
        user.setUsername("test");
        return user;
    }
}
