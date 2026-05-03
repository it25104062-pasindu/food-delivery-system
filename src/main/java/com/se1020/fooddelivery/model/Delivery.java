package com.se1020.fooddelivery.model;

public class Delivery {

    private String id;
    private String orderId;
    private String riderId;
    private String riderName;
    private String status; // "ASSIGNED", "PICKED_UP", "ON_THE_WAY", "DELIVERED"
    private String customerAddress;
    private double latitude;
    private double longitude;
    private String updatedAt;

    public Delivery() {}

    public Delivery(String id, String orderId, String riderId, String riderName,
                    String status, String customerAddress, double latitude, double longitude, String updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.riderId = riderId;
        this.riderName = riderName;
        this.status = status;
        this.customerAddress = customerAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }

    public String getRiderName() { return riderName; }
    public void setRiderName(String riderName) { this.riderName = riderName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
