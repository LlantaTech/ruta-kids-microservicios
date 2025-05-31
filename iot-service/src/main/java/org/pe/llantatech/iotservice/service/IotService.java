package org.pe.llantatech.iotservice.service;

import org.pe.llantatech.iotservice.model.IotDevice;
import org.pe.llantatech.iotservice.model.IotDeviceMeasuring;

import java.util.List;
import java.util.Optional;

public interface IotService {
    IotDevice createDevice(IotDevice device);
    Optional<IotDevice> getDeviceById(String id);
    List<IotDevice> getAllDevices();
    IotDevice updateDevice(String id, IotDevice device);
    boolean deleteDevice(String id);

    IotDeviceMeasuring addMeasuring(String deviceId, IotDeviceMeasuring measuring);
    List<IotDeviceMeasuring> getMeasurings(String deviceId);
}
