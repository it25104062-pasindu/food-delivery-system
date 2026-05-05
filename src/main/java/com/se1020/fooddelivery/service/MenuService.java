package com.se1020.fooddelivery.service;

import com.se1020.fooddelivery.model.MenuItem;
import com.se1020.fooddelivery.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuItem> getAllItems() {
        return menuRepository.findAll();
    }

    public List<MenuItem> getAvailableItems() {
        return menuRepository.findAvailable();
    }

    public List<MenuItem> getByCategory(String category) {
        return menuRepository.findByCategory(category);
    }

    public MenuItem addItem(String name, String description, String imageUrl, double price, String category) {
        MenuItem item = new MenuItem(null, name, description, imageUrl, price, category, true);
        return menuRepository.save(item);
    }

    public MenuItem updateItem(String id, String name, String description, String imageUrl, double price, String category, boolean available) {
        MenuItem item = menuRepository.findById(id).orElseThrow();
        item.setName(name);
        item.setDescription(description);
        item.setImageUrl(imageUrl);
        item.setPrice(price);
        item.setCategory(category);
        item.setAvailable(available);
        return menuRepository.update(item);
    }

    public boolean deleteItem(String id) {
        return menuRepository.delete(id);
    }

    public Optional<MenuItem> findById(String id) {
        return menuRepository.findById(id);
    }
}