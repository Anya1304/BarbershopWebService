package com.spdu.app.hairdressing_salon.repository;

import com.spdu.app.hairdressing_salon.model.HairdressingSalon;

import java.util.List;
import java.util.Optional;

public interface HairdressingSalonRepository {
    HairdressingSalon save(HairdressingSalon hairdressingSalon);

    Optional<HairdressingSalon> findById(int id);

    List<HairdressingSalon> findAll();

    void deleteById(int id);
}