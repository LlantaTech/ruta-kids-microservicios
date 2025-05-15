package org.pe.llantatech.iotservice;

import org.junit.jupiter.api.Test;
import org.pe.llantatech.iotservice.model.IotDevice;
import org.pe.llantatech.iotservice.model.IotDeviceMeasuring;
import org.pe.llantatech.iotservice.model.IotMeasuringStatus;
import org.pe.llantatech.iotservice.service.IotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IotServiceApplicationTests {

    @Autowired
    private IotService iotService;

    @Test
    void contextLoads() {
        assertNotNull(iotService);
    }

    @Test
    void testCreateAndGetDeviceById() {
        // Arrange
        IotDevice device = new IotDevice();
        device.setDeviceName("Sensor Humedad");
        device.setSerialNumber("SN001");
        device.setDeviceType("humidity");
        device.setDeviceStatus("active");

        // Act
        IotDevice created = iotService.createDevice(device);
        Optional<IotDevice> found = iotService.getDeviceById(created.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Sensor Humedad", found.get().getDeviceName());
    }

    @Test
    void testAddTemperatureMeasuringWithAlert() {
        // Arrange
        IotDevice device = new IotDevice();
        device.setDeviceName("Temp Sensor");
        device.setSerialNumber("SN-TEMP");
        device.setDeviceType("temperature");
        device.setDeviceStatus("active");

        IotDevice created = iotService.createDevice(device);

        IotDeviceMeasuring measuring = new IotDeviceMeasuring();
        measuring.setMeasuringType("temperature");
        measuring.setMeasuringValue("95");
        measuring.setMeasuringUnit("°C");

        // Act
        IotDeviceMeasuring result = iotService.addMeasuring(created.getId(), measuring);

        // Assert
        assertTrue(result.isAlert());
        assertEquals(IotMeasuringStatus.ALERT, result.getStatus());
    }

    @Test
    void testAddNormalPressureMeasuring() {
        // Arrange
        IotDevice device = new IotDevice();
        device.setDeviceName("Presión Sensor");
        device.setSerialNumber("SN-PRESS");
        device.setDeviceType("pressure");
        device.setDeviceStatus("active");

        IotDevice created = iotService.createDevice(device);

        IotDeviceMeasuring measuring = new IotDeviceMeasuring();
        measuring.setMeasuringType("pressure");
        measuring.setMeasuringValue("50");
        measuring.setMeasuringUnit("kPa");

        // Act
        IotDeviceMeasuring result = iotService.addMeasuring(created.getId(), measuring);

        // Assert
        assertFalse(result.isAlert());
        assertEquals(IotMeasuringStatus.ERROR, result.getStatus());
    }

    @Test
    void testUpdateDevice() {
        // Arrange
        IotDevice device = new IotDevice();
        device.setDeviceName("Dispositivo Test");
        device.setSerialNumber("SN-UPD");
        device.setDeviceType("gas");
        device.setDeviceStatus("inactive");

        IotDevice created = iotService.createDevice(device);

        // Act
        created.setDeviceStatus("active");
        created.setDeviceName("Dispositivo Actualizado");
        IotDevice updated = iotService.updateDevice(created.getId(), created);

        // Assert
        assertEquals("active", updated.getDeviceStatus());
        assertEquals("Dispositivo Actualizado", updated.getDeviceName());
    }

    @Test
    void testDeleteDevice() {
        // Arrange
        IotDevice device = new IotDevice();
        device.setDeviceName("Sensor Eliminar");
        device.setSerialNumber("SN-DEL");
        device.setDeviceType("light");
        device.setDeviceStatus("active");

        IotDevice created = iotService.createDevice(device);

        // Act
        boolean deleted = iotService.deleteDevice(created.getId());
        Optional<IotDevice> result = iotService.getDeviceById(created.getId());

        // Assert
        assertTrue(deleted);
        assertFalse(result.isPresent());
    }
}
