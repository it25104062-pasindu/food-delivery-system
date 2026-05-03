package com.se1020.fooddelivery.model;

import java.util.List;

public class Order {

    private String id;
    private String customerId;
    private String customerName;
    private List<OrderItem> items;
    private double totalAmount;
    private String status; // "PENDING", "CONFIRMED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED", "CANCELLED"
    private String deliveryAddress;
    private String createdAt;
    private String assignedRiderId;


    public Order() {}

    public Order(String id, String customerId, String customerName, List<OrderItem> items,
                 double totalAmount, String status, String deliveryAddress, String createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getAssignedRiderId() { return assignedRiderId; }
    public void setAssignedRiderId(String assignedRiderId) { this.assignedRiderId = assignedRiderId; }
}
