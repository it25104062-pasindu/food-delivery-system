package com.se1020.fooddelivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart {

    private String id;
    private String customerId;
    private List<CartItem> items = new ArrayList<>();

    public Cart() {}

    public Cart(String id, String customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    @JsonIgnore
    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    @JsonIgnore
    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { this.items = items; }
}