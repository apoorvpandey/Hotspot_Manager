package com.modernboyz.hotspotmanager.Model;

public class IPAndMacModel {
    private String ipAddress, macAddress;

    public IPAndMacModel(String ipAddress, String macAddress) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }

    public IPAndMacModel() {
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
