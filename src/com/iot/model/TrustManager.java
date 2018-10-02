package com.iot.model;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.*;

public class TrustManager {
    private ArrayList<ArrayList<Report>> reportlist;

    private Report rTarget;

    private Node nodeList[];

    private ServiceProvider serviceProviders[];
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
        PrintStream console = System.out;

        System.setOut(o);

        serviceProviders = new ServiceProvider[numServices];

        //Create the total amount of nodes
        int networkSize = numNodes + numcNodes;
        nodeList = new Node[networkSize];

        //get the value of nodes from the percentage
        int poorWitnessNodes = (int)(networkSize * numPwNodes);
        int maliciousNodes = (int)(networkSize * numMaliciousNodes);


        Random r = new Random(System.currentTimeMillis());

        for(int i = 0; i < numServices; i++) {
            serviceProviders[i] = new ServiceProvider(i, chooseCapability(r));
        }

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

        //create initial reports
        reportlist = new ArrayList<>();

        for (Node i : nodeList) {
            //System.out.println(i.toString());

            ArrayList<Report> tempList = new ArrayList<>();
            Report tempReport = new Report(chooseService(r), chooseCapability(r), chooseNote(r), new Date());

            tempList.add(tempReport);
            reportlist.add(tempList);
        }

        for(ArrayList<Report> list : reportlist) {
            System.out.println(list);
//            for(Report report : list) {
//                System.out.println(report.toString());
//            }
        }


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


    /**
     * @param r for choosing a random service from the list
     * @return a random service from the provided list
     */
    private ServiceProvider chooseService(@NotNull Random r) {
        return serviceProviders[r.nextInt(serviceProviders.length)];
    }

    /**
     * @param r for choosing a random capability from 0 to 100
     * @return returns the random capability
     */
    private double chooseCapability(@NotNull Random r) {
        return r.nextDouble()*100; // next double returns a value between 0-1.0
    }

    /**
     * This function chooses either -1, 0 or 1
     * @param r for choosing the random note
     * @return either -1, 0 or 1
     */
    private int chooseNote(@NotNull Random r) {
        List<Integer> noteRange = new ArrayList<>();
        noteRange.add(-1);
        noteRange.add(0);
        noteRange.add(1);

        return noteRange.get(r.nextInt(noteRange.size()));
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

            ArrayList<Report> currentNode = reportlist.get(0);

            ServiceProvider sTarget = currentNode.get(0).getService(); //Current service in request
            double cTarget = currentNode.get(0).getCapability(); //Current node capability

        ArrayList<Report> rTarget = reportlist.get(1); //next report

        double dS[] = [];
        double dC = 0;

        double contextualDistance = calculateContextualDistance(dS, dC);


        //step 2.3 Computation of the weights WRij for each retained report ij in the step 2.2

        //step 2.4 Computation of the trust value Ti for each proxy Pi

        //step 2.5 Provision o the best rated proxies Pi

    }



    private double calculateContextualDistance(double dS, double dC) {
        double temp = 0.0;


        return temp;
    }

    private double findMin(double valueX, double valueY) {
        if(valueX < valueY) return valueX;
        else return valueY;
    }
}


