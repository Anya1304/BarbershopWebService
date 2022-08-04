package com.spdu.app.intTests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spdu.app.intTests.controller.repository.SalonTestRepository;
import com.spdu.app.intTests.controller.repository.ScheduleTestRepository;
import com.spdu.app.intTests.controller.repository.TimeSlotTestRepository;
import com.spdu.app.intTests.controller.repository.UserTestRepository;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.hairdressing_salon.service.HairdressingSalonService;
import com.spdu.app.role.Role;
import com.spdu.app.role.service.RoleService;
import com.spdu.app.schedule.model.Schedule;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.user.converter.UserConverter;
import com.spdu.app.user.dto.UserDto;
import com.spdu.app.user.model.User;
import com.spdu.app.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "user", password = "user", authorities = {"USER", "ADMIN"})
class UserControllerTest {

    private static final String TEST_PASSWORD1 = "first testPassword";
    private static final String TEST_NAME1 = "test1";
    private static final String TEST_EMAIL1 = "test1@gmail.com";

    @Autowired
    UserTestRepository userRepository;

    @Autowired
    SalonTestRepository salonRepository;

    @Autowired
    HairdressingSalonService salonService;

    @Autowired
    RoleService roleService;

    @Autowired
    TimeSlotTestRepository timeSlotTestRepository;

    @Autowired
    ScheduleTestRepository scheduleTestRepository;

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void whenAddUserThenShouldReturnSameUser() throws Exception {
        //GIVEN
        UserDto userExpected = new UserDto();
        userExpected.setUsername(TEST_NAME1);
        userExpected.setPassword(TEST_PASSWORD1);
        userExpected.setEmail(TEST_EMAIL1);

        //WHEN
        String userJson = getJsonFromResponse(objectMapper
                .writeValueAsString(userExpected));

        UserDto userActual = objectMapper.readValue(userJson, UserDto.class);

        List<User> users = userService.getList();

        //THEN
        assertThat(userActual.getUsername()).isEqualTo(userExpected.getUsername());

        assertThat(users).hasSize(1);
    }

    @Test
    void whenGetUsersThenShouldReturnActualUserList() throws Exception {
        //GIVEN
        List<User> usersExpected = createUsers();
        usersExpected.forEach(this::saveUserToDb);

        //WHEN
        String userJson = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserDto> usersDto = Arrays.asList(objectMapper.readValue(userJson, UserDto[].class));

        //THEN
        List<User> usersActual = usersDto
                .stream()
                .map(UserConverter::fromDto).toList();
        assertTrue(usersExpected.containsAll(usersActual));
    }

    @Test
    void whenGetUserThenShouldReturnSameUser() throws Exception {
        //GIVEN
        User userExpected = createUser(TEST_NAME1, TEST_PASSWORD1, TEST_EMAIL1);
        userExpected = saveUserToDb(userExpected);

        //WHEN
        String userJson = mockMvc.perform(get("/api/users/" + userExpected.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserDto userActual = objectMapper.readValue(userJson, UserDto.class);

        //THEN
        assertThatUserEqualsUserDto(userExpected, userActual);
    }

    @Test
    void whenUserRemovedThenThisUserDoesntExist() throws Exception {
        //GIVEN
        User user = createUser(TEST_NAME1, TEST_PASSWORD1, TEST_EMAIL1);
        user = saveUserToDb(user);

        //WHEN
        mockMvc.perform(delete("/api/users/" + user.getId())).andExpect(status().isOk());

        String userJson = mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserDto> usersActual = Arrays.asList(objectMapper.readValue(userJson, UserDto[].class));

        //THEN
        User finalUser = user;
        assertTrue(usersActual.stream().noneMatch(userDto -> userDto.getId() == finalUser.getId()));
    }

    @Test
    void whenAddUserWithoutSalonIdThenShouldContainDefaultRole() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        UserDto user = new UserDto();
        user.setUsername(TEST_NAME1);
        user.setPassword(TEST_PASSWORD1);
        user.setEmail(TEST_EMAIL1);

        //WHEN
        String userJson = getJsonFromResponse(objectMapper
                .writeValueAsString(user));
        UserDto usersActual = objectMapper.readValue(userJson, UserDto.class);
        List<Role> roleList = roleService.findAllByUserId(usersActual.getId());

        //THEN
        assertTrue(roleList.contains(Role.USER));
    }

    @Test
    void whenAddUserWithSalonIdThenShouldContainsEmployeeRole() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        UserDto user = new UserDto();
        user.setUsername(TEST_NAME1);
        user.setPassword(TEST_PASSWORD1);
        user.setEmail(TEST_EMAIL1);
        user.setSalonId(salon.getId());

        //WHEN
        String content = objectMapper
                .writeValueAsString(user);
        String userJson = getJsonFromResponse(content);
        UserDto usersActual = objectMapper.readValue(userJson, UserDto.class);
        List<Role> roleList = roleService.findAllByUserId(usersActual.getId());

        //THEN
        assertTrue(roleList.contains(Role.USER));
        assertTrue(roleList.contains(Role.EMPLOYEE));
    }

