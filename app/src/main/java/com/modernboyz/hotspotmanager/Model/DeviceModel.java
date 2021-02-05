package com.modernboyz.hotspotmanager.Model;

public class DeviceModel {
    String name, macAddress;

    public DeviceModel() {
    }

    public DeviceModel(String name, String macAddress) {
        this.name = name;
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }
}
