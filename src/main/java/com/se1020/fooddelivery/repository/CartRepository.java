package com.se1020.fooddelivery.repository;

import com.se1020.fooddelivery.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CartRepository extends JsonRepository<Cart> {

    @Override
    protected String getFileName() { return "carts.json"; }

    @Override
    protected Class<Cart> getType() { return Cart.class; }

    @Override
    protected String getId(Cart cart) { return cart.getId(); }

    @Override
    protected void setId(Cart cart, String id) { cart.setId(id); }

    public Optional<Cart> findByCustomerId(String customerId) {
        return findWhere(c -> c.getCustomerId().equals(customerId)).stream().findFirst();
    }
}
