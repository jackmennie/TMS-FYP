package com.iot.model;

/*
    Author:         Jack Mennie
    Date:           13.09.18
    Description:    This class will hold all the parameters for the application

*/

public class Parameters {
    //Trust Params
    public static double maxTrust;
    public static double minTrust;

    //Entity selection params
    public static double eta; // η
    public static double theta; // θ in the range [0,1], express the memory of the system
    public static double lambda; // λ in the range [0,1], express the memory of the system

    //Contextual Distance params
    public static double dSMax;
    public static double dCMax;

    public Parameters() {

    }

    public void init() {

    }

    public static void setDefaults() {
        maxTrust = 1.5;
        minTrust = -maxTrust;

        eta = 5;
        theta = 0;
        lambda = 0;

        dSMax = 25;
        dCMax = 15;
    }
}
