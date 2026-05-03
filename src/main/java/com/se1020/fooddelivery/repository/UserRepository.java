package com.se1020.fooddelivery.repository;


import com.se1020.fooddelivery.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends JsonRepository<User> {

    @Override
    protected String getFileName() { return "users.json"; }

    @Override
    protected Class<User> getType() { return User.class; }

    @Override
    protected String getId(User user) { return user.getId(); }

    @Override
    protected void setId(User user, String id) { user.setId(id); }

    public Optional<User> findByEmail(String email) {
        return findWhere(u -> u.getEmail().equalsIgnoreCase(email)).stream().findFirst();
    }
}