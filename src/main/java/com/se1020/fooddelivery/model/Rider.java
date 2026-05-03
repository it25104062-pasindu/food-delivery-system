package com.se1020.fooddelivery.model;

public class Rider extends Person {

    private String vehicleNumber;
    private boolean available;

    public Rider() {}

    public Rider(String id, String name, String email, String phone, String vehicleNumber, boolean available) {
        super(id, name, email, phone);
        this.vehicleNumber = vehicleNumber;
        this.available = available;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
