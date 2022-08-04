package com.spdu.app.service;

import com.spdu.app.orders.exception.InvalidUserException;
import com.spdu.app.orders.model.Order;
import com.spdu.app.orders.repository.OrderRepository;
import com.spdu.app.orders.service.OrderServiceImpl;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.time_slot.service.TimeSlotService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private TimeSlotService timeSlotService;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void whenAddOrderThenShouldCallRepositoryMethod() {
        //GIVEN
        Order order = createOrder(2);
        TimeSlot timeSlot = createTimeSlot();
        given(timeSlotService.findById(timeSlot.getId())).willReturn(timeSlot);
        //WHEN
        orderService.save(order, 1);
        //THEN
        verify(orderRepository).save(order);
        verify(timeSlotService).saveOrderIntoTimeSlot(order.getId(), timeSlot.getId());
    }

    @Test
    public void whenAddOrderThenShouldCallTimeSlotServiceMethod() {
        //GIVEN
        Order order = createOrder(2);
        TimeSlot timeSlot = createTimeSlot();
        given(timeSlotService.findById(timeSlot.getId())).willReturn(timeSlot);
        //WHEN
        orderService.save(order, 1);
        //THEN
        verify(timeSlotService).saveOrderIntoTimeSlot(order.getId(), timeSlot.getId());
    }

    @Test
    public void whenBookerAndEmployeeIsOnePersonThenShouldThrowException() {
        //GIVEN
        TimeSlot timeSlot = createTimeSlot();
        int userId = 1;
        Order order = createOrder(userId);
        given(timeSlotService.findById(timeSlot.getId())).willReturn(timeSlot);
        //THEN
        assertThatThrownBy(() -> orderService.save(order, timeSlot.getId()))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("employee and user must be different users");
    }

    @Test
    public void whenAddOrderThenShouldSetOrderDate() {
        //GIVEN
        int userId = 2;
        Order order = createOrder(userId);
        TimeSlot timeSlot = createTimeSlot();
        given(timeSlotService.findById(timeSlot.getId())).willReturn(timeSlot);
        //WHEN
        orderService.save(order, timeSlot.getId());
        //THEN
        assertThat(order.getOrderDate()).isNotNull();
    }

    @Test
    public void whenFindAllOrdersThenShouldCallRepositoryMethod() {
        //WHEN
        orderService.findAll();
        //THEN
        verify(orderRepository).findAll();
    }

    @Test
    public void whenFindOrderByIdThenShouldCallRepositoryMethod() {
        //GIVEN
        int orderId = 1;
        //WHEN
        orderService.findById(orderId);
        //THEN
        verify(orderRepository).findById(orderId);
    }

    @Test
    public void whenDeleteOrderByIdThenShouldCallRepositoryMethod() {
        //GIVEN
        int orderId = 1;
        //WHEN
        orderService.deleteById(orderId);
        //THEN
        verify(orderRepository).deleteById(orderId);
    }

    @NotNull
    private TimeSlot createTimeSlot() {
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId(1);
        timeSlot.setTimeStart(LocalTime.of(8, 0));
        timeSlot.setTimeEnd(LocalTime.of(9, 0));
        timeSlot.setDay(LocalDate.now());
        timeSlot.setEmployeeId(1);
        return timeSlot;
    }

    @NotNull
    private Order createOrder(int userId) {
        Order order = new Order();
        order.setId(1);
        order.setDescription("test");
        order.setUserId(userId);
        return order;
    }
}
