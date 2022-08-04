package com.spdu.app.confirmation_token.service;

import com.spdu.app.confirmation_token.model.ConfirmationToken;
import com.spdu.app.confirmation_token.repository.TokenRepository;
import com.spdu.app.mail_sending.MailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final MailService mailService;

    public TokenServiceImpl(TokenRepository tokenRepository,
                            MailService mailService) {
        this.tokenRepository = tokenRepository;
        this.mailService = mailService;
    }

    @Override
    public void save(ConfirmationToken confirmationToken) {
        tokenRepository.save(confirmationToken);
    }

    @Override
    public ConfirmationToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public ConfirmationToken findByUserId(Integer userId) {
        return tokenRepository.findByUserId(userId);
    }

    @Override
    public void updateConfirmColumnSetTrue(ConfirmationToken token) {
        tokenRepository.updateConfirmColumnSetTrue(token);
        mailService.greetingNewUser(token.getUser());
    }

    @Override
    public List<ConfirmationToken> findAllNonConfirmTokens() {
        return tokenRepository.findAllNonConfirmTokens();
    }

    @Override
    public void delete(ConfirmationToken token) {
        tokenRepository.delete(token);
    }
}
