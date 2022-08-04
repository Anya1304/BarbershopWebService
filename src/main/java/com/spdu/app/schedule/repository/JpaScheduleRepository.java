package com.spdu.app.schedule.repository;

import com.spdu.app.schedule.model.Schedule;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Profile(value = "jpa")
public interface JpaScheduleRepository extends CrudRepository<Schedule, Long>, ScheduleRepository {
    @Query(value = "SELECT sch FROM Schedule sch WHERE sch.hairdressingSalonId = :salonId")
    List<Schedule> findAllBySalonId(@Param("salonId") Integer id);
}