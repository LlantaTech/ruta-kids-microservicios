package org.pe.llantatech.iotservice.model;

import lombok.Data;

@Data
public class IotDeviceMeasuring {

    private String id;
    private String deviceId;
    private String measuringType;
    private String measuringValue;
    private String measuringUnit;
    private Long measuringTimestamp;
    private IotMeasuringStatus status;
    private String locationId;
    private Location location;

    private boolean isAlert;
}
