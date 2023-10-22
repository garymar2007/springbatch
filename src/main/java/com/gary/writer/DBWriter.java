package com.gary.writer;

import com.gary.model.Vehicle;
import com.gary.repository.VehicleRepository;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DBWriter implements ItemWriter<Vehicle> {
    @Autowired
    private VehicleRepository vehicleRepository;


    @Override
    public void write(List<? extends Vehicle> vehicles) throws Exception {
        vehicleRepository.saveAll(vehicles);
    }
}
