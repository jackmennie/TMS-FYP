package com.iot.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TrustManager {
    private Report report;

    private Node nodeList[];
    private Node contrainedNodelist[];
    //private double qualityOfRecommendation;
    private ServiceRequester serviceList[];
    private Parameters model;

    //print stream to file
    PrintStream o;

    /**
     * Step 1: Initialisation
     *      * All nodes are assumed trustworthy
     *      * set up the nodes and set the trustworthy to 1
     *      * It will be assumed that the rep will be 0.
     * @param numNodes = the amount of nodes within the network
     * @param numcNodes = the amount of constrained nodes within the network (which are unable to provide assistance to other nodes)
     * @param numPwNodes = Percentage of poor witness nodes
     * @param numMaliciousNodes = Percentage of malicious nodes
     * @param iQualityofRec = Initial quality of recommendation
     * @param numServices = number of services within the network
     */
    public void init(int numNodes, int numcNodes, double numPwNodes, double numMaliciousNodes, double iQualityofRec, int numServices)
        throws FileNotFoundException {
        o = new PrintStream(new File("out.dbg"));

        System.setOut(o);

        System.out.println("init");
        nodeList = new Node[numNodes];
        contrainedNodelist = new Node[numcNodes];


        serviceList = new ServiceRequester[numServices];

        //Create the total amount of nodes
        int networkSize = nodeList.length + contrainedNodelist.length;

        //get the value of nodes from the percentage
        int poorWitnessNodes = (int)(networkSize * numPwNodes);
        int maliciousNodes = (int)(networkSize * numMaliciousNodes);


        Random r = new Random(System.currentTimeMillis());

        //Generate a list of positions for the poor witness nodes
        ArrayList<Integer> pwNodesPos = generateNodePosition(r, poorWitnessNodes, networkSize);

        //Generate a list of positions for the malicious nodes
        ArrayList<Integer> malNodesPos = generateNodePosition(r, maliciousNodes, networkSize);

        //Create each node in the network
        for(int i = 0; i < nodeList.length; i++) {
            boolean pwNode = false;
            boolean mNode = false;

            for(int j = 0; j < pwNodesPos.size(); j++) {
                if(i == pwNodesPos.get(j)) {
                    pwNode = true;
                }
            }

            for(int j = 0; j < malNodesPos.size(); j++) {
                if(i == malNodesPos.get(j)) {
                    mNode = true;
                }
            }

            //public Node(int nodeID, Position pos, int energyLevel, double qualityOfRecommendation, ServiceRequester[] services, boolean malicious, boolean poorWitnessNode, boolean constrainedNode) {
            Position newPos = new Position(1,1,0);
            nodeList[i] = new Node(i, newPos, 100, iQualityofRec, null, mNode, pwNode, false);
        }


        for(int i = 0; i < pwNodesPos.size(); i++) {
            if(i == 0) {
                System.out.print("Poor witness nodes list: [");
            }

            System.out.print("(" + (i+1) + ") " + pwNodesPos.get(i) + ", ");

            if (i == pwNodesPos.size()-1) {
                System.out.print("]\n\n");
            }
        }

        for(int i = 0; i < malNodesPos.size(); i++) {
            if(i == 0) {
                System.out.print("Malicious nodes list: [");
            }

            System.out.print("(" + (i+1) + ") " + malNodesPos.get(i) + ", ");

            if (i == malNodesPos.size()-1) {
                System.out.print("]\n\n");
            }
        }

        for(int i = 0; i < nodeList.length; i++) {
            System.out.println(nodeList[i].toString());
        }

        System.out.println("Poor witness nodes count: " + poorWitnessNodes);

        PrintStream console = System.out;
        System.setOut(console);
        System.out.println("test");
    }

    /**
     * This function will randomly and uniquely pick a number between 0 and the node list
     * length and then assign it to the node position array.
     * @param r
     * @param amount
     * @param networkSize
     * @return node positions
     */
    private ArrayList<Integer> generateNodePosition(Random r, int amount, int networkSize) {
        ArrayList<Integer> randomNumArray = new ArrayList<>();
        boolean checked = false;

        int position;

        //This is to generate the amount of nodes within the network
        for(int i = 0; i < amount; i++) {
            do {
                position = r.nextInt(networkSize);

                System.out.println("chosen position: " + position);

                if(randomNumArray.size() == 0) {
                    randomNumArray.add(position);
                    checked = true;
                } else {
                    for (int j = 0; j < randomNumArray.size(); j++) {
                        System.out.println("\t(" + j + "): Checking array pos: " + randomNumArray.get(j));
                        System.out.println("\tSize= " + randomNumArray.size());
                        if (randomNumArray.get(j) == position) {
                            //number is found in the array already, pick another.
                            System.out.println("\tPosition is same, picking another...");
                            break;
                        } else if (randomNumArray.get(j) != position) {
                            System.out.println("\tPosition is not same, checking next...");
                        }

                        if (j == randomNumArray.size()-1) {
                            //can assume that the position does not exist in the array
                            // and assign the position
                            System.out.println("\tPosition is not in array, assigning pos");
                            randomNumArray.add(position);
                            checked = true;
                        }
                    }
                }

            } while(!checked);

            checked = false;
        }

        //Before returning, sort the array
        Collections.sort(randomNumArray);

        return randomNumArray;
    }

    /*
        Step 2 Entity Selection
     */
    public void entitySelection() {
        //step 2.1 Restriction of the set of proxies

        //step 2.2 Restriction of the set of Reports Rij for each proxy Pi

        //step 2.3 Computation of the weights WRij for each retained report ij in the step 2.2

        //step 2.4 Computation of the trust value Ti for each proxy Pi

        //step 2.5 Provision o the best rated proxies Pi

    }
}


