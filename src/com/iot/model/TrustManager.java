package com.iot.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TrustManager {
    private Node nodeList[];
    private Node contrainedNodelist[];
    private double qualityOfRecommendation;

    private Service serviceList[];

    private Parameters model;
    //Step 1 (Information Gathering
    /*
        Nodes set to trustworthy [1]
        Rep would be 0
     */

    //Step 2 (Entity Selection)

    //Step 3 (Transaction)

    //Step 4 (Reward and Punish)

    //Step 5 (Learning)

    /**
     * Step 1: Initialisation
     * All nodes are assumed trustworthy
     * set up the nodes and set the trustworthy to 1
     * It will be assumed that the rep will be 0.
     *
     * numNodes = the amount of nodes within the network
     * numcNodes = the amount of constrained nodes within the network (which are unable to provide assistance to other nodes)
     * numPwNodes = Percentage of poor witness nodes
     * numMaliciousNodes = Percentage of malicious nodes
     * iQualityofRec = Initial quality of recommendation
     * numServices = number of services within the network
     */
    public void init(int numNodes, int numcNodes, double numPwNodes, double numMaliciousNodes, double iQualityofRec, int numServices) {
        System.out.println("init");
        nodeList = new Node[numNodes];
        contrainedNodelist = new Node[numcNodes];

        qualityOfRecommendation = iQualityofRec;

        serviceList = new Service[numServices];

        //Create the amount of
        int networkSize = nodeList.length + contrainedNodelist.length;
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

            nodeList[i] = new Node(i, mNode, pwNode, false);
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



        //Initialise each node
        for(int i = 0; i < nodeList.length; i++) {

        }
    }
    
    private ArrayList<Integer> generateNodePosition(Random r, int amount, int networkSize) {
        ArrayList<Integer> randomNumArray = new ArrayList<>();
        boolean checked = false;

        int position;

        //This is to generate the amount of poor witness nodes within the network
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
}


