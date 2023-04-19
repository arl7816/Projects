package parkingoffice;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Contains the ParkingOffice class which manages all the parking lots and the cars within them
 * Does so by using the processDays function which acts as intended by going through a text file of given events
 * @author Alex Lee
 * */

public class ParkingOffice {
    /**
     * an integer representing the total amount of parking lots
     */
    private int parkingLots;
    /**
     * a set of CarData classes used to keep the ticketed cars pre-sorted
     */
    private TreeSet<CarData> carTree = new TreeSet<CarData>();
    /**
     * a hash map with the licence plate as a key and a CarData object as the value
     */
    private HashMap<String, CarData> cars = new HashMap<String, CarData>();
    /**
     * an array of parking lots
     */
    private LotData[] lots;

    /**
     * resets all of the internal functions within each of the lots
     */
    private void beginDay() {
        //reset each of the parking lots
        for (LotData lot : lots) {
            lot.newDay();
        }
    }

    /**
     * gets the report of every parking lot in the
     *
     * @return every report
     */
    private String getLotInfo() {
        String printStatement = "Lot usage was:\n";
        for (LotData lot : lots) {
            printStatement += lot.report() + "\n";
        }
        return printStatement.strip();
    }

    /**
     * Gets the worst 10 offendors of the day. If there are less than 10, than only displays those
     *
     * @return string representation of each of the worst offending cars
     */
    private String worstCases() {
        String printStatement = "End of day. Worst offenders are:\n";
        int items = 0;
        Iterator<CarData> itr = carTree.descendingIterator();

        while (items < 10 && itr.hasNext()) {
            CarData car = itr.next();

            if (car.getTickets() == 0) {
                break;
            }

            printStatement += car.toString() + "\n";
            items++;
        }
        return printStatement;
    }

    /**
     * Prints out the worst offenders of the day and the lot info
     */
    private void endDay() {
        // prints out the report of the day
        String worst = worstCases();
        String lotInfo = getLotInfo();
        System.out.println("------------\n" + worst + lotInfo);
    }

    /**
     * Converts the txt file into a CarData and puts them in the appropriate data structures
     * each car is separated by a "\n"
     * the parameters of the car are separated by a space (licencePlate allowedParking)
     *
     * @param fileName: the file name of the data to converted into CarData
     */
    private void getInfo(String fileName) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(fileName));

        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            String[] arr = line.split(" ");
            CarData car = new CarData(arr[0], Integer.parseInt(arr[1]));
            carTree.add(car);
            cars.put(car.getLicencePlate(), car);
        }

        scan.close();
    }


    /**
     * processes each of the days events in a file of events as described as such in the main file
     *
     * @param fileName: the file that contains the events
     */
    public void processDays(String fileName) throws FileNotFoundException {
        Scanner scan = new Scanner(new File(fileName));
        boolean mode = true;  // true when an officer is issuing tickets to cars; false when cars are paying off tickets
        int lotNum = 0;

        while (scan.hasNextLine()){
            String line = scan.nextLine();

            if (line.equals("BeginDay")){
                beginDay();
                continue;
            }else if (line.equals("P")){
                mode = false;
                continue;
            }else if (line.equals("EndDay")){
                endDay();
                continue;
            }

            // found an int --> should start ticketing people using that lot
            if (line.matches("\\d*")){
                lotNum = Integer.parseInt(line);
                mode = true;
                continue;
            }

            if (!this.cars.containsKey(line)){
                continue;
            }

            if (mode) {
                if (this.lots[lotNum].sawCar(line)){;
                    if (this.cars.get(line).getAllowedParking() != lotNum) {
                        this.carTree.remove(this.cars.get(line));
                        this.cars.get(line).giveTicket();
                        this.carTree.add(this.cars.get(line));
                    }
                }
            }else{
                this.carTree.remove(this.cars.get(line));
                this.cars.get(line).payTickets();
                this.carTree.add(this.cars.get(line));
            }
        }

        scan.close();
    }

    /**
     * Class constructor for a ParkingOffice
     * @param parkingLots: the total number of parking lots
     * @param fileName: the file containing the CardData parameters
     * */
    public ParkingOffice(int parkingLots, String fileName) throws FileNotFoundException {
        this.getInfo(fileName);
        this.parkingLots = parkingLots;

        this.lots = new LotData[parkingLots];
        for (int i=0; i<this.lots.length; i++){
            this.lots[i] = new LotData(i);
        }
    }
}