    @Test
    void whenAddUserWithSalonIdThenShouldGenerateTimeSlotsForThisUser() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);


        UserDto user = new UserDto();
        user.setUsername(TEST_NAME1);
        user.setPassword(TEST_PASSWORD1);
        user.setEmail(TEST_EMAIL1);
        user.setSalonId(salon.getId());

        int idOfFirstSchedule = scheduleTestRepository.findAllBySalonId(salon.getId()).get(0).getId();
        scheduleTestRepository.updateWorkingHoursById(idOfFirstSchedule, salon.getId(), LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"));
        //WHEN
        String content = objectMapper
                .writeValueAsString(user);
        String userJson = getJsonFromResponse(content);
        UserDto usersActual = objectMapper.readValue(userJson, UserDto.class);

        List<Schedule> scheduleList = scheduleTestRepository.findAllBySalonId(usersActual
                        .getSalonId())
                .stream()
                .filter(schedule -> schedule.getTimeFrom() != null)
                .collect(Collectors.toList());

        List<TimeSlot> timeSlots = timeSlotTestRepository.findAllByUserId(usersActual.getId());

        //THEN
        assertThat(timeSlots).isNotEmpty();
        for (TimeSlot timeSlot : timeSlots) {
            assertTrue(scheduleList.stream()
                    .anyMatch(schedules -> schedules.getDay().equals(timeSlot.getDay().getDayOfWeek())));
        }
    }

    private String getJsonFromResponse(String objectAsString) throws Exception {
        return mockMvc.perform(post("/api/add-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectAsString))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private List<User> createUsers() {
        User user1 = createUser(TEST_NAME1, TEST_PASSWORD1, TEST_EMAIL1);

        String testName2 = "test2";
        String testPassword2 = "second testPassword";
        String testEmail2 = "test2@gmail.com";
        User user2 = createUser(testName2, testPassword2, testEmail2);

        return List.of(user1, user2);
    }

    private User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        return user;
    }

    private HairdressingSalon createSalon() {
        HairdressingSalon hairdressingSalon = new HairdressingSalon();
        hairdressingSalon.setName("test salon");
        hairdressingSalon.setEmail("testSalon@gmail.com");
        hairdressingSalon.setDescription("test description");
        hairdressingSalon.setAddress("test street, Test city");


        return hairdressingSalon;
    }

    private HairdressingSalon saveSalonToDb(HairdressingSalon hairdressingSalon) {
        return salonService.save(hairdressingSalon);
    }

    private User saveUserToDb(User user) {
        return userService.save(user);
    }

    private void assertThatUserEqualsUserDto(User userActual, UserDto userExpected) {
        assertThat(userActual.getPassword()).isEqualTo(userExpected.getPassword());
        assertThat(userActual.getUsername()).isEqualTo(userExpected.getUsername());
    }
}
