package com.iot.model;

import java.util.Date;

public class Report {
    private ServiceProvider service;
    private double capability; //capability of node Pi when assisting the service
    private int note; //score given by the requester node to Pi for evaluating the offered service. {-1,0,1}
    private Date time; //time at which the service was obtained.

    public Report(ServiceProvider service, double capability, int note, Date time) {
        this.service = service;
        this.capability = capability;
        this.note = note;
        this.time = time;
    }
}
