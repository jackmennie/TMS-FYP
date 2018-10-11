package com.iot.model;

//import org.jetbrains.annotations.NotNull;

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
    PrintStream console;

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
        o = new PrintStream(new File("logFile.dbg"));
        console = System.out;

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
            Report anotherReport = new Report(chooseService(r), chooseCapability(r), chooseNote(r), new Date());
            Report anotherReport2 = new Report(chooseService(r), chooseCapability(r), chooseNote(r), new Date());
            tempList.add(tempReport);
            tempList.add(anotherReport);
            //tempList.add(anotherReport2);
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
    private ServiceProvider chooseService(Random r) {
        return serviceProviders[r.nextInt(serviceProviders.length)];
    }

    /**
     * @param r for choosing a random capability from 0 to 100
     * @return returns the random capability
     */
    private double chooseCapability(Random r) {
        return r.nextDouble()*100; // next double returns a value between 0-1.0
    }

    /**
     * This function chooses either -1, 0 or 1
     * @param r for choosing the random note
     * @return either -1, 0 or 1
     */
    private int chooseNote(Random r) {
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
        System.setOut(console);
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
        //double dS[] = new double[100];
        //double dC[] = 0;
        //double contextualDistance = calculateContextualDistance(dS, dC);

        double dS[][] = new double[nodeList.length][];
        double dC[][] = new double[nodeList.length][];
        double s[][] = new double[nodeList.length][]; //current report service
        double c[][] = new double[nodeList.length][]; //current report capacity

        double sTarget = 0;
        double cTarget = 0;

        for(int i = 0; i < reportlist.size(); i++) {
            //Check if only element in the list
            ArrayList<Report> currentNodeReports = reportlist.get(i);

            dS[i] = new double[currentNodeReports.size()];
            dC[i] = new double[currentNodeReports.size()];

            s[i] = new double[currentNodeReports.size()];
            c[i] = new double[currentNodeReports.size()];

            if(currentNodeReports.size() == 1) {
//                Report rTarget = currentNodeReports.get(0);
//                ServiceProvider sTarget = currentNodeReports.get(0).getService();
//                double cTarget = currentNodeReports.get(0).getCapability();

                dS[i][0] = 0; //no change in service capability
                dC[i][0] = 0; //no change in node capability
            } else {


                for(int j = 0; j < currentNodeReports.size(); j++) {
                    if(j == currentNodeReports.size()-1 ) {
                        System.out.println("===");
                        Report rTarget = currentNodeReports.get(0);
                        sTarget = rTarget.getService().getCapability();
                        cTarget = rTarget.getCapability();
                    } else if((j+1) < currentNodeReports.size()) {
                        System.out.println("test");
                        Report rTarget = currentNodeReports.get(j + 1);
                        sTarget = rTarget.getService().getCapability();
                        cTarget = rTarget.getCapability();
                    }

                    Report currentReport = currentNodeReports.get(j);



                    dS[i][j] = sTarget - currentReport.getService().getCapability();
                    dC[i][j] = cTarget - currentReport.getCapability();

                    s[i][j] = currentReport.getService().getCapability();
                    c[i][j] = currentReport.getCapability();

                    System.out.println("Calculating Ds with: " + sTarget + ", and current: " + currentReport.getService().getCapability());
                    System.out.println("\tDs=" + dS[i][j]);
                    System.out.println("Calculating Dc with: " + cTarget + ", and current: " + currentReport.getCapability());
                    System.out.println("\tDc=" + dC[i][j]);
                }
            }
        }

        //compute contextual distance
        double sMax[] = findMax(s);
        double cMax[] = findMax(c);
        double contextualDistance[][] = calculateContextualDistance(dS, dC, sMax, cMax, s, c, sTarget, cTarget);


        System.out.println(Arrays.deepToString(contextualDistance));

        //Add to the report[i][j]

        for(int i = 0; i < reportlist.size(); i++) {
            ArrayList<Report> reports = reportlist.get(i);
            for(int j = 0; j < reports.size(); j++) {
                Report report = reports.get(j);
                report.setDistance(contextualDistance[i][j]);

                System.out.println(report);
            }
        }


        //step 2.3 Computation of the weights WRij for each retained report ij in the step 2.2

        Date tNow = new Date();
        double weightRij[][] = new double[nodeList.length][];

        for(int i = 0; i < reportlist.size(); i++) {
            weightRij[i] = new double[reportlist.size()];

            ArrayList<Report> currentNodesList = reportlist.get(i);

            for(int j = 0; j < currentNodesList.size(); j++) {
                Report report = currentNodesList.get(j);

                System.out.println("Calculating Weight for report: " + i + j);

                System.out.println("\tTime Now: " + tNow.getTime() + ", Report Time: " + report.getTime().getTime());

                Date diff = new Date(tNow.getTime() - report.getTime().getTime());
                double timeAsDouble = diff.getTime() / 1000.0;
                System.out.println("\tTime: " + timeAsDouble);

                System.out.println("\tNote: " + report.getNote());

                double paramS = report.getNote() == -1.0 ? 1.0 : 0.0;

                System.out.println("\tParamS: " + paramS);

                System.out.println("\tDistance: " + contextualDistance[i][j]);
                weightRij[i][j] = Math.pow(Parameters.lambda, contextualDistance[i][j])
                        * Math.pow(Parameters.theta, (paramS+1) * (timeAsDouble));

                System.out.println("\t\tWeight is: " + weightRij[i][j]);



            }
        }

        //step 2.4 Computation of the trust value Ti for each proxy Pi

        double trustValue = 0;

        //step 2.5 Provision o the best rated proxies Pi

    }



    private double[][] calculateContextualDistance(
            double dS[][], double dC[][], double sMax[], double cMax[], double s[][], double c[][], double sTarget, double cTarget) {

        System.setOut(console);

        System.out.println("dS - ");
        System.out.println(Arrays.deepToString(dS));
        System.out.println("dC - ");
        System.out.println(Arrays.deepToString(dC));
        System.out.println("sMax - ");
        System.out.println(Arrays.toString(sMax));
        System.out.println("cMax - ");
        System.out.println(Arrays.toString(cMax));
        System.out.println("s - ");
        System.out.println(Arrays.deepToString(s));
        System.out.println("c - ");
        System.out.println(Arrays.deepToString(c));
        System.out.println("sTarget - ");
        System.out.println(sTarget);
        System.out.println("cTarget - ");
        System.out.println(cTarget);

        double d[][] = new double[dS.length][];

        for(int i = 0; i < dS.length; i++) {
            d[i] = new double[dS[i].length];
            for(int j = 0; j < dS[i].length; j++) {
                if(dS[i][j] != 0.0 || dC[i][j] != 0.0) {
                    if(dS[i][j] > 0) {
                        d[i][j] = findMin(
                                Math.sqrt((Math.pow(Parameters.dSMax, 2) + Math.pow(Parameters.dCMax, 2)) *
                                        ((dS[i][j] / Parameters.dSMax) + (dC[i][j] / Parameters.dCMax))),

                                Math.sqrt(
                                        (Math.pow(Parameters.dSMax, 2) + Math.pow(Parameters.dCMax, 2))
                                                *
                                                ((Math.pow(((sMax[i] - s[i][j]) / (sMax[i] - (sTarget - Parameters.eta))), 2))
                                                        + (Math.pow((c[i][j] / cTarget + Parameters.eta), 2))))
                        );
                    } else {
                        d[i][j] = findMin(
                                Math.sqrt((Math.pow(Parameters.dSMax, 2) + Math.pow(Parameters.dCMax, 2)) *
                                        ((dS[i][j] / Parameters.dSMax) + (dC[i][j] / Parameters.dCMax))),

                                Math.sqrt(
                                        (Math.pow(Parameters.dSMax, 2) + Math.pow(Parameters.dCMax, 2))
                                                *
                                                ((Math.pow(((cMax[i] - c[i][j]) / (cMax[i] - (cTarget - Parameters.eta))), 2))
                                                        + (Math.pow((s[i][j] / sTarget + Parameters.eta), 2))))
                        );
                    }
                } else {
                    d[i][j] = 0;
                }
                System.out.println("The calculated D is: " + d[i][j]);
                System.out.print("\tDs[" + dS[i][j]);
                System.out.print("], Dc[" + dC[i][j]);
                System.out.print("], cMax[" + cMax[i]);
                System.out.print("], sMax[" + sMax[i]);
                System.out.print("], s[" + s[i][j]);
                System.out.print("], c[" + c[i][j]);
                System.out.print("], sTarget[" + sTarget);
                System.out.print("], cTarget[" + cTarget + "]\n\n");

            }
        }



        return d;
    }
    private double[] findMax(double value[][]) {
        double sMax[] = new double[value.length];

        for(int i = 0; i < value.length; i++) {
            double max = value[i][0];

            for(int j = 0; j < value[i].length; j++) {
                if(value[i][j] > max) {
                    max = value[i][j];
                }
            }

            sMax[i] = max;
        }

        return sMax;
    }

    private double findMin(double valueX, double valueY) {
        if(valueX < valueY) return valueX;
        else return valueY;
    }
}


