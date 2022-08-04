package com.spdu.app.intTests.controller.repository;

import com.spdu.app.orders.mapper.OrderMapper;
import com.spdu.app.orders.repository.JdbcOrderRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class OrderTestRepository extends JdbcOrderRepository {
    public OrderTestRepository(OrderMapper orderMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        super(orderMapper, jdbcTemplate);
    }

    public void deleteAll() {
        String query = "DELETE FROM orders";
        String query2 = "DELETE FROM users";
        jdbcTemplate.update(query, new EmptySqlParameterSource());
        jdbcTemplate.update(query2, new EmptySqlParameterSource());
    }
}
