package com.spdu.app.role.repository;

import com.spdu.app.role.Role;
import com.spdu.app.user.model.User;

import java.util.List;

public interface RoleRepository {

    void save(User user, Role role);

    List<Role> findAllByUserId(Integer userId);
}
