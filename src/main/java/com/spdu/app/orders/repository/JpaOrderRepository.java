package com.spdu.app.orders.repository;

import com.spdu.app.orders.model.Order;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;

@Profile(value = "jpa")
public interface JpaOrderRepository extends CrudRepository<Order, Long>, OrderRepository {
}
