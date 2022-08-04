package com.spdu.app.orders.repository;

import com.spdu.app.orders.model.Order;

import java.util.List;

public interface OrderRepository {

    Order save(Order order);

    Order findById(int id);

    List<Order> findAll();

    List<Order> findAllByUserId(Integer userId);

    void deleteById(int id);
}
