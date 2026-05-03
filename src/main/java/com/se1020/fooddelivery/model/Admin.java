package com.se1020.fooddelivery.model;

public class Admin extends User {

    public Admin() {}

    public Admin(String id, String name, String email, String password, String phone, String address) {
        super(id, name, email, password, "ADMIN", phone, address);
    }
}
