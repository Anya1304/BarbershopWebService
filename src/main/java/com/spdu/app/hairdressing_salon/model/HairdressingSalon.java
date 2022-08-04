package com.spdu.app.hairdressing_salon.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "hairdressing_salon")
public class HairdressingSalon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HairdressingSalon that = (HairdressingSalon) o;
        return id == that.id
                && name.equals(that.name)
                && email.equals(that.email)
                && Objects.equals(description, that.description)
                && address.equals(that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, description, address);
    }

    @Override
    public String toString() {
        return "HairdressingSalon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

