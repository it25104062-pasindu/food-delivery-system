package com.se1020.fooddelivery.controller;

import com.se1020.fooddelivery.model.Delivery;
import com.se1020.fooddelivery.service.DeliveryService;
import com.se1020.fooddelivery.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/customer/orders")
public class OrderController {

    private final OrderService orderService;
    private final DeliveryService deliveryService;

    public OrderController(OrderService orderService, DeliveryService deliveryService) {
        this.orderService = orderService;
        this.deliveryService = deliveryService;
    }

    private boolean isCustomer(HttpSession session) {
        return "CUSTOMER".equals(session.getAttribute("userRole"));
    }

    @GetMapping("/confirm")
    public String confirmPage(HttpSession session, Model model) {
        if (!isCustomer(session)) return "redirect:/login";
        String customerId = (String) session.getAttribute("userId");
        model.addAttribute("customerId", customerId);
        return "orders/confirm";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam String deliveryAddress,
                             HttpSession session) {
        if (!isCustomer(session)) return "redirect:/login";
        String customerId = (String) session.getAttribute("userId");
        String customerName = (String) session.getAttribute("userName");
        orderService.placeOrder(customerId, customerName, deliveryAddress);
        return "redirect:/customer/orders/history";
    }

    @GetMapping("/history")
    public String orderHistory(HttpSession session, Model model) {
        if (!isCustomer(session)) return "redirect:/login";
        String customerId = (String) session.getAttribute("userId");
        model.addAttribute("orders", orderService.getOrdersByCustomer(customerId));
        return "orders/history";
    }

    @GetMapping("/track/{id}")
    public String trackOrder(@PathVariable String id, Model model, HttpSession session) {
        if (!isCustomer(session)) return "redirect:/login";
        orderService.findById(id).ifPresent(o -> model.addAttribute("order", o));
        Optional<Delivery> deliveryOpt = deliveryService.findByOrderId(id);
        deliveryOpt.ifPresent(d -> model.addAttribute("delivery", d));

        double lat = 6.9271;
        double lng = 79.8612;
        if (deliveryOpt.isPresent()) {
            var d = deliveryOpt.get();
            lat = d.getLatitude();
            lng = d.getLongitude();
        }
        // US-formatted decimals for JS parseFloat — avoids locale commas breaking the map inline script.
        model.addAttribute("mapLatUs", String.format(Locale.US, "%.7f", lat));
        model.addAttribute("mapLngUs", String.format(Locale.US, "%.7f", lng));
        model.addAttribute("mapHasDelivery", deliveryOpt.isPresent());

        return "orders/track";
    }

    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable String id, HttpSession session) {
        if (!isCustomer(session)) return "redirect:/login";
        orderService.cancelOrder(id);
        return "redirect:/customer/orders/history";
    }
}

// order placment and tracking