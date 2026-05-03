package com.se1020.fooddelivery.service;

import com.se1020.fooddelivery.model.User;
import com.se1020.fooddelivery.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String name, String email, String password, String phone, String address) {
        User user = new User(null, name, email, password, "CUSTOMER", phone, address);
        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        if (email == null || email.isBlank()) return Optional.empty();
        String attempted = password != null ? password : "";
        return userRepository.findByEmail(email.trim())
                .filter(u -> java.util.Objects.equals(u.getPassword(), attempted));
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public User updateProfile(String id, String name, String phone, String address) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        return userRepository.update(user);
    }

    public boolean deleteAccount(String id) {
        return userRepository.delete(id);
    }
}