package org.pe.llantatech.iotservice.model;

public enum IotAction {

    SHUT_DOWN("SHUT_DOWN"),
    RESTART("RESTART"),
    REBOOT("REBOOT");

    private String action;

    IotAction(String action) {
        this.action = action;
    }
}
