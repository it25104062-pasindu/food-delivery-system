package com.se1020.fooddelivery.controller;

import com.se1020.fooddelivery.service.RiderService;
import com.se1020.fooddelivery.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;


/*
CREATE - POST
READ - GET
UPDATE  - PUT
DELETE -DELTE
*/

@Controller
public class AuthController {

    private final UserService userService;
    private final RiderService riderService;

    public AuthController(UserService userService, RiderService riderService) {
        this.userService = userService;
        this.riderService = riderService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        String trimmedEmail = email != null ? email.trim() : "";
        if (trimmedEmail.isEmpty() || password == null) {
            model.addAttribute("error", "Enter both email and password.");
            return "auth/login";
        }

        var userOpt = userService.login(trimmedEmail, password);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Invalid email or password");
            return "auth/login";
        }

        var u = userOpt.get();
        String userIdStored = u.getId();
        if (userIdStored == null || userIdStored.isBlank()) {
            model.addAttribute("error", "Account data is incomplete. Contact support.");
            return "auth/login";
        }

        String roleRaw = u.getRole();
        if (roleRaw == null || roleRaw.isBlank()) {
            model.addAttribute("error", "This account has no assigned role. Please contact support.");
            return "auth/login";
        }

        String roleUpper = roleRaw.trim().toUpperCase(Locale.ROOT);
        String sessionRole =
                switch (roleUpper) {
                    case "ADMIN", "RIDER" -> roleUpper;
                    default -> "CUSTOMER";
                };

        regenerateSession(request);
        HttpSession session = request.getSession();
        session.setAttribute("userId", userIdStored);
        session.setAttribute("userRole", sessionRole);
        session.setAttribute("userName", u.getName() != null ? u.getName() : "");

        return switch (sessionRole) {
            case "ADMIN" -> "redirect:/admin/dashboard";
            case "RIDER" -> "redirect:/rider/dashboard";
            default -> "redirect:/customer/dashboard";
        };
    }

    @PostMapping("/login/rider")
    public String riderLogin(HttpServletRequest request, @RequestParam String email, Model model) {
        var riderOpt = riderService.findByEmail(email);
        if (riderOpt.isEmpty()) {
            model.addAttribute("error", "Rider account not found for this email");
            return "auth/login";
        }

        var rider = riderOpt.get();
        String id = rider.getId();
        if (id == null || id.isBlank()) {
            model.addAttribute("error", "Rider record is incomplete. Ask admin to recreate this rider.");
            return "auth/login";
        }

        regenerateSession(request);
        HttpSession session = request.getSession();
        session.setAttribute("userId", id);
        session.setAttribute("userRole", "RIDER");
        session.setAttribute("userName", rider.getName() != null ? rider.getName() : "");
        return "redirect:/rider/dashboard";
    }

    private static void regenerateSession(HttpServletRequest request) {
        HttpSession old = request.getSession(false);
        if (old != null) {
            try {
                old.invalidate();
            } catch (IllegalStateException ignored) {
                // session already invalidated
            }
        }
        request.getSession(true);
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String phone,
                           @RequestParam String address) {
        userService.register(name, email, password, phone, address);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        if (userId == null) return "redirect:/login";
        if ("RIDER".equals(userRole)) return "redirect:/rider/dashboard";
        userService.findById(userId).ifPresent(u -> model.addAttribute("user", u));
        return "auth/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String phone,
                                @RequestParam String address,
                                HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        userService.updateProfile(userId, name, phone, address);
        return "redirect:/profile";
    }

    @PostMapping("/profile/delete")
    public String deleteAccount(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        userService.deleteAccount(userId);
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            try {
                session.invalidate();
            } catch (IllegalStateException ignored) {
                //
            }
        }
        return "redirect:/login";
    }
}
