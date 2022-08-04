package com.spdu.app.confirmation_token.service;

import com.spdu.app.confirmation_token.model.ConfirmationToken;

import java.util.List;

public interface TokenService {
    void save(ConfirmationToken confirmationToken);

    ConfirmationToken findByToken(String token);

    ConfirmationToken findByUserId(Integer userId);

    void updateConfirmColumnSetTrue(ConfirmationToken token);

    List<ConfirmationToken> findAllNonConfirmTokens();

    void delete(ConfirmationToken token);
}
