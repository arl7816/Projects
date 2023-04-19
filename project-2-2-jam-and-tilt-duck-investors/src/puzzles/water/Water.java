package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Alex Lee
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            Solver solve = new Solver();

            ArrayList<Integer> caps = new ArrayList<Integer>();
            for (int i=1; i< args.length; i++){
                caps.add(Integer.parseInt(args[i]));
            }

            WaterConfig start = new WaterConfig(caps, Integer.parseInt(args[0]));

            List<Configuration> path = solve.BFSSearch(start);

            System.out.println("Amount: " + start.getGoal() + ", Buckets: " + start.stringCapacities());
            solve.printPath(path);
        }
    }
}
