package com.spdu.app.hairdressing_salon.service;

import com.spdu.app.hairdressing_salon.model.HairdressingSalon;

import java.util.List;

public interface HairdressingSalonService {
    HairdressingSalon save(HairdressingSalon hairdressingSalon);

    HairdressingSalon get(int id);

    List<HairdressingSalon> getList();

    void delete(int id);
}
