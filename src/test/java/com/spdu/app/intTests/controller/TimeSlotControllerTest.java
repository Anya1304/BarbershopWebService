package com.spdu.app.intTests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spdu.app.intTests.controller.repository.OrderTestRepository;
import com.spdu.app.intTests.controller.repository.TimeSlotTestRepository;
import com.spdu.app.intTests.controller.repository.UserTestRepository;
import com.spdu.app.time_slot.converter.TimeSlotConverter;
import com.spdu.app.time_slot.dto.TimeSlotDto;
import com.spdu.app.time_slot.exception.TimeWasWrongException;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDate;
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
public class TimeSlotControllerTest {

    private static final LocalDate DAY1 = LocalDate.now().plusDays(1);
    private static final LocalDate DAY2 = LocalDate.now().plusDays(1);
    private static final LocalTime TIME_START1 = LocalTime.parse("09:00");
    private static final LocalTime TIME_START2 = LocalTime.parse("10:00");
    private static final LocalTime TIME_END1 = LocalTime.parse("10:00");
    private static final LocalTime TIME_END2 = LocalTime.parse("11:00");

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserTestRepository userRepository;

    @Autowired
    OrderTestRepository orderRepository;

    @Autowired
    TimeSlotTestRepository timeSlotRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void beforeEach() {
        timeSlotRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void whenAddTimeSlotThenShouldReturnSameTimeSlot() throws Exception {
        //GIVEN
        User employee = createUser();
        saveUserToDb(employee);

        TimeSlotDto timeSlotExpected = createTimeSlotDto(DAY1, TIME_START1, TIME_END1, employee);

        //WHEN
        String content = objectMapper.writeValueAsString(timeSlotExpected);
        String timeSlotJson = mockMvc.perform(post("/api/add-time-slot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        TimeSlotDto timeSlotActual = objectMapper.readValue(timeSlotJson, TimeSlotDto.class);
        List<TimeSlot> timeSlots = timeSlotRepository.findAllByUserId(employee.getId());

        //THEN
        assertThat(timeSlotExpected.getTimeEnd()).isEqualTo(timeSlotActual.getTimeEnd());
        assertThat(timeSlotExpected.getTimeStart()).isEqualTo(timeSlotActual.getTimeStart());
        assertThat(timeSlotExpected.getDay()).isEqualTo(timeSlotActual.getDay());
        assertThat(timeSlotExpected.getEmployeeId()).isEqualTo(timeSlotActual.getEmployeeId());
        assertThat(timeSlots).hasSize(1);
    }

    @Test
    void whenGetTimeSlotsThenShouldReturnActualTimeSlotList() throws Exception {
        //GIVEN
        User employee = createUser();
        saveUserToDb(employee);

        List<TimeSlot> timeSlotsExpected = createTimeSlots(employee);
        timeSlotsExpected.forEach(timeSlotRepository::save);

        //WHEN
        String timeSlotJson = getJsonFromGetResponse(employee);
        List<TimeSlotDto> timeSlotsDto = Arrays.asList(objectMapper.readValue(timeSlotJson, TimeSlotDto[].class));

        //THEN
        List<TimeSlot> timeSlotsActual = timeSlotsDto
                .stream()
                .map(TimeSlotConverter::fromDto).toList();
        assertTrue(timeSlotsExpected.containsAll(timeSlotsActual));
    }

    @Test
    void whenTimeSlotRemovedThenThisTimeSlotDoesntExist() throws Exception {
        //GIVEN
        User employee = createUser();
        saveUserToDb(employee);

        TimeSlot timeSlot = createTimeSlot(DAY2, TIME_START2, TIME_END2, employee);
        saveTimeSlotToDb(timeSlot);

        //WHEN
        mockMvc.perform(delete("/api/time-slots?employeeId=" + employee.getId()))
                .andExpect(status().isOk());

        String timeSlotJson = getJsonFromGetResponse(employee);
        List<TimeSlotDto> timeSlotDtoList = Arrays.asList(objectMapper.readValue(timeSlotJson, TimeSlotDto[].class));

        //THEN
        assertTrue(timeSlotDtoList.stream().noneMatch(timeSlotDto -> timeSlotDto.getId() == timeSlot.getId()));
    }

    @Test
    public void whenAddTimeSlotWithWrongTimeShouldReturnException() throws Exception {
        //GIVEN
        User employee = createUser();
        saveUserToDb(employee);

        TimeSlotDto timeSlotExpected = createTimeSlotDto(DAY1, TIME_END1, TIME_START1, employee);

        final NestedServletException exception = getNestedServletException(timeSlotExpected);

        final Throwable nestedException = exception.getCause();
        assertThat(nestedException).isInstanceOf(TimeWasWrongException.class);
        assertThat(nestedException.getMessage()).isEqualTo("Start Time should be before Time End");
    }

    @Test
    public void whenAddTimeSlotWithWrongDurationTimeShouldReturnException() throws Exception {
        //GIVEN
        User employee = createUser();
        saveUserToDb(employee);

        TimeSlotDto timeSlotExpected = createTimeSlotDto(DAY1, TIME_START1, TIME_START1, employee);

        //WHEN
        final NestedServletException exception = getNestedServletException(timeSlotExpected);

        //THEN
        final Throwable nestedException = exception.getCause();
        assertThat(nestedException).isInstanceOf(TimeWasWrongException.class);
        assertThat(nestedException.getMessage()).isEqualTo("Duration must be 1 hour");
    }

    @Test
    public void whenAddTimeSlotInYesterdayShouldReturnException() throws Exception {
        //GIVEN
        User employee = createUser();
        saveUserToDb(employee);

        LocalDate day = LocalDate.now().minusDays(1);
        TimeSlotDto timeSlotExpected = createTimeSlotDto(day, TIME_START1, TIME_START1, employee);

        //WHEN
        final NestedServletException exception = getNestedServletException(timeSlotExpected);

        //THEN
        final Throwable nestedException = exception.getCause();
        assertThat(nestedException).isInstanceOf(TimeWasWrongException.class);
        assertThat(nestedException.getMessage()).isEqualTo("Day must be after now");
    }

    private List<TimeSlot> createTimeSlots(User employee) {
        TimeSlot timeSlot = createTimeSlot(DAY1, TIME_START1, TIME_END1, employee);
        TimeSlot timeSlot2 = createTimeSlot(DAY2, TIME_START2, TIME_END2, employee);

        return List.of(timeSlot, timeSlot2);
    }

    private void saveUserToDb(User user) {
        userRepository.save(user);
    }

    private void saveTimeSlotToDb(TimeSlot timeSlot) {
        timeSlotRepository.save(timeSlot);
    }

    private String getJsonFromGetResponse(User employee) throws Exception {
        return mockMvc.perform(get("/api/time-slots?employeeId=" + employee.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private User createUser() {
        User user = new User();
        user.setUsername("Test username");
        user.setPassword("test13test");
        user.setEmail("test@gmail.com");

        return user;
    }

    private NestedServletException getNestedServletException(TimeSlotDto timeSlotExpected) throws JsonProcessingException {
        String content = objectMapper.writeValueAsString(timeSlotExpected);
        return assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/api/add-time-slot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)));
    }

    private TimeSlotDto createTimeSlotDto(LocalDate day, LocalTime timeStart, LocalTime
            timeEnd, User employee) {
        TimeSlotDto timeSlot = new TimeSlotDto();
        timeSlot.setDay(day);
        timeSlot.setTimeStart(timeStart);
        timeSlot.setTimeEnd(timeEnd);
        timeSlot.setEmployeeId(employee.getId());

        return timeSlot;
    }

    private TimeSlot createTimeSlot(LocalDate day, LocalTime timeStart, LocalTime
            timeEnd, User employee) {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setDay(day);
        timeSlot.setTimeStart(timeStart);
        timeSlot.setTimeEnd(timeEnd);
        timeSlot.setEmployeeId(employee.getId());

        return timeSlot;
    }
}
