package commonJam.solver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that creates the common Solver that is used for
 * solving Configuration puzzles
 *
 * @author Andrew Photinakis
 */
public class Solver {
    /**
     * number of configs made
     */
    public static int configcount;
    /**
     * holds the current config
     */
    public static Configuration currConfig;
    /**
     * hashmap of configuration predecessors
     */
    public HashMap<Configuration, Configuration> predecessors;
    /**
     * Solver constructor that starts the config count at 0
     */
    public Solver() {
        configcount = 0;
    }

    /**
     * Calls the start of the BFS algorithm
     *
     * @param solveThis puzzle config that is passed in
     * @return a list of configurations that is the path, if one exists
     */
    public List<Configuration> solve(Configuration solveThis) {
        return this.getShortestPath(solveThis);
    }

    /**
     * First part of the BFS algorithm that makes the predecessor map
     *
     * @param puzzle puzzle that is passed in to be solved in the method above
     * @return a list of configurations that is the path, if one exists
     */
    public List<Configuration> getShortestPath(Configuration puzzle) {

        List<Configuration> queue = new LinkedList<>();
        predecessors = new HashMap<>();
        predecessors.put(puzzle, puzzle);
        queue.add(puzzle);

        while (!queue.isEmpty()) {
            currConfig = queue.remove(0);
            configcount++;
            if (currConfig.isSolution()) {
                return constructPath(predecessors, puzzle, currConfig);
            }
            for (Configuration neighbor : currConfig.getNeighbors()) {
                if (!predecessors.containsKey(neighbor)) {
                    predecessors.put(neighbor, currConfig);
                    queue.add(neighbor);
                    configcount++;
                }
            }
        }
        return new LinkedList<>();
    }

    /**
     * Constructs the shortest path from the start config to the solution if
     * it exists, as found in the method above in the first return statement.
     *
     * @param predecessors  hashmap of predecessors made in the previous method
     * @param puzzleToSolve configuration puzzle to solve
     * @param solution      the solution configuration that was found in the previous method
     * @return the path from the start to finish, if one exists.
     */
    private List<Configuration> constructPath(HashMap<Configuration, Configuration> predecessors,
                                              Configuration puzzleToSolve, Configuration solution) {
        List<Configuration> path = new LinkedList<>();
        if (predecessors.containsKey(solution)) {
            Configuration currentConfig = solution;
            while (currentConfig != puzzleToSolve) {
                path.add(0, currentConfig);
                currentConfig = predecessors.get(currentConfig);
            }
            path.add(0, puzzleToSolve);
        }
        return path;
    }

}
