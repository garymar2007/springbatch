package com.gary.writer;

import com.gary.model.Inventory;
import com.gary.model.Vehicle;
import com.gary.repository.VehicleRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomizedWriter implements ItemWriter<Vehicle> {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Value("${dealer.vehicles.file.path}")
    private String dealerVehiclesFilePath;

    @Override
    public void write(List<? extends Vehicle> vehicles) throws Exception {
        vehicleRepository.saveAll(vehicles);

        Resource exportFileResource = new FileSystemResource(dealerVehiclesFilePath);
        XStreamMarshaller vehicleMarshaller = new XStreamMarshaller();
        vehicleMarshaller.setAliases(Collections.singletonMap(
                "v",
                Vehicle.class
        ));

        Map<String, List<Vehicle>> dealerVehicles =
                vehicles.stream().collect(Collectors.groupingBy(v -> v.getDealer_Id()));

        dealerVehicles.forEach((k, v) -> {
            try {
                jaxbObjectToXML(k, v);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void jaxbObjectToXML(String dealerId, List<Vehicle> vehicles) throws JAXBException {
        Inventory vehicleInv = Inventory.builder().vehicles(vehicles).build();
        JAXBContext jaxbContext = JAXBContext.newInstance(Inventory.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        jaxbMarshaller.marshal(vehicleInv, new File(dealerVehiclesFilePath + "\\" + dealerId + ".xml"));
//        //Print XML String to Console
//        StringWriter sw = new StringWriter();
//
//        //Write XML to StringWriter
//        jaxbMarshaller.marshal(vehicleInv, sw);
//
//        //Verify XML Content
//        String xmlContent = sw.toString();
//        System.out.println( xmlContent );
    }
}
