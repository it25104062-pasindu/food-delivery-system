package com.se1020.fooddelivery.controller;

import com.se1020.fooddelivery.service.CartService;
import com.se1020.fooddelivery.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customer")
public class CartController {

    private final CartService cartService;
    private final MenuService menuService;

    public CartController(CartService cartService, MenuService menuService) {
        this.cartService = cartService;
        this.menuService = menuService;
    }

    private boolean isCustomer(HttpSession session) {
        return "CUSTOMER".equals(session.getAttribute("userRole"));
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isCustomer(session)) return "redirect:/login";
        String customerId = (String) session.getAttribute("userId");
        model.addAttribute("cart", cartService.getOrCreateCart(customerId));
        return "customer/dashboard";
    }

    @GetMapping("/menu")
    public String browsMenu(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("items", category != null
                ? menuService.getByCategory(category)
                : menuService.getAvailableItems());
        model.addAttribute("category", category);
        return "customer/menu";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam String menuItemId,
                            @RequestParam int quantity,
                            HttpSession session) {
        if (!isCustomer(session)) return "redirect:/login";
        String customerId = (String) session.getAttribute("userId");
        cartService.addItem(customerId, menuItemId, quantity);
        return "redirect:/customer/cart";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        if (!isCustomer(session)) return "redirect:/login";
        String customerId = (String) session.getAttribute("userId");
        model.addAttribute("cart", cartService.getOrCreateCart(customerId));
        return "customer/cart";
    }

    @PostMapping("/cart/update")
    public String updateQuantity(@RequestParam String menuItemId,
                                 @RequestParam int quantity,
                                 HttpSession session) {
        if (!isCustomer(session)) return "redirect:/login";
        String customerId = (String) session.getAttribute("userId");
        cartService.updateItemQuantity(customerId, menuItemId, quantity);
        return "redirect:/customer/cart";
    }

    @PostMapping("/cart/remove")
    public String removeItem(@RequestParam String menuItemId, HttpSession session) {
        if (!isCustomer(session)) return "redirect:/login";
        String customerId = (String) session.getAttribute("userId");
        cartService.removeItem(customerId, menuItemId);
        return "redirect:/customer/cart";
    }
}