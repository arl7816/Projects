package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Holds the main algorithm for solving the Tilt puzzle using a BFS search.
 * If no solution exist, then it will notify the user
 * @author Alex Lee
 * */
public class Tilt {
    public static void main(String[] args) throws IOException{
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }else{
            System.out.println("Starting the program");
            TiltConfig start = new TiltConfig(args[0]);
            Solver solver = new Solver();
            System.out.print("File: " + args[0]);
            System.out.print(start.toString());
            List<Configuration> path = solver.BFSSearch(start);
           solver.printPath(path);
        }
    }
}
