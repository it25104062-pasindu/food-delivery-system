package com.se1020.fooddelivery.service;

import com.se1020.fooddelivery.model.Rider;
import com.se1020.fooddelivery.repository.RiderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RiderService {

    private final RiderRepository riderRepository;

    public RiderService(RiderRepository riderRepository) {
        this.riderRepository = riderRepository;
    }

    public List<Rider> getAllRiders() {
        return riderRepository.findAll();
    }

    public List<Rider> getAvailableRiders() {
        return riderRepository.findAvailable();
    }

    public Optional<Rider> findById(String id) {
        return riderRepository.findById(id);
    }

    public Optional<Rider> findByEmail(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        String trimmed = email.trim();
        return riderRepository.findWhere(r -> r.getEmail() != null && r.getEmail().equalsIgnoreCase(trimmed))
                .stream()
                .findFirst();
    }

    /** Saves only to riders.json. Returns false if email blank or already used. */
    public boolean addRider(String name, String email, String phone, String vehicleNumber) {
        if (email == null || email.isBlank()) return false;
        String clean = email.trim();
        if (findByEmail(clean).isPresent()) return false;
        riderRepository.save(new Rider(null, name, clean, phone, vehicleNumber, true));
        return true;
    }

    public Rider updateRider(String id, String name, String email, String phone, String vehicleNumber, boolean available) {
        Rider rider = riderRepository.findById(id).orElseThrow();
        rider.setName(name);
        rider.setEmail(email);
        rider.setPhone(phone);
        rider.setVehicleNumber(vehicleNumber);
        rider.setAvailable(available);
        return riderRepository.update(rider);
    }

    public boolean deleteRider(String id) {
        return riderRepository.delete(id);
    }
}