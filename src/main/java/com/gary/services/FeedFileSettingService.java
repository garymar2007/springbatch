package com.gary.services;

import com.gary.model.Vehicle;
import com.gary.repository.VehicleRepository;
import com.gary.utils.HashCodeHelper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@Data
public class FeedFileSettingService {

    @Autowired
    private VehicleRepository vehicleRepository;

    private List<Vehicle> addVehicles;
    private List<Vehicle> revisedVehicles;
    private List<Vehicle> endVehicles;
    private final List<String> requiredDealerIds = new ArrayList<>() {
        {
            add("1195");
            add("1196");
        }
    };

    public void addNewVehicle(Vehicle v) {
        addVehicles.add(v);
    }

    public void addUpdateVehicle(Vehicle v) {
        revisedVehicles.add(v);
    }

    public void addEndVehicle(Vehicle v) {
        endVehicles.add(v);
    }

    public boolean compareFeedFileVehicleWithDB(Vehicle feedFileVehicle, Vehicle dbVehicle) {
        if (getCompositeKey(feedFileVehicle).equals(getCompositeKey(dbVehicle))
                && HashCodeHelper.getHashCode(feedFileVehicle).equals(dbVehicle.getHashCode())) {
            return true;
        }
        return false;
    }

    private String getCompositeKey(Vehicle v) {
        return String.format("%s%s%s",v.getDealerId(), v.getStockId(), v.getVin());
    }
}
