package com.spdu.app.intTests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spdu.app.intTests.controller.repository.SalonTestRepository;
import com.spdu.app.intTests.controller.repository.ScheduleTestRepository;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.schedule.converter.ScheduleConverter;
import com.spdu.app.schedule.dto.ScheduleDto;
import com.spdu.app.schedule.exception.DayDuplicationException;
import com.spdu.app.schedule.model.Schedule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", password = "user", authorities = {"USER", "ADMIN"})
public class ScheduleControllerTest {

    private static final LocalTime TIME_FROM1 = LocalTime.parse("09:00");
    private static final LocalTime TIME_TO1 = LocalTime.parse("19:00");
    private static final String DAY_OF_WEEK1 = "MONDAY";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SalonTestRepository salonRepository;

    @Autowired
    ScheduleTestRepository scheduleRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void beforeEach() {
        salonRepository.deleteAll();
    }

    @Test
    void whenAddDailyScheduleThenShouldReturnSameSchedule() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        ScheduleDto scheduleExpected = createScheduleDto(DAY_OF_WEEK1, TIME_FROM1, TIME_TO1, salon);

        String content = objectMapper.writeValueAsString(scheduleExpected);
        String scheduleJson = mockMvc.perform(post("/api/add-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ScheduleDto scheduleActual = objectMapper.readValue(scheduleJson, ScheduleDto.class);
        List<Schedule> schedules = scheduleRepository.findAllBySalonId(salon.getId());

        assertThat(schedules).hasSize(1);
        assertThatSchedulesEquals(scheduleExpected, scheduleActual);
    }

    @Test
    void whenGetSchedulesThenShouldReturnActualScheduleList() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        List<Schedule> schedulesExpected = createSchedules(salon);
        schedulesExpected.forEach(scheduleRepository::save);

        //WHEN
        String scheduleJson = mockMvc.perform(get("/api/schedule?salonId=" + salon.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ScheduleDto> schedulesDto = Arrays.asList(objectMapper.readValue(scheduleJson, ScheduleDto[].class));

        //THEN
        List<Schedule> schedulesActual = schedulesDto
                .stream()
                .map(ScheduleConverter::fromDto).toList();
        assertTrue(schedulesExpected.containsAll(schedulesActual));
    }

    @Test
    void whenScheduleRemovedThenThisScheduleDoesntExist() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        Schedule schedule = createSchedule(DAY_OF_WEEK1, TIME_FROM1, TIME_TO1, salon);
        saveScheduleToDb(schedule);

        //WHEN
        mockMvc.perform(delete("/api/schedule?salonId=" + salon.getId()));

        String scheduleJson = mockMvc.perform(get("/api/schedule?salonId=" + salon.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ScheduleDto> scheduleDtoList = Arrays.asList(objectMapper.readValue(scheduleJson, ScheduleDto[].class));

        //THEN
        assertTrue(scheduleDtoList.stream().noneMatch(scheduleDto -> scheduleDto.getId() == schedule.getId()));
    }

    @Test
    void whenAddExistingDailyScheduleThenShouldThrownException() {
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        Schedule schedule = createSchedule(DAY_OF_WEEK1, TIME_FROM1, TIME_TO1, salon);
        saveScheduleToDb(schedule);

        Schedule schedule1 = createSchedule(DAY_OF_WEEK1, TIME_FROM1, TIME_TO1, salon);

        final NestedServletException exception = assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/api/add-schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(schedule1))));

        final Throwable nestedException = exception.getCause();
        assertThat(nestedException).isInstanceOf(DayDuplicationException.class);
    }

    private Schedule saveScheduleToDb(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    private void saveSalonToDb(HairdressingSalon salon) {
        salonRepository.save(salon);
    }

    private List<Schedule> createSchedules(HairdressingSalon salon) {
        Schedule schedule1 = createSchedule(DAY_OF_WEEK1, TIME_FROM1, TIME_TO1, salon);
        Schedule schedule2 = createSchedule("TUESDAY", TIME_FROM1, TIME_TO1, salon);
        Schedule schedule3 = createSchedule("WEDNESDAY", TIME_FROM1, TIME_TO1, salon);
        Schedule schedule4 = createSchedule("THURSDAY", TIME_FROM1, TIME_TO1, salon);
        Schedule schedule5 = createSchedule("FRIDAY", TIME_FROM1, TIME_TO1, salon);
        Schedule schedule6 = createSchedule("SUNDAY", TIME_FROM1, TIME_TO1, salon);
        Schedule schedule7 = createSchedule("SATURDAY", TIME_FROM1, TIME_TO1, salon);

        return List.of(schedule1, schedule2, schedule3, schedule4, schedule5, schedule6, schedule7);
    }

    private Schedule createSchedule(String dayOfWeek, LocalTime timeFrom,
                                    LocalTime timeTo, HairdressingSalon salon) {
        Schedule schedule = new Schedule();
        schedule.setDay(DayOfWeek.valueOf(dayOfWeek));
        schedule.setTimeFrom(timeFrom);
        schedule.setTimeTo(timeTo);
        schedule.setHairdressingSalonId(salon.getId());

        return schedule;
    }

    private ScheduleDto createScheduleDto(String dayOfWeek, LocalTime timeFrom,
                                          LocalTime timeTo, HairdressingSalon salon) {
        ScheduleDto schedule = new ScheduleDto();
        schedule.setDay(DayOfWeek.valueOf(dayOfWeek));
        schedule.setTimeFrom(timeFrom);
        schedule.setTimeTo(timeTo);
        schedule.setHairdressingSalonId(salon.getId());

        return schedule;
    }

    private HairdressingSalon createSalon() {
        HairdressingSalon hairdressingSalon = new HairdressingSalon();
        hairdressingSalon.setName("Test salon");
        hairdressingSalon.setEmail("salon@test.test");
        hairdressingSalon.setDescription("this is the best test salon");
        hairdressingSalon.setAddress("Test street 1, Test city");

        return hairdressingSalon;
    }

    private void assertThatSchedulesEquals(ScheduleDto scheduleExpected, ScheduleDto scheduleActual) {
        assertThat(scheduleActual.getDay()).isEqualTo(scheduleExpected.getDay());
        assertThat(scheduleActual.getTimeFrom()).isEqualTo(scheduleExpected.getTimeFrom());
        assertThat(scheduleActual.getTimeTo()).isEqualTo(scheduleExpected.getTimeTo());
    }
}
