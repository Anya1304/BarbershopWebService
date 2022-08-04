package com.spdu.app.spring_security.service;

import com.spdu.app.confirmation_token.model.ConfirmationToken;
import com.spdu.app.confirmation_token.repository.TokenRepository;
import com.spdu.app.spring_security.SecurityUser;
import com.spdu.app.user.model.User;
import com.spdu.app.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(SecurityUserDetailsService.class);
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public SecurityUserDetailsService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present"));
        ConfirmationToken token = tokenRepository.findByUserId(user.getId());
        logger.info("Try to authorize user with id {}", user.getId());
        return new SecurityUser(user, token.isConfirm());
    }
}