package com.se1020.fooddelivery.service;

import com.se1020.fooddelivery.model.Cart;
import com.se1020.fooddelivery.model.Order;
import com.se1020.fooddelivery.model.OrderItem;
import com.se1020.fooddelivery.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public Order placeOrder(String customerId, String customerName, String deliveryAddress) {
        Cart cart = cartService.getOrCreateCart(customerId);

        List<OrderItem> items = cart.getItems().stream()
                .map(ci -> new OrderItem(ci.getMenuItemId(), ci.getMenuItemName(), ci.getUnitPrice(), ci.getQuantity()))
                .toList();

        Order order = new Order(null, customerId, customerName, items,
                cart.getTotal(), "PENDING", deliveryAddress,
                LocalDateTime.now().toString());

        Order saved = orderRepository.save(order);
        cartService.clearCart(customerId);
        return saved;
    }

    public List<Order> getOrdersByCustomer(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    public Order cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus("CANCELLED");
        return orderRepository.update(order);
    }

    public Order updateStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        return orderRepository.update(order);
    }

    public boolean deleteOrder(String orderId) {
        return orderRepository.delete(orderId);
    }
}