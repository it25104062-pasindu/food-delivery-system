package com.se1020.fooddelivery.repository;


import com.se1020.fooddelivery.model.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository extends JsonRepository<Order> {

    @Override
    protected String getFileName() { return "orders.json"; }

    @Override
    protected Class<Order> getType() { return Order.class; }

    @Override
    protected String getId(Order order) { return order.getId(); }

    @Override
    protected void setId(Order order, String id) { order.setId(id); }

    public List<Order> findByCustomerId(String customerId) {
        return findWhere(o -> o.getCustomerId().equals(customerId));
    }

    public List<Order> findByStatus(String status) {
        return findWhere(o -> o.getStatus().equalsIgnoreCase(status));
    }

    public List<Order> findByRiderId(String riderId) {
        return findWhere(o -> riderId.equals(o.getAssignedRiderId()));
    }
}