package com.iot.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class TrustManager {
    private ArrayList<ArrayList<Report>> reportlist;
    private Report rTarget;

    private Node nodeList[];

    private ServiceProvider serviceList[];
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
     * @param iQualityOfRec = Initial quality of recommendation
     * @param numServices = number of services within the network
     */
    public void init(int numNodes, int numcNodes, double numPwNodes, double numMaliciousNodes, double iQualityOfRec, int numServices)
        throws FileNotFoundException {
        o = new PrintStream(new File("out.dbg"));

        //System.setOut(o);

        serviceList = new ServiceProvider[numServices];

        //Create the total amount of nodes
        int networkSize = numNodes + numcNodes;
        nodeList = new Node[networkSize];

        //get the value of nodes from the percentage
        int poorWitnessNodes = (int)(networkSize * numPwNodes);
        int maliciousNodes = (int)(networkSize * numMaliciousNodes);


        Random r = new Random(System.currentTimeMillis());

        //Generate a list of positions for the poor witness nodes
        ArrayList<Integer> pwNodesPos = generateNodePosition(r, poorWitnessNodes, networkSize);
        //Generate a list of positions for the malicious nodes
        ArrayList<Integer> malNodesPos = generateNodePosition(r, maliciousNodes, networkSize);
        //Generate a list where to position the constrained nodes
        ArrayList<Integer> constrainedNodesPos = generateNodePosition(r, numcNodes, networkSize);

        //Create each node in the network
        for(int i = 0; i < networkSize; i++) {
            boolean isPoorWitnessNode = false;
            boolean isMaliciousNode = false;
            boolean isConstrainedNode = false;

            for (Integer pwNodesPo : pwNodesPos) {
                if (i == pwNodesPo)
                    isPoorWitnessNode = true;
            }

            for (Integer malNodesPo : malNodesPos) {
                if (i == malNodesPo)
                    isMaliciousNode = true;
            }

            for (Integer constrainedNodesPo : constrainedNodesPos) {
                if(i == constrainedNodesPo)
                    isConstrainedNode = true;
            }

            //public Node(int nodeID, Position pos, int energyLevel, double qualityOfRecommendation, ServiceProvider[] services, boolean malicious, boolean poorWitnessNode, boolean constrainedNode) {
            Position newPos = new Position(1,1,0);
            nodeList[i] = new Node(i, newPos, 100, iQualityOfRec, null, isMaliciousNode, isPoorWitnessNode, isConstrainedNode);
        }

        for (Node i : nodeList) {
            System.out.println(i.toString());
        }

        //create initial report
        reportlist = new ArrayList<>();

        ArrayList<Report> tempList = new ArrayList<>();
        
        Report newReport = new Report(serviceList[0], 100, 2, new Date());


        PrintStream console = System.out;
        System.setOut(console);
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

                //System.out.println("chosen position: " + position);

                if(randomNumArray.size() == 0) {
                    randomNumArray.add(position);
                    checked = true;
                } else {
                    for (int j = 0; j < randomNumArray.size(); j++) {
                        //System.out.println("\t(" + j + "): Checking array pos: " + randomNumArray.get(j));
                        //System.out.println("\tSize= " + randomNumArray.size());
                        if (randomNumArray.get(j) == position) {
                            //number is found in the array already, pick another.
                            //System.out.println("\tPosition is same, picking another...");
                            break;
                        } else if (randomNumArray.get(j) != position) {
                            //System.out.println("\tPosition is not same, checking next...");
                        }

                        if (j == randomNumArray.size()-1) {
                            //can assume that the position does not exist in the array
                            // and assign the position
                            //System.out.println("\tPosition is not in array, assigning pos");
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
            //we don't need to restrict at this current time.
        //step 2.2 Restriction of the set of Reports Rij for each proxy Pi
            /*
                Service target is the current service in request
                Capability target is the current Node Capability

                Rtarget is the next report to be received

                context similarity between a report about previous interaction and present target report is considering
                a global contextual distance dij
             */

        //step 2.3 Computation of the weights WRij for each retained report ij in the step 2.2

        //step 2.4 Computation of the trust value Ti for each proxy Pi

        //step 2.5 Provision o the best rated proxies Pi

    }


//    private ArrayList<Double> calculateContextualDistance() {
//        ArrayList<Double> temp ;
//
//
//        return temp;
//    }
}


