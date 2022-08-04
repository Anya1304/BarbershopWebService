package com.spdu.app.orders.mapper;

import com.spdu.app.orders.model.Order;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();

        order.setId(rs.getInt("id"));
        String date = rs.getString("order_date");
        order.setOrderDate(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        order.setDescription(rs.getString("description"));
        order.setUserId(rs.getObject("user_id", Integer.class));

        return order;
    }
}
