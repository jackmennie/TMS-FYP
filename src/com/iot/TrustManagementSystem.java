package com.iot;

/*
    Author:         Jack Mennie
    Date:           13.09.18
    Description:    This class will be the input class for the user.

*/

import com.iot.io.FileDialog;
import com.iot.io.FileIO;
import com.iot.model.Parameters;
import com.iot.model.TrustManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class TrustManagementSystem {
    Scanner input;              //Used for input
    boolean running = true;    //Ensure the application is continuing to run until user quits

    TrustManager TM;    //Trust Manager Object


    public static void main (String args[])  {
        TrustManagementSystem TMS = new TrustManagementSystem();
        TMS.input = new Scanner(System.in);


        TMS.menu();
    }

    private void menu() {
        //continue running the menu until user quits the application
        while(running) {
            System.out.println("Welcome to the Trust Management System\n");

            if(TM == null) {
                //User must set up the trust manager
                TM = new TrustManager();
                /* Display user to enter in existing trust model
                   or use existing defaults to configure
                */

                System.out.println("The system has not been set up yet, would you like to: " +
                        "\n\t1. Import Trust Model\n\t2. Use the default Trust Model and configure");

                boolean valid = false;

                while(!valid) {
                    System.out.print("\t=> ");
                    String option = input.next();
                    switch(option) {
                        case "1":
                            //Import model
                            importModel();
                            valid = true;
                            break;
                        case "2":
                            //Use Default Model
                            initTrustModel();
                            valid = true;
                            break;
                        default: System.out.println("Invalid Option"); break;
                    }
                }
            } else {
                //Trust manager has been initialised and assumed that user is running simulation again

                System.out.println("Select an option: \n\t1. Import Trust Model\n\t2. Configure Trust Model" +
                        "\n\t3. Run Simulation\n\t4. Retrieve Results\n\t5. Save Trust Model" +
                        "\n\t6. Quit");
                System.out.print("\t=> ");

                String option = input.next();
                switch(option) {
                    case "1":
                        //import
                        break;
                    case "2":
                        //configure
                        break;
                    case "3":
                        //run
                        runSimulation();
                        break;
                    case "4":
                        //results
                        break;
                    case "5":
                        //save model
                        break;
                    case "6":
                        //exit
                        System.exit(0);
                        break;
                    default: System.out.println("Invalid option, Please select an option from 1-6"); break;
                }
            }
        }
    }

    //Opens the modelDialog
    private void importModel() {
        //imports a trust model

        FileIO importTM = new FileIO();

        FileDialog dialog = new FileDialog();
        System.out.println("Please select the file");
        System.out.println(importTM.openTrustModel(dialog.getFile()));
    }

    private void initTrustModel() {
        Parameters.setDefaults();
        TM = new TrustManager();

        try {
            TM.init(200, 100, 0.2, 0.1, 1, 6);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void runSimulation() {
        TM.entitySelection();
    }
}
