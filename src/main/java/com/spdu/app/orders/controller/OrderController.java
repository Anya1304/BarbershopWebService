package com.spdu.app.orders.controller;

import com.spdu.app.orders.dto.OrderDto;
import com.spdu.app.orders.service.OrderServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.spdu.app.orders.converter.OrderConverter.fromDto;
import static com.spdu.app.orders.converter.OrderConverter.toDto;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "salon")
public class OrderController {

    private final OrderServiceImpl orderService;

    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders/create/")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public OrderDto postOrder(@RequestBody @Valid OrderDto orderDto, Integer timeSlotId) {
        return toDto(orderService.save(fromDto(orderDto), timeSlotId));
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('USER')")
    public List<OrderDto> getOrders() {
        return toDto(orderService.findAll());
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public OrderDto getOrder(@PathVariable int id) {
        return toDto(orderService.findById(id));
    }

    @DeleteMapping("/orders/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteOrder(@PathVariable int id) {
        orderService.deleteById(id);
    }
}
