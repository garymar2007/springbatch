package com.gary.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name="inv")
public class Inventory {
    @XmlElement(name = "v")
    private List<Vehicle> vehicles;
}
