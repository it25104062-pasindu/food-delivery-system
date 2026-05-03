package com.se1020.fooddelivery.service;

import com.se1020.fooddelivery.model.Delivery;
import com.se1020.fooddelivery.model.Order;
import com.se1020.fooddelivery.model.Rider;
import com.se1020.fooddelivery.repository.DeliveryRepository;
import com.se1020.fooddelivery.repository.OrderRepository;
import com.se1020.fooddelivery.repository.RiderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final RiderRepository riderRepository;

    public DeliveryService(DeliveryRepository deliveryRepository,
                           OrderRepository orderRepository,
                           RiderRepository riderRepository) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.riderRepository = riderRepository;
    }

    /** Creates a Delivery when order has no rider and no delivery row yet. Empty optional on invalid input or duplicate assignment. */
    public Optional<Delivery> assignRider(String orderId, String riderId) {
        if (orderId == null || orderId.isBlank() || riderId == null || riderId.isBlank())
            return Optional.empty();
        Order order = orderRepository.findById(orderId).orElseThrow();
        String assigned = order.getAssignedRiderId();
        if (assigned != null && !assigned.isBlank())
            return Optional.empty();
        if (deliveryRepository.findByOrderId(orderId).isPresent())
            return Optional.empty();
        Rider rider = riderRepository.findById(riderId).orElseThrow();

        order.setAssignedRiderId(riderId);
        order.setStatus("CONFIRMED");
        orderRepository.update(order);

        rider.setAvailable(false);
        riderRepository.update(rider);

        Delivery delivery = new Delivery(null, orderId, riderId, rider.getName(),
                "ASSIGNED", order.getDeliveryAddress(),
                6.9271, 79.8612,
                LocalDateTime.now().toString());

        return Optional.of(deliveryRepository.save(delivery));
    }

    public List<Delivery> getDeliveriesByRider(String riderId) {
        return deliveryRepository.findByRiderId(riderId);
    }

    public Optional<Delivery> findByOrderId(String orderId) {
        return deliveryRepository.findByOrderId(orderId);
    }

    public Delivery updateStatus(String deliveryId, String status, double latitude, double longitude) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow();
        delivery.setStatus(status);
        delivery.setLatitude(latitude);
        delivery.setLongitude(longitude);
        delivery.setUpdatedAt(LocalDateTime.now().toString());

        final Delivery updatedDelivery = deliveryRepository.update(delivery);

        orderRepository.findById(updatedDelivery.getOrderId()).ifPresent(order -> {
            if ("CANCELLED".equals(order.getStatus()))
                return;
            if ("DELIVERED".equals(status)) {
                riderRepository.findById(updatedDelivery.getRiderId()).ifPresent(rider -> {
                    rider.setAvailable(true);
                    riderRepository.update(rider);
                });
                order.setStatus("DELIVERED");
                orderRepository.update(order);
            } else {
                syncOrderStatusWithDelivery(order, status);
            }
        });

        return updatedDelivery;
    }

    /** Rider delivery steps drive customer-facing order status (no manual admin step for OUT_FOR_DELIVERY). */
    private void syncOrderStatusWithDelivery(Order order, String deliveryStatus) {
        String mapped = switch (deliveryStatus != null ? deliveryStatus : "") {
            case "ASSIGNED" -> "CONFIRMED";
            case "PICKED_UP", "ON_THE_WAY" -> "OUT_FOR_DELIVERY";
            default -> null;
        };
        if (mapped == null) return;

        int current = orderStatusRank(order.getStatus());
        int next = orderStatusRank(mapped);
        if (next > current) {
            order.setStatus(mapped);
            orderRepository.update(order);
        }
    }

    private static int orderStatusRank(String s) {
        if (s == null) return 0;
        return switch (s) {
            case "PENDING" -> 0;
            case "CONFIRMED" -> 1;
            case "PREPARING" -> 2;
            case "OUT_FOR_DELIVERY" -> 3;
            case "DELIVERED" -> 4;
            default -> 0;
        };
    }

    public boolean deleteDelivery(String id) {
        return deliveryRepository.delete(id);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }
}