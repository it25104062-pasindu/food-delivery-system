package com.se1020.fooddelivery.controller;

import com.se1020.fooddelivery.service.DeliveryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


//rider management
@Controller
@RequestMapping("/rider")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    private boolean isRider(HttpSession session) {
        return "RIDER".equals(session.getAttribute("userRole"));
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isRider(session)) return "redirect:/login";
        String riderId = (String) session.getAttribute("userId");
        model.addAttribute("deliveries", deliveryService.getDeliveriesByRider(riderId));
        return "rider/dashboard";
    }

    @GetMapping("/delivery/{id}")
    public String deliveryDetail(@PathVariable String id, Model model, HttpSession session) {
        if (!isRider(session)) return "redirect:/login";
        deliveryService.findByOrderId(id)
                .ifPresent(d -> model.addAttribute("delivery", d));
        return "rider/delivery";
    }

    @PostMapping("/delivery/update/{id}")
    public String updateStatus(@PathVariable String id,
                               @RequestParam String status,
                               @RequestParam double latitude,
                               @RequestParam double longitude,
                               HttpSession session) {
        if (!isRider(session)) return "redirect:/login";
        deliveryService.updateStatus(id, status, latitude, longitude);
        return "redirect:/rider/dashboard";
    }

    @PostMapping("/delivery/delete/{id}")
    public String deleteDelivery(@PathVariable String id, HttpSession session) {
        if (!isRider(session)) return "redirect:/login";
        deliveryService.deleteDelivery(id);
        return "redirect:/rider/dashboard";
    }
}