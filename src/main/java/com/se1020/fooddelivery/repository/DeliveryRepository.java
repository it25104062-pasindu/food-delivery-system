package com.se1020.fooddelivery.repository;

import com.se1020.fooddelivery.model.Delivery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeliveryRepository extends JsonRepository<Delivery> {

    @Override
    protected String getFileName() { return "deliveries.json"; }

    @Override
    protected Class<Delivery> getType() { return Delivery.class; }

    @Override
    protected String getId(Delivery delivery) { return delivery.getId(); }

    @Override
    protected void setId(Delivery delivery, String id) { delivery.setId(id); }

    public List<Delivery> findByRiderId(String riderId) {
        return findWhere(d -> d.getRiderId().equals(riderId));
    }

    public Optional<Delivery> findByOrderId(String orderId) {
        return findWhere(d -> d.getOrderId().equals(orderId)).stream().findFirst();
    }
}