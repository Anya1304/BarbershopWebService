package com.spdu.app.service;

import com.spdu.app.hairdressing_salon.exception.SalonDoesntExistException;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.hairdressing_salon.repository.HairdressingSalonRepository;
import com.spdu.app.hairdressing_salon.service.HairdressingSalonServiceImpl;
import com.spdu.app.mail_sending.MailService;
import com.spdu.app.schedule.model.Schedule;
import com.spdu.app.schedule.service.ScheduleService;
import com.spdu.app.user.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SalonServiceTest {
    @Mock
    private HairdressingSalonRepository salonRepository;
    @Mock
    private ScheduleService scheduleService;
    @Mock
    private MailService mailService;
    @Mock
    private UserService userService;

    @InjectMocks
    private HairdressingSalonServiceImpl salonService;

    @Test
    public void whenAddSalonThenShouldCallRepositoryMethod() {
        //GIVEN
        HairdressingSalon salon = createSalon();
        //WHEN
        salonService.save(salon);
        //THEN
        verify(salonRepository).save(salon);
    }

    @Test
    public void whenAddSalonThenShouldCallSaveMethodScheduleRepository() {
        //GIVEN
        HairdressingSalon salon = createSalon();
        Schedule schedule = new Schedule();
        schedule.setDay(DayOfWeek.FRIDAY);
        schedule.setHairdressingSalonId(salon.getId());
        //WHEN
        salonService.save(salon);
        //THEN
        verify(scheduleService).save(schedule);
    }

    @Test
    public void whenGetSalonThenShouldCallRepositoryMethod() {
        //GIVEN
        int salonId = 1;
        given(salonRepository.findById(salonId)).willReturn(Optional.of(createSalon()));
        //WHEN
        salonService.get(salonId);
        //THEN
        verify(salonRepository).findById(salonId);
    }

    @Test
    public void whenGetDoesntExistingSalonThenShouldThrownException() {
        //GIVEN
        int salonId = 1;
        given(salonRepository.findById(salonId)).willReturn(Optional.empty());
        //THEN
        assertThatThrownBy(() -> salonService.get(salonId))
                .isInstanceOf(SalonDoesntExistException.class)
                .hasMessage("salon with id " + salonId + " dont exist");
    }

    @Test
    public void whenFindAllSalonsThenShouldCallRepositoryMethod() {
        //WHEN
        salonService.getList();
        //THEN
        verify(salonRepository).findAll();
    }

    @Test
    public void whenDeleteSalonByIdThenShouldCallRepositoryMethod() {
        //GIVEN
        int salonId = 1;
        //WHEN
        salonService.delete(salonId);
        //THEN
        verify(salonRepository).deleteById(salonId);
    }

    @NotNull
    private HairdressingSalon createSalon() {
        HairdressingSalon salon = new HairdressingSalon();
        salon.setId(1);
        salon.setName("test");
        salon.setAddress("test city test street");
        salon.setDescription("welcome to test");
        salon.setEmail("test@gmail.com");
        return salon;
    }
}
