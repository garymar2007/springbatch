package com.gary.processor;

import com.gary.model.Vehicle;
import com.gary.model.VehiclePimaryKey;
import com.gary.repository.VehicleRepository;
import com.gary.services.FeedFileSettingService;
import com.gary.utils.GsonHelper;
import com.gary.utils.HashCodeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.batch.item.ItemProcessor;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.Optional;

@Component
public class Processor implements ItemProcessor<Vehicle, Vehicle>{
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private FeedFileSettingService feedFileSettingService;

    @Override
    public Vehicle process(Vehicle vehicle) throws Exception {
        //if dealer ID is not the same as what I want or vehicle is unchanged, then ignore the vehicle
        if (!feedFileSettingService.getRequiredDealerIds().contains(vehicle.getDealerId())) {
            return null;
        }
        Optional<Vehicle> dbVehicle = vehicleRepository.findById(new VehiclePimaryKey(vehicle.getVin(), vehicle.getStockId()));
        if(dbVehicle != null && !dbVehicle.isEmpty() && feedFileSettingService.compareFeedFileVehicleWithDB(vehicle, dbVehicle.get())) {
            return null;
        }

        vehicle.setHashCode(HashCodeHelper.getHashCode(vehicle));
        return vehicle;
    }
}
