package com.gary.utils;

import com.gary.model.Vehicle;
import org.apache.commons.codec.digest.DigestUtils;

public class HashCodeHelper {
    public static String getHashCode(Vehicle vehicle) {
        String objectToJson = new GsonHelper<Vehicle>().convertObjectToJson(vehicle);
        return new DigestUtils("SHA3-256").digestAsHex(objectToJson);
    }
}
