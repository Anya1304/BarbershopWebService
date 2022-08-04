package com.spdu.app.orders.repository;

import com.spdu.app.orders.mapper.OrderMapper;
import com.spdu.app.orders.model.Order;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile(value = "jdbc")
public class JdbcOrderRepository implements OrderRepository {

    protected OrderMapper orderMapper;
    protected NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(OrderMapper orderMapper,
                               NamedParameterJdbcTemplate jdbcTemplate) {
        this.orderMapper = orderMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order save(Order order) {
        MapSqlParameterSource parameterSource = getMapSqlParameterSource(order);
        String query = "INSERT INTO orders (order_date, description,user_id) " +
                "VALUES (:date, :description, :userID)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, parameterSource, keyHolder, new String[]{"id"});
        order.setId(keyHolder.getKey().intValue());

        return order;
    }

    @Override
    public Order findById(int id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("orderId", id);
        String query = "SELECT * FROM orders WHERE id = :orderId";
        return jdbcTemplate.queryForObject(query, parameterSource, orderMapper);
    }

    @Override
    public List<Order> findAll() {
        String query = "SELECT * FROM orders";
        return jdbcTemplate.query(query, orderMapper);
    }

    @Override
    public List<Order> findAllByUserId(Integer userId) {
        String query = "SELECT * FROM orders WHERE user_id=" + userId;
        return jdbcTemplate.query(query, orderMapper);
    }

    @Override
    public void deleteById(int id) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource("orderId", id);
        String query = "DELETE FROM orders WHERE id = :orderId ";
        jdbcTemplate.update(query, parameterSource);
    }

    private MapSqlParameterSource getMapSqlParameterSource(Order order) {
        return new MapSqlParameterSource()
                .addValue("date", order.getOrderDate())
                .addValue("description", order.getDescription())
                .addValue("userID", order.getUserId());
    }
}
