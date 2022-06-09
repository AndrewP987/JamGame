package jam;

import commonJam.solver.Configuration;
import commonJam.solver.Solver;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Jam class that takes in a file and creates the starting config.
 * Then it solves it and prints the steps from start to finish
 * if the solution exists
 *
 * @author Andrew Photinakis
 */
public class Jam {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Jam filename");
        } else {

            String filename = args[0];
            System.out.println("File: " + filename);
            JamConfiguration jamConfig = new JamConfiguration(filename);
            System.out.println(jamConfig);

            Solver solver = new Solver();
            Collection<Configuration> configurationCollection = solver.solve(jamConfig);
            List<Configuration> configurationArrayList = configurationCollection.stream().toList();

        }
    }
}


