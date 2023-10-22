package com.gary.processor;

import com.gary.model.Vehicle;
import org.springframework.stereotype.Component;

import org.springframework.batch.item.ItemProcessor;
import java.util.Date;

@Component
public class Processor implements ItemProcessor<Vehicle, Vehicle>{

    @Override
    public Vehicle process(Vehicle vehicle) throws Exception {
        vehicle.setTimestamp(new Date());
        return vehicle;
    }
}
