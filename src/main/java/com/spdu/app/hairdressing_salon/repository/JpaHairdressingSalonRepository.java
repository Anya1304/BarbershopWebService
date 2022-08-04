package com.spdu.app.hairdressing_salon.repository;

import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile(value = "jpa")
public interface JpaHairdressingSalonRepository extends CrudRepository<HairdressingSalon, Long>,
        HairdressingSalonRepository {

}