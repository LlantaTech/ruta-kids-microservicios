package org.pe.llantatech.iotservice.service.impl;

import org.pe.llantatech.iotservice.model.IotDevice;
import org.pe.llantatech.iotservice.model.IotDeviceMeasuring;
import org.pe.llantatech.iotservice.model.IotMeasuringStatus;
import org.pe.llantatech.iotservice.service.IotService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IotServiceImpl implements IotService {

    private final Map<String, IotDevice> deviceStore = new HashMap<>();

    @Override
    public IotDevice createDevice(IotDevice device) {
        String id = UUID.randomUUID().toString();
        device.setId(id);
        device.setCreatedAt(System.currentTimeMillis());
        device.setUpdatedAt(System.currentTimeMillis());
        device.setMeasurings(new ArrayList<>());
        deviceStore.put(id, device);
        return device;
    }

    @Override
    public Optional<IotDevice> getDeviceById(String id) {
        return Optional.ofNullable(deviceStore.get(id));
    }

    @Override
    public List<IotDevice> getAllDevices() {
        return new ArrayList<>(deviceStore.values());
    }

    @Override
    public IotDevice updateDevice(String id, IotDevice device) {
        IotDevice existing = deviceStore.get(id);
        if (existing == null) return null;
        device.setId(id);
        device.setCreatedAt(existing.getCreatedAt());
        device.setUpdatedAt(System.currentTimeMillis());
        deviceStore.put(id, device);
        return device;
    }

    @Override
    public boolean deleteDevice(String id) {
        return deviceStore.remove(id) != null;
    }

    @Override
    public IotDeviceMeasuring addMeasuring(String deviceId, IotDeviceMeasuring measuring) {
        IotDevice device = deviceStore.get(deviceId);
        if (device == null) throw new IllegalArgumentException("Device not found");

        measuring.setId(UUID.randomUUID().toString());
        measuring.setDeviceId(deviceId);
        measuring.setMeasuringTimestamp(System.currentTimeMillis());

        // Aquí puedes poner reglas específicas según tipo
        double value = Double.parseDouble(measuring.getMeasuringValue());

        boolean isAlert = false;
        switch (measuring.getMeasuringType()) {
            case "temperature":
                if (value > 80 || value < -10) isAlert = true;
                break;
            case "pressure":
                if (value < 20 || value > 120) isAlert = true;
                break;
        }

        measuring.setAlert(isAlert);
        measuring.setStatus(isAlert ? IotMeasuringStatus.ALERT : IotMeasuringStatus.ERROR);

        device.getMeasurings().add(measuring);
        device.setUpdatedAt(System.currentTimeMillis());

        return measuring;
    }

    @Override
    public List<IotDeviceMeasuring> getMeasurings(String deviceId) {
        IotDevice device = deviceStore.get(deviceId);
        if (device == null) return Collections.emptyList();
        return device.getMeasurings();
    }
}
