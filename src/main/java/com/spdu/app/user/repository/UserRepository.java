package com.spdu.app.user.repository;

import com.spdu.app.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(int id);

    List<User> findAll();

    List<User> findBySalonId(Integer salonId);

    List<User> findCustomersBySalonId(Integer salonId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    void deleteById(int id);
}
