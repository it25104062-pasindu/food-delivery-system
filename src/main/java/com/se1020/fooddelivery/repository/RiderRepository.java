package com.se1020.fooddelivery.repository;

import com.se1020.fooddelivery.model.Rider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RiderRepository extends JsonRepository<Rider> {

    @Override
    protected String getFileName() { return "riders.json"; }

    @Override
    protected Class<Rider> getType() { return Rider.class; }

    @Override
    protected String getId(Rider rider) { return rider.getId(); }

    @Override
    protected void setId(Rider rider, String id) { rider.setId(id); }

    public List<Rider> findAvailable() {
        return findWhere(Rider::isAvailable);
    }
}