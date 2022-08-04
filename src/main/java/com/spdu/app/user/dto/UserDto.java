package com.spdu.app.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {

    private int id;

    @NotBlank
    @Size(min = 2, message = "user name should have at least 2 characters")
    private String username;

    @NotBlank
    @Size(min = 5, message = "the password should have at least 5 characters")
    @Size(max = 20, message = "the password must contain at most 20 characters")
    private String password;

    private Integer salonId;

    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSalonId() {
        return salonId;
    }

    public void setSalonId(Integer salonId) {
        this.salonId = salonId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

