package com.learning.order_service.service;

import com.learning.order_service.dto.CreateOrderRequest;
import com.learning.order_service.model.Order;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final ConcurrentMap<Long, Order> orders =
            new ConcurrentHashMap<>();

    private final AtomicLong idGenerator =
            new AtomicLong(0);

    public Order createOrder(CreateOrderRequest request) {

        Long id = idGenerator.incrementAndGet();

        Order order = new Order(
                id,
                request.customerId(),
                request.productName(),
                request.quantity(),
                request.price()
        );

        orders.put(id, order);

        return order;
    }

    public List<Order> getAllOrders() {
        return List.copyOf(orders.values());
    }

    public Order getOrderById(Long id) {
        Order order = orders.get(id);

        if (order == null) {
            throw new OrderNotFoundException(id);
        }

        return order;
    }
}