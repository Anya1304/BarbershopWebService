package com.spdu.app.orders.service;

import com.spdu.app.orders.exception.InvalidUserException;
import com.spdu.app.orders.model.Order;
import com.spdu.app.orders.repository.OrderRepository;
import com.spdu.app.time_slot.model.TimeSlot;
import com.spdu.app.time_slot.service.TimeSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements com.spdu.app.orders.service.OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final TimeSlotService timeSlotService;

    public OrderServiceImpl(OrderRepository userRepository,
                            TimeSlotService timeSlotService) {
        this.orderRepository = userRepository;
        this.timeSlotService = timeSlotService;
    }

    @Override
    public Order save(Order order, int timeSlotId) {
        order.setOrderDate(LocalDateTime.now().withNano(0));
        checkOrder(order, timeSlotId);
        orderRepository.save(order);
        logger.info("Order saved in db");
        timeSlotService.saveOrderIntoTimeSlot(order.getId(), timeSlotId);
        return order;
    }

    @Override
    public Order findById(int id) {
        logger.info("search order with id {}", id);
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findAllByUserId(Integer userId) {
        return orderRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteById(int id) {
        orderRepository.deleteById(id);
        logger.info("deleted order with id {}", id);
    }

    private void checkOrder(Order order, Integer timeSlotId) {
        TimeSlot timeSlot = timeSlotService.findById(timeSlotId);
        if (Objects.equals(timeSlot.getEmployeeId(), order.getUserId())) {
            throw new InvalidUserException("employee and user must be different users");
        }
    }
}
