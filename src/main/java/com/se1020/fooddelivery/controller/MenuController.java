package com.se1020.fooddelivery.controller;

import com.se1020.fooddelivery.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public String listItems(Model model) {
        model.addAttribute("items", menuService.getAllItems());
        return "menu/list";
    }

    @GetMapping("/add")
    public String addItemPage() {
        return "menu/form";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam String name,
                          @RequestParam String description,
                          @RequestParam String imageUrl,
                          @RequestParam double price,
                          @RequestParam String category) {
        menuService.addItem(name, description, imageUrl, price, category);
        return "redirect:/admin/menu";
    }

    @GetMapping("/edit/{id}")
    public String editItemPage(@PathVariable String id, Model model) {
        menuService.findById(id).ifPresent(item -> model.addAttribute("item", item));
        return "menu/form";
    }

    @PostMapping("/edit/{id}")
    public String editItem(@PathVariable String id,
                           @RequestParam String name,
                           @RequestParam String description,
                           @RequestParam String imageUrl,
                           @RequestParam double price,
                           @RequestParam String category,
                           @RequestParam(defaultValue = "false") boolean available) {
        menuService.updateItem(id, name, description, imageUrl, price, category, available);
        return "redirect:/admin/menu";
    }

    @PostMapping("/delete/{id}")
    public String deleteItem(@PathVariable String id) {
        menuService.deleteItem(id);
        return "redirect:/admin/menu";
    }
}