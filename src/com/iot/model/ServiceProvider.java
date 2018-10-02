package com.iot.model;

public class ServiceProvider {
    private int serviceID;
    private double capability;

    ServiceProvider(int serviceID, double capability) {
        this.serviceID = serviceID;
        this.capability = capability;
    }

    double getCapability() {
        return capability;
    }

    @Override
    public String toString() {
        return "SP: " + serviceID;
    }
}
