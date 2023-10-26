package com.gary.repository;

import com.gary.model.Vehicle;
import com.gary.model.VehiclePimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, VehiclePimaryKey> {
    Optional<Vehicle> findById(VehiclePimaryKey id);

    List<Vehicle> findByDealerId(String dealerId);
}
