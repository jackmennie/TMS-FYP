package com.iot.model;

public class Node {
    private int nodeID;
    private boolean malicious;
    private boolean poorWitnessNode;
    private boolean constrainedNode;

    public Node(int id, boolean malicious, boolean pwNode, boolean cNode) {
        this.nodeID = id;
        this.malicious = malicious;
        this.poorWitnessNode = pwNode;
        this.constrainedNode = cNode;
    }

    public String toString() {
        return "[Node: " + nodeID + ", m: " + malicious + ", pw: " + poorWitnessNode
                + ", c: " + constrainedNode;
    }
}
