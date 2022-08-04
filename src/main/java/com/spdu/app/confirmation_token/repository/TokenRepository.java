package com.spdu.app.confirmation_token.repository;

import com.spdu.app.confirmation_token.model.ConfirmationToken;

import java.util.List;

public interface TokenRepository {
    void save(ConfirmationToken confirmationToken);

    ConfirmationToken findByToken(String token);

    ConfirmationToken findByUserId(Integer userId);

    void updateConfirmColumnSetTrue(ConfirmationToken token);

    List<ConfirmationToken> findAllNonConfirmTokens();

    void delete(ConfirmationToken token);
}
