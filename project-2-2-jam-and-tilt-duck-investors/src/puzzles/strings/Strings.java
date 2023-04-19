package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.strings.StringConfig;

import java.util.List;

/**
 * Main class for the strings puzzle.
 *
 * @author Alex Lee
 */
public class Strings{
    /**
     * Run an instance of the strings puzzle.
     *
     * @param args [0]: the starting string;
     *             [1]: the finish string.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            Solver solve = new Solver();
            StringConfig start = new StringConfig(args[0], args[1]);
            //StringConfig end = new StringConfig(args[1], args[1]);

            List<Configuration> path = solve.BFSSearch(start);

            System.out.println("Start: " + start.getContent() + ", End: " + start.getGoal());
            solve.printPath(path);
        }
    }
}
