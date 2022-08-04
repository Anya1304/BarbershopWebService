package com.spdu.app.user.service;

import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.user.model.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User get(int id);

    List<User> getList();

    List<User> getUsersBySalon(HairdressingSalon salon);

    List<User> findCustomersBySalonId(Integer salonId);

    void delete(int id);
}
