package com.spdu.app.orders.service;

import com.spdu.app.orders.model.Order;

import java.util.List;

public interface OrderService {
    Order save(Order order, int timeSlotId);

    Order findById(int id);

    List<Order> findAll();

    List<Order> findAllByUserId(Integer userId);

    void deleteById(int id);
}
