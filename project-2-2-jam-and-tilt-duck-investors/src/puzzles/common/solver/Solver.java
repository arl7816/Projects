package puzzles.common.solver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** A class meant to hold the main algorithm behind solving a puzzle configuration using a BFS based
 * search
 * @author Alex Lee
 * */

public class Solver{

    /** the total amount of configurations that are created in the process of solving the puzzle
     * resets after every BFS search
     * */
    private int totalConfigurations; // count the total configurations
    /** the total number of unique configurations created in the process of solving a puzzle
     * Resets after every BFS solve
     * */
    private int uniqueConfigurations;

    /**
     * Gets the total number of configurations after solving a puzzle
     * @return the total amount of configurations
     * */
    public int getTotalConfigurations() {
        return this.totalConfigurations;
    }

    /**
     * Gets the total number of unique configurations created in the process of solving a puzzle
     * @return the total number of unique configurations
     * */
    public int getUniqueConfigurations() {
        return this.uniqueConfigurations;
    }

    /**
     * Given a path, prints out the steps of the given path, along with the total configurations and the
     * unique configurations. If no solution was available, will print out "No solution"
     * (expects that the user has solved the path and prints out immediately after, as the total configurations
     * will not match up if more than one puzzle if solved)
     * @param path: the path of the solution
     * */
    public void printPath(List<Configuration> path){
        System.out.println("Total configs: " + this.getTotalConfigurations());
        System.out.println("Unique configs: " + this.getUniqueConfigurations());

        if (path.size() == 0){
            System.out.println("No solution");
            return;
        }

        String printStatement = "";
        for (int i=0; i<path.size(); i++){
            if (path.get(i) == null){
                System.out.println("No solution");
                return;
            }

            printStatement += "Step " + i + ": " + path.get(i) + "\n";
        }
        System.out.println(printStatement);
    }

    /**
     * Constructs a path leading from the initial configuration and every step to the goal
     * If the size of the path is, then no path exist
     * @param predecessors: every configuration along the path toward the goal
     * @param start: the starting configuration
     * @param end: the end configuration
     * @return the path of steps to from the start to the goal configuration
     * */
    private List<Configuration> constructPath(
            HashMap<Configuration, Configuration> predecessors,
            Configuration start, Configuration end){
        List<Configuration> path = new LinkedList<>();
        if (predecessors.containsKey(end)){
            Configuration current = end;
            while (current == null || !current.equals(start)){
                path.add(0, current);
                current = predecessors.get(current);
            }
            path.add(0, start);
        }

        return path;
    }

    /**
     * Given a starting configuration, will search for and construct a path from the initial configuration
     * to the goal configuration, using a BFS search algorithm.
     * (If the size of the path is 0, no path exist)
     * (Will also cause the totalConfigurations and the uniqueConfigurations to reset)
     * @param start: the starting configuration
     * @return the path of steps to from the start to the goal configuration
     * */
    public List<Configuration> BFSSearch(Configuration start){
        this.totalConfigurations = 1;
        this.uniqueConfigurations = 1; // takes into account the starting configuration

        LinkedList<Configuration> queue = new LinkedList<Configuration>();
        HashMap<Configuration, Configuration> predecessors = new HashMap<Configuration, Configuration>();
        Configuration end = null;

        queue.add(start);
        predecessors.put(start, start);

        while (!queue.isEmpty()){
            Configuration config = queue.remove(0);
            if (config == null){
                continue;
            }

            if (config.isSolution()){
                end = config;
                break;
            }

            // goes through the neighbors
            for (Configuration con: config.getNeighbors()){
                this.totalConfigurations++;
                if (!predecessors.containsKey(con)){
                    predecessors.put(con, config);
                    queue.add(con);
                    this.uniqueConfigurations++;
                }
            }
        }

        return this.constructPath(predecessors, start, end);
    }
}