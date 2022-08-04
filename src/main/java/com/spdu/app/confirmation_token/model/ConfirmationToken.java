package com.spdu.app.confirmation_token.model;

import com.spdu.app.user.model.User;

import java.time.LocalDate;
import java.util.UUID;

public class ConfirmationToken {
    private int tokenId;
    private String confirmationToken;
    private LocalDate createdDate;
    private boolean isConfirm;
    private User user;

    public ConfirmationToken() {
        createdDate = LocalDate.now();
    }

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = LocalDate.now();
        confirmationToken = UUID.randomUUID().toString();
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
