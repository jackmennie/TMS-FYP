package com.iot.model;

public class Node {
    private int nodeID;

    private Position pos;
    private int energyLevel;
    private double qualityOfRecommendation;
    private ServiceProvider services[];

    private boolean malicious;
    private boolean poorWitnessNode;
    private boolean constrainedNode;

    private double realQualityOfRecommendation;

    public Node(int nodeID, Position pos, int energyLevel, double qualityOfRecommendation, ServiceProvider[] services, boolean malicious, boolean poorWitnessNode, boolean constrainedNode) {
        this.nodeID = nodeID;
        this.pos = pos;
        this.energyLevel = energyLevel;
        this.qualityOfRecommendation = qualityOfRecommendation;
        this.services = services;
        this.malicious = malicious;
        this.poorWitnessNode = poorWitnessNode;
        this.constrainedNode = constrainedNode;
    }

    public String toString() {
        return "[Node: " + nodeID + ", m: " + malicious + ", pw: " + poorWitnessNode
                + ", c: " + constrainedNode;
    }

    public double getQualityOfRecommendation() {
        return qualityOfRecommendation;
    }
}
