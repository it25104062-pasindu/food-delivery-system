package com.se1020.fooddelivery.controller;

import com.se1020.fooddelivery.model.Rider;
import com.se1020.fooddelivery.service.DeliveryService;
import com.se1020.fooddelivery.service.OrderService;
import com.se1020.fooddelivery.service.RiderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final RiderService riderService;
    private final DeliveryService deliveryService;

    public AdminController(OrderService orderService, RiderService riderService, DeliveryService deliveryService) {
        this.orderService = orderService;
        this.riderService = riderService;
        this.deliveryService = deliveryService;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("riders", riderService.getAllRiders());
        return "admin/dashboard";
    }

    @GetMapping("/orders")
    public String allOrders(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("riders", riderService.getAvailableRiders());
        Map<String, String> riderNames = riderService.getAllRiders().stream()
                .collect(Collectors.toMap(Rider::getId, Rider::getName, (a, b) -> a));
        model.addAttribute("riderNames", riderNames);
        return "admin/orders";
    }

    @PostMapping("/orders/assign")
    public String assignRider(@RequestParam String orderId,
                              @RequestParam(required = false, defaultValue = "") String riderId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        if (orderId.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Missing order.");
            return "redirect:/admin/orders";
        }
        if (riderId.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Pick a rider from the dropdown before assigning.");
            return "redirect:/admin/orders";
        }
        if (deliveryService.assignRider(orderId, riderId).isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "Could not assign. Order may already have a rider / delivery, or data is inconsistent.");
            return "redirect:/admin/orders";
        }
        redirectAttributes.addFlashAttribute("success", "Rider assigned.");
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/status/{id}")
    public String updateOrderStatus(@PathVariable String id,
                                    @RequestParam String status,
                                    HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        orderService.updateStatus(id, status);
        return "redirect:/admin/orders";
    }

    @PostMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }

    @GetMapping("/riders")
    public String riderList(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("riders", riderService.getAllRiders());
        return "admin/riders";
    }

    @PostMapping("/riders/add")
    public String addRider(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String phone,
                           @RequestParam String vehicleNumber,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) return "redirect:/login";
        if (!riderService.addRider(name, email, phone, vehicleNumber)) {
            redirectAttributes.addFlashAttribute("error",
                    "Rider not added. Email may be blank or already used in riders (riders.json only—not users).");
            return "redirect:/admin/riders";
        }
        redirectAttributes.addFlashAttribute("success", "Rider added.");
        return "redirect:/admin/riders";
    }

    @GetMapping("/riders/edit/{id}")
    public String editRiderPage(@PathVariable String id, Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        riderService.findById(id).ifPresent(r -> model.addAttribute("rider", r));
        return "admin/rider-form";
    }

    @PostMapping("/riders/edit/{id}")
    public String editRider(@PathVariable String id,
                            @RequestParam String name,
                            @RequestParam String email,
                            @RequestParam String phone,
                            @RequestParam String vehicleNumber,
                            @RequestParam(defaultValue = "false") boolean available,
                            HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        riderService.updateRider(id, name, email, phone, vehicleNumber, available);
        return "redirect:/admin/riders";
    }

    @PostMapping("/riders/delete/{id}")
    public String deleteRider(@PathVariable String id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        riderService.deleteRider(id);
        return "redirect:/admin/riders";
    }
}