package com.spdu.app.service;

import com.spdu.app.schedule.model.Schedule;
import com.spdu.app.schedule.repository.ScheduleRepository;
import com.spdu.app.schedule.service.ScheduleServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    @Test
    public void whenAddScheduleThenShouldCallRepositoryMethod() {
        //GIVEN
        Schedule schedule = createSchedule(1, LocalTime.of(8, 0),
                LocalTime.of(21, 0), DayOfWeek.MONDAY);
        //WHEN
        scheduleService.save(schedule);
        //THEN
        verify(scheduleRepository).save(schedule);
    }

    @Test
    public void whenAddExistingDayInScheduleThenShouldThrowException() {
        //GIVEN
        Schedule schedule1 = createSchedule(1, LocalTime.of(8, 0),
                LocalTime.of(21, 0), DayOfWeek.MONDAY);
        Schedule schedule2 = createSchedule(2, LocalTime.of(8, 0),
                LocalTime.of(21, 0), DayOfWeek.TUESDAY);
        Schedule schedule3 = createSchedule(3, LocalTime.of(8, 0),
                LocalTime.of(21, 0), DayOfWeek.TUESDAY);
        given(scheduleRepository.findAllBySalonId(1)).willReturn(List.of(schedule1, schedule2));
        //THEN
        assertThatThrownBy(() -> scheduleService.save(schedule3))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("this day of week already exist in schedule");
    }

    @Test
    public void whenFindAllScheduleByIdThenShouldCallRepositoryMethod() {
        //GIVEN
        int salonId = 1;
        //WHEN
        scheduleService.findAllBySalonId(salonId);
        //THEN
        verify(scheduleRepository).findAllBySalonId(salonId);
    }

    @Test
    public void whenDeleteScheduleThenShouldCallRepositoryMethod() {
        //GIVEN
        int salonId = 1;
        //WHEN
        scheduleService.deleteAllBySalonId(salonId);
        //THEN
        verify(scheduleRepository).deleteAllBySalonId(salonId);
    }

    @NotNull
    private Schedule createSchedule(int id, LocalTime timeFrom, LocalTime timeTo,
                                    DayOfWeek day) {
        Schedule schedule1 = new Schedule();
        schedule1.setId(id);
        schedule1.setTimeFrom(timeFrom);
        schedule1.setTimeTo(timeTo);
        schedule1.setDay(day);
        schedule1.setHairdressingSalonId(1);
        return schedule1;
    }
}
