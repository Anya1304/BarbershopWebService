package com.spdu.app.role.service;

import com.spdu.app.role.Role;
import com.spdu.app.role.repository.RoleRepository;
import com.spdu.app.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(User user, Role role) {
        roleRepository.save(user, role);
        createRoleListOrElseAddRole(user, role);
        logger.info("Saved role {} of user with id {} into db", role.name(), user.getId());
    }

    @Override
    public void saveWithDefaultRole(User user) {
        save(user, Role.USER);
    }

    @Override
    public List<Role> findAllByUserId(Integer userId) {
        return roleRepository.findAllByUserId(userId);
    }

    private void createRoleListOrElseAddRole(User user, Role role) {
        if (user.getRoleList() == null) {
            List<Role> roleList = new ArrayList<>();
            roleList.add(role);
            user.setRoleList(roleList);
        } else {
            user.getRoleList().add(role);
        }
    }
}
