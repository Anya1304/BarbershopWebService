package com.spdu.app.role.service;

import com.spdu.app.role.Role;
import com.spdu.app.user.model.User;

import java.util.List;

public interface RoleService {
    void save(User user, Role role);

    void saveWithDefaultRole(User user);

    List<Role> findAllByUserId(Integer userId);
}
