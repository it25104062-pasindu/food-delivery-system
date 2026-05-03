package com.se1020.fooddelivery.model;

public class User extends Person {

    private String password;
    private String role; 
    private String address;

    public User() {}

    public User(String id, String name, String email, String password, String role, String phone, String address) {
        super(id, name, email, phone);
        this.password = password;
        this.role = role;
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
