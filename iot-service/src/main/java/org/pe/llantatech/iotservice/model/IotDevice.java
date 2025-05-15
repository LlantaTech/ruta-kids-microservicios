package org.pe.llantatech.iotservice.model;

import lombok.Data;

import java.util.List;

@Data
public class IotDevice {

    private String id;
    private String serialNumber;
    private String deviceType;
    private String deviceName;
    private String deviceStatus; // active, inactive, maintenance
    private Location location;
    private Long createdAt;
    private Long updatedAt;

    private List<IotDeviceMeasuring> measurings;

    private IotAction action;
}
