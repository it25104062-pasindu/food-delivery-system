package com.se1020.fooddelivery.service;

import com.se1020.fooddelivery.model.Cart;
import com.se1020.fooddelivery.model.CartItem;
import com.se1020.fooddelivery.model.MenuItem;
import com.se1020.fooddelivery.repository.CartRepository;
import com.se1020.fooddelivery.repository.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;

    public CartService(CartRepository cartRepository, MenuRepository menuRepository) {
        this.cartRepository = cartRepository;
        this.menuRepository = menuRepository;
    }

    public Cart getOrCreateCart(String customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> cartRepository.save(new Cart(null, customerId)));
    }

    public Cart addItem(String customerId, String menuItemId, int quantity) {
        Cart cart = getOrCreateCart(customerId);
        MenuItem menuItem = menuRepository.findById(menuItemId).orElseThrow();

        // If item already in cart, increase quantity
        for (CartItem item : cart.getItems()) {
            if (item.getMenuItemId().equals(menuItemId)) {
                item.setQuantity(item.getQuantity() + quantity);
                return cartRepository.update(cart);
            }
        }

        cart.getItems().add(new CartItem(menuItemId, menuItem.getName(), menuItem.getPrice(), quantity));
        return cartRepository.update(cart);
    }

    public Cart updateItemQuantity(String customerId, String menuItemId, int quantity) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow();
        cart.getItems().stream()
                .filter(item -> item.getMenuItemId().equals(menuItemId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
        return cartRepository.update(cart);
    }

    public Cart removeItem(String customerId, String menuItemId) {
        Cart cart = cartRepository.findByCustomerId(customerId).orElseThrow();
        cart.getItems().removeIf(item -> item.getMenuItemId().equals(menuItemId));
        return cartRepository.update(cart);
    }

    public void clearCart(String customerId) {
        cartRepository.findByCustomerId(customerId).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.update(cart);
        });
    }
}