package com.spdu.app.intTests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spdu.app.intTests.controller.repository.OrderTestRepository;
import com.spdu.app.intTests.controller.repository.ScheduleTestRepository;
import com.spdu.app.hairdressing_salon.model.HairdressingSalon;
import com.spdu.app.hairdressing_salon.service.HairdressingSalonService;
import com.spdu.app.orders.converter.OrderConverter;
import com.spdu.app.orders.dto.OrderDto;
import com.spdu.app.orders.exception.InvalidUserException;
import com.spdu.app.orders.exception.TimeSlotAlreadyBusyException;
import com.spdu.app.orders.model.Order;
import com.spdu.app.orders.service.OrderService;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.time_slot.service.TimeSlotService;
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
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;
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
public class OrderControllerTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    OrderTestRepository orderRepository;

    @Autowired
    HairdressingSalonService salonService;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Autowired
    TimeSlotService timeSlotService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ScheduleTestRepository scheduleTestRepository;

    @AfterEach
    void afterEach() {
        orderRepository.deleteAll();
    }

    @Test
    void whenAddOrderThenShouldReturnSameOrder() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        int idOfFirstSchedule = scheduleTestRepository.findAllBySalonId(salon.getId()).get(0).getId();
        scheduleTestRepository.updateWorkingHoursById(idOfFirstSchedule, salon.getId(),
                LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"));

        User employee = new User();
        employee.setUsername("EmployeeTest");
        employee.setPassword("EmployeeTest");
        employee.setEmail("employeeTest@gmail.com");
        employee.setSalonId(salon.getId());
        saveUserToDb(employee);

        User user = createUser();
        saveUserToDb(user);

        OrderDto orderExpected = new OrderDto();
        orderExpected.setDescription("test");
        orderExpected.setUserId(user.getId());

        List<TimeSlot> timeSlot = timeSlotService.findAllByUserId(employee.getId());

        String content = objectMapper.writeValueAsString(orderExpected);
        int firstTimeSlotId = timeSlot.get(0).getId();

        String orderJson = mockMvc.perform(post("/api/orders/create")
                        .param("timeSlotId", String.valueOf(firstTimeSlotId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderDto orderActual = objectMapper.readValue(orderJson, OrderDto.class);
        orderExpected.setOrderDate(orderActual.getOrderDate());
        List<Order> orders = orderRepository.findAll();

        assertThat(orders).hasSize(1);
        assertThat(orderExpected.getOrderDate()).isEqualTo(orderActual.getOrderDate());
        assertThat(orderExpected.getUserId()).isEqualTo(orderActual.getUserId());
        assertThat(orderExpected.getDescription()).isEqualTo(orderActual.getDescription());
    }

    @Test
    void whenGetOrdersThenShouldReturnActualOrderList() throws Exception {
        //GIVEN
        User user = createUser();
        saveUserToDb(user);

        List<Order> ordersExpected = createOrders(user);
        ordersExpected.forEach(this::saveOrderToDb);

        //WHEN
        String orderJson = getResponse("/api/orders");
        List<OrderDto> ordersDto = Arrays.asList(objectMapper.readValue(orderJson, OrderDto[].class));

        //THEN
        List<Order> ordersActual = ordersDto
                .stream()
                .map(OrderConverter::fromDto).toList();
        assertTrue(ordersExpected.containsAll(ordersActual));
    }

    @Test
    void whenGetOrderThenShouldReturnSameOrder() throws Exception {
        //GIVEN
        User user = createUser();
        saveUserToDb(user);

        Order orderExpected = createOrder(user);
        saveOrderToDb(orderExpected);

        //WHEN
        String orderJson = getResponse("/api/orders/" + orderExpected.getId());
        OrderDto orderActual = objectMapper.readValue(orderJson, OrderDto.class);

        //THEN
        assertThat(orderExpected.getOrderDate()).isEqualTo(orderActual.getOrderDate());
        assertThat(orderExpected.getDescription()).isEqualTo(orderActual.getDescription());
        assertThat(orderExpected.getUserId()).isEqualTo(orderActual.getUserId());
    }

    @Test
    void whenOrderRemovedThenThisOrderDoesntExist() throws Exception {
        //GIVEN
        User user = createUser();
        saveUserToDb(user);

        Order order = createOrder(user);
        saveOrderToDb(order);

        //WHEN
        mockMvc.perform(delete("/api/orders/" + order.getId())).andExpect(status().isOk());

        String orderJson = getResponse("/api/orders");
        List<OrderDto> ordersActual = Arrays.asList(objectMapper.readValue(orderJson, OrderDto[].class));

        //THEN
        assertTrue(ordersActual.stream().noneMatch(orderDto -> orderDto.getId() == order.getId()));
    }

    @Test
    void whenTheOrderIsCreatedOnTheSamUserThenMustThrownException() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        int idOfFirstSchedule = scheduleTestRepository.findAllBySalonId(salon.getId()).get(0).getId();
        scheduleTestRepository.updateWorkingHoursById(idOfFirstSchedule, salon.getId(),
                LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"));

        User employee = new User();
        employee.setUsername("EmployeeTest");
        employee.setPassword("EmployeeTest");
        employee.setEmail("EmployeeTes@gmail.com");
        employee.setSalonId(salon.getId());
        saveUserToDb(employee);

        OrderDto orderExpected = new OrderDto();
        orderExpected.setDescription("test");
        orderExpected.setUserId(employee.getId());

        List<TimeSlot> timeSlot = timeSlotService.findAllByUserId(employee.getId());

        String content = objectMapper.writeValueAsString(orderExpected);
        int firstTimeSlotId = timeSlot.get(0).getId();

        //WHEN
        final NestedServletException exception = getNestedServletException(content, firstTimeSlotId);
        final Throwable nestedException = exception.getCause();

        //THEN
        assertThat(nestedException).isInstanceOf(InvalidUserException.class);
        assertThat(nestedException.getMessage()).isEqualTo("employee and user must be different users");
    }

    @Test
    void whenAddOrderAtBusyTimeSLotThenMustThrownException() throws Exception {
        //GIVEN
        HairdressingSalon salon = createSalon();
        saveSalonToDb(salon);

        int idOfFirstSchedule = scheduleTestRepository.findAllBySalonId(salon.getId()).get(0).getId();
        scheduleTestRepository.updateWorkingHoursById(idOfFirstSchedule, salon.getId(),
                LocalTime.parse("12:00:00"), LocalTime.parse("13:00:00"));

        User employee = new User();
        employee.setUsername("EmployeeTest2");
        employee.setPassword("EmployeeTest2");
        employee.setEmail("employeeTest@gmail.com");
        employee.setSalonId(salon.getId());
        saveUserToDb(employee);

        User user = createUser();
        saveUserToDb(user);

        OrderDto orderExpected = new OrderDto();
        orderExpected.setDescription("test");
        orderExpected.setUserId(user.getId());


        List<TimeSlot> timeSlot = timeSlotService.findAllByUserId(employee.getId());

        String content = objectMapper.writeValueAsString(orderExpected);
        int firstTimeSlotId = timeSlot.get(0).getId();
        orderService.save(OrderConverter.fromDto(orderExpected), firstTimeSlotId);

        //WHEN
        final NestedServletException exception = getNestedServletException(content, firstTimeSlotId);
        final Throwable nestedException = exception.getCause();

        //THEN
        assertThat(nestedException).isInstanceOf(TimeSlotAlreadyBusyException.class);
    }

    private String getResponse(String s) throws Exception {
        return mockMvc.perform(get(s))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private void saveOrderToDb(Order order) {
        order.setOrderDate(LocalDateTime.now().withNano(0));
        orderRepository.save(order);
    }

    private List<Order> createOrders(User user) {
        Order order1 = new Order();
        order1.setDescription("test1");
        order1.setUserId(user.getId());

        Order order2 = new Order();
        order2.setDescription("test2");
        order2.setUserId(user.getId());

        return List.of(order1, order2);
    }

    private Order createOrder(User user) {
        Order order = new Order();
        order.setDescription("test1");
        order.setUserId(user.getId());

        return order;
    }

    private HairdressingSalon createSalon() {
        HairdressingSalon hairdressingSalon = new HairdressingSalon();
        hairdressingSalon.setName("Test salon");
        hairdressingSalon.setEmail("salon@test.test");
        hairdressingSalon.setDescription("this is the best test salon");
        hairdressingSalon.setAddress("Test street 1, Test city");

        return hairdressingSalon;
    }

    private User createUser() {
        User user = new User();
        user.setUsername("test username");
        user.setPassword("test password");
        user.setEmail("test@gmail.com");

        return user;
    }

    private NestedServletException getNestedServletException(String content, int firstTimeSlotId) {
        return assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post("/api/orders/create")
                        .param("timeSlotId", String.valueOf(firstTimeSlotId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)));
    }

    private void saveSalonToDb(HairdressingSalon salon) {
        salonService.save(salon);
    }

    private void saveUserToDb(User user) {
        userService.save(user);
    }
}
