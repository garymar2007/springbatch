package com.gary.model;

import com.gary.utils.HashCodeHelper;
import com.gary.utils.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name="v")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@IdClass(VehiclePimaryKey.class)
public class Vehicle {
    @Id
    @XmlElement(name="VIN", required = true)
    private String vin;
    @XmlElement(name="DealerID", required = true)
    @Column(name="DealerID")
    private String dealerId;
    @Id
    @XmlElement(name="StockID", required = true)
    @Column(name="StockId")
    private String stockId;
    @XmlElement(name="Year")
    private String year;
    @XmlElement(name="Make")
    private String make;
    @XmlElement(name="Model")
    private String model;
    private String hashCode;
    @Transient
    private ItemType itemType;
//    @XmlElement(name="Body")
//    private String body;
//    @XmlElement(name="Trim")
//    private String trim;
//    @XmlElement(name="Engine")
//    private String engine;
//    @XmlElement(name="Transmission")
//    private String transmission;
//    @XmlElement(name="DriveTrain")
//    private String driveTrain;
//    @XmlElement(name="ExternalColor")
//    private String externalColor;
//    @XmlElement(name="InternalColor")
//    private String internalColor;
//    @XmlElement(name="Mileage")
//    private String mileage;
//    @XmlElement(name="AskingPrice")
//    private String askingPrice;
//    @XmlElement(name="WebPrice")
//    private String webPrice;
//    @XmlElement(name="IsUsed")
//    private int isUsed;
//    @XmlElement(name="IsCertified")
//    private int isCertified;
//    @XmlElement(name="Comment")
//    private String comment;
//    @XmlElement(name="Images")
//    private String images;
//    @XmlElement(name="VehicleType")
//    private String vehicleType;
//    @XmlElement(name="Features")
//    private String features;
//    @XmlElement(name="MSRP")
//    private String msrp;
//    @XmlElement(name="DateInStock")
//    private String dateInStock;
//    @XmlElement(name="FuelHighway")
//    private String fuelHighway;
//    @XmlElement(name="FuelCity")
//    private String fuelCity;
//    @XmlElement(name="FuelType")
//    private String fuelType;

    public Vehicle(String vin, String dealerId, String stockId, String year, String make, String model) {
        this.vin = vin;
        this.dealerId = dealerId;
        this.stockId = stockId;
        this.year = year;
        this.make = make;
        this.model = model;
    }
}
