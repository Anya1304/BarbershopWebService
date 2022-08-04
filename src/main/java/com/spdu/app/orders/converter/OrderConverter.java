package com.spdu.app.orders.converter;

import com.spdu.app.orders.dto.OrderDto;
import com.spdu.app.orders.model.Order;

import java.util.List;

public final class OrderConverter {

    private OrderConverter() {
    }

    public static OrderDto toDto(Order order) {
        OrderDto orderDto = new OrderDto();

        orderDto.setId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setDescription(order.getDescription());
        orderDto.setUserId(order.getUserId());

        return orderDto;
    }

    public static Order fromDto(OrderDto orderDto) {
        Order order = new Order();

        order.setId(orderDto.getId());
        order.setOrderDate(orderDto.getOrderDate());
        order.setDescription(orderDto.getDescription());
        order.setUserId(orderDto.getUserId());

        return order;
    }

    public static List<OrderDto> toDto(List<Order> orders) {
        return orders.stream()
                .map(OrderConverter::toDto)
                .toList();
    }
}

