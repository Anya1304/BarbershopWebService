package com.spdu.app.service;

import com.spdu.app.time_slot.exception.TimeWasWrongException;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.time_slot.repository.TimeSlotRepository;
import com.spdu.app.time_slot.service.TimeSlotServiceImpl;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class TimeSlotServiceTest {
    @Mock
    private TimeSlotRepository timeSlotRepository;
    @InjectMocks
    private TimeSlotServiceImpl timeSlotService;

    @Test
    public void whenSaveTimeSlotThenShouldCallRepositoryMethod() {
        //GIVEN
        TimeSlot timeSlot = createTimeSlot(LocalTime.of(9, 0), LocalTime.of(10, 0), LocalDate.now().plusDays(1));
        //WHEN
        timeSlotService.save(timeSlot);
        //THEN
        ArgumentCaptor<TimeSlot> timeSlotCapture = ArgumentCaptor.forClass(TimeSlot.class);
        verify(timeSlotRepository).save(timeSlotCapture.capture());
        Assertions.assertThat(timeSlotCapture.getValue()).isEqualTo(timeSlot);
    }

    @Test
    public void whenSaveTimeSlotWithWrongDayThenShouldThrownException() {
        //GIVEN
        TimeSlot timeSlot = createTimeSlot(LocalTime.of(9, 0), LocalTime.of(9, 0), LocalDate.now().minusDays(1));
        //THEN
        assertThatThrownBy(() -> timeSlotService.save(timeSlot))
                .isInstanceOf(TimeWasWrongException.class)
                .hasMessage("Day must be after now");
        verifyNoMoreInteractions(timeSlotRepository);
    }

    @Test
    public void whenSaveTimeSlotWithWrongStartTimeThenShouldThrownException() {
        //GIVEN
        TimeSlot timeSlot = createTimeSlot(LocalTime.of(10, 0), LocalTime.of(9, 0), LocalDate.now().plusDays(2));
        //THEN
        assertThatThrownBy(() -> timeSlotService.save(timeSlot))
                .isInstanceOf(TimeWasWrongException.class)
                .hasMessage("Start Time should be before Time End");
        verifyNoMoreInteractions(timeSlotRepository);
    }

    @Test
    public void whenSaveTimeSlotWithWrongDurationThenShouldThrownException() {
        //GIVEN
        TimeSlot timeSlot = createTimeSlot(LocalTime.of(7, 0), LocalTime.of(9, 0), LocalDate.now().plusDays(1));
        //THEN
        assertThatThrownBy(() -> timeSlotService.save(timeSlot))
                .isInstanceOf(TimeWasWrongException.class)
                .hasMessage("Duration must be 1 hour");
        verifyNoMoreInteractions(timeSlotRepository);
    }

    @Test
    public void whenFindAllTimeSlotsByUserIdThenShouldCallRepositoryMethod() {
        //GIVEN
        TimeSlot timeSlot1 = createTimeSlot(LocalTime.of(7, 0), LocalTime.of(8, 0),
                LocalDate.now().plusDays(1));
        TimeSlot timeSlot2 = createTimeSlot(LocalTime.of(8, 0), LocalTime.of(9, 0),
                LocalDate.now().plusDays(1));
        int userId = 1;
        given(timeSlotRepository.findAllByUserId(userId)).willReturn(List.of(timeSlot1, timeSlot2));

        //WHEN
        timeSlotService.findAllByUserId(userId);
        //THEN
        verify(timeSlotRepository).findAllByUserId(userId);
    }

    @Test
    public void whenFindTimeSlotByIdThenShouldCallRepositoryMethod() {
        //GIVEN
        TimeSlot timeSlot = createTimeSlot(LocalTime.of(7, 0), LocalTime.of(8, 0), LocalDate.now().plusDays(1));
        timeSlot.setId(1);
        //WHEN
        given(timeSlotRepository.findById(timeSlot.getId())).willReturn(timeSlot);
        timeSlotService.findById(timeSlot.getId());
        //THEN
        verify(timeSlotRepository).findById(timeSlot.getId());
    }

    @Test
    public void whenDeleteTimeSlotThenShouldCallRepositoryMethod() {
        //GIVEN
        int timeSlotId = 1;
        //WHEN
        timeSlotService.delete(timeSlotId);
        //THEN
        verify(timeSlotRepository).deleteById(timeSlotId);
    }

    @NotNull
    private TimeSlot createTimeSlot(LocalTime timeStart, LocalTime timeEnd, LocalDate day) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setTimeStart(timeStart);
        timeSlot.setTimeEnd(timeEnd);
        timeSlot.setDay(day);
        return timeSlot;
    }
}
