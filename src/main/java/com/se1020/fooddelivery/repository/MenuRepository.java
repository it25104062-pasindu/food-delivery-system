package com.se1020.fooddelivery.repository;

import com.se1020.fooddelivery.model.MenuItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuRepository extends JsonRepository<MenuItem> {

    @Override
    protected String getFileName() { return "menu.json"; }

    @Override
    protected Class<MenuItem> getType() { return MenuItem.class; }

    @Override
    protected String getId(MenuItem item) { return item.getId(); }

    @Override
    protected void setId(MenuItem item, String id) { item.setId(id); }

    public List<MenuItem> findByCategory(String category) {
        return findWhere(item -> item.getCategory().equalsIgnoreCase(category));
    }

    public List<MenuItem> findAvailable() {
        return findWhere(MenuItem::isAvailable);
    }
}