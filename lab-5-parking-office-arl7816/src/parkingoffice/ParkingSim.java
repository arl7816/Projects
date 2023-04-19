package parkingoffice;

import java.io.FileNotFoundException;


/**
 * Holds the ParkingSim class which contains the main method. Is used to control process of the parking simulator.
 * First creates a parking office using the file given, then runs a simulation using another text file
 *
 * Begin day causes resets the day
 * EndDay gives the stats of the day, such as worst offendors and lot usage
 * integers represent a security officer entering a parking lot
 * P simply means the cars afterward are paying there tickets
 * otherwise, it's a car that will either pay off its tickets, gets a ticket, or is allowed in the lot
 * */

public class ParkingSim {
    public static void main(String[] arg) throws FileNotFoundException {
        // only run if all arguments are provided
        if (arg.length != 3){
            return;
        }

        int numLots = Integer.parseInt(arg[0]);
        String carFileName = arg[1];
        String daysFileName = arg[2];

        System.out.println("Usage: java parkingOffice " + numLots + " " + carFileName + " " + daysFileName);

        ParkingOffice parkingOffice = new ParkingOffice(numLots, carFileName);
        parkingOffice.processDays(daysFileName);
    }
}