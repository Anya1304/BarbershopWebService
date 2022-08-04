package com.spdu.app.hairdressing_salon.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class HairdressingSalonDto {
    private int id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, message = "name should have at least 2 characters")
    private String name;

    @NotBlank(message = "Email is mandatory")
    private String email;

    private String description;

    @NotEmpty(message = "Address cannot be empty")
    private String address;

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

