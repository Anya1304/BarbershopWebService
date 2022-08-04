package com.spdu.app.confirmation_token;

import com.spdu.app.confirmation_token.model.ConfirmationToken;
import com.spdu.app.confirmation_token.repository.TokenRepository;
import com.spdu.app.user.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TokenExpirationDateChecker {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public TokenExpirationDateChecker(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${tokenCheck.period}")
    public void checkToken() {
        List<ConfirmationToken> tokens = tokenRepository.findAllNonConfirmTokens();
        tokens.forEach(token -> {
            if (!LocalDate.now().equals(token.getCreatedDate())) {
                userRepository.deleteById(token.getUser().getId());
            }
        });
    }
}
