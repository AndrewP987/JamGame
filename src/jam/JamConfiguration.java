package jam;

import commonJam.solver.Configuration;
import commonJam.solver.Coordinates;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Class that creates JamConfiguration that are used for the
 * Strings puzzle class to implement
 *
 * @author Andrew Photinakis
 */
public class JamConfiguration implements Configuration {

    /**
     * holds car name and its attributes
     */
    public TreeMap<String, Car> stringCarTreeMap;
    /**
     * holds the game board
     */
    public char[][] gameBoard;
    /**
     * holds game board number of rows
     */
    public static int numRows;
    /**
     * holds game board number of columns
     */
    public static int numCols;
    /**
     * char for filing empty board
     */
    public static final char EMPTY = '.';
    /**
     * name of car
     */
    private String carName;
    /**
     * starting row of car
     */
    private int startingRow;
    /**
     * starting col of car
     */
    private int startingCol;
    /**
     * ending row of car
     */
    private int endingRow;
    /**
     * ending col of car
     */
    private int endingCol;

    /**
     * JamConfig constructor that creates the configuration.
     * Means it creates the board by reading the file by line,
     * creates the cars as necessary and stores them in a tree map,
     * and plots the cars on the game board for play
     *
     * @param filename game file that is passed in
     * @throws IOException for a potential FileNotFoundException
     */
    public JamConfiguration(String filename) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filename));
        String[] fields = br.readLine().split(" ");
        numRows = Integer.parseInt(fields[0]);
        numCols = Integer.parseInt(fields[1]);
        this.gameBoard = new char[numRows][numCols];

        String[] cars = br.readLine().split(" ");
        int numCars = Integer.parseInt(cars[0]);

        /** Creates the tree map as well as each car in the file and puts it in the tree map */
        this.stringCarTreeMap = new TreeMap<>();
        String line;
        String[] lineSplited;
        while ((line = br.readLine()) != null) {
            lineSplited = line.split(" ");
            this.carName = lineSplited[0];
            this.startingRow = Integer.parseInt(lineSplited[1]);
            this.startingCol = Integer.parseInt(lineSplited[2]);
            this.endingRow = Integer.parseInt(lineSplited[3]);
            this.endingCol = Integer.parseInt(lineSplited[4]);
            Car car = new Car(this.carName, this.startingRow, this.startingCol,
                    this.endingRow, this.endingCol);
            this.stringCarTreeMap.put(this.carName, car);
        }

        /** initially fills the game board with empty dots */
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                this.gameBoard[row][col] = EMPTY;
            }
        }

        /** plots each car on the game board respective to its starting and ending values */
        for (String currString : this.stringCarTreeMap.keySet()) {
            Car currCar = this.stringCarTreeMap.get(currString);
            int startingRow = currCar.getStartingRow();
            int startingCol = currCar.getStartingCol();
            int endingRow = currCar.getEndingRow();
            int endingCol = currCar.getEndingCol();

            if (currCar.movesSideways()) {
                for (int c = startingCol; c <= endingCol; c++) {
                    this.gameBoard[startingRow][c] = currString.charAt(0);
                }
            } else {
                for (int r = startingRow; r <= endingRow; r++) {
                    this.gameBoard[r][startingCol] = currString.charAt(0);
                }
            }
        }
    }

    /**
     * JamConfig copy constructor that takes in the other JamConfig as well as the car
     * and an ArrayList of the updated coordinates for that car
     *
     * @param other              the other Jam Configuration that is passed in
     * @param car                the car that's position is to be changed on the game board
     * @param updatedCoordinates the new coordinates that represent the new positions
     */
    public JamConfiguration(JamConfiguration other, Car car, ArrayList<Coordinates> updatedCoordinates) {
        this.gameBoard = new char[numRows][numCols];
        this.stringCarTreeMap = other.stringCarTreeMap;

        /** copies the old game board onto the new one before changes are made */
        for (int r = 0; r < numRows; r++) {
            if (numCols >= 0) System.arraycopy(other.gameBoard[r], 0, this.gameBoard[r], 0, numCols);
        }

        /** goes through the new game board and any spot that holds the car is now empty */
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (this.gameBoard[i][j] == car.getCarName().charAt(0)) {
                    this.gameBoard[i][j] = EMPTY;
                }
            }
        }

        /**
         * gets the first and last coords of the new car and replaces its object
         * in the tree map
         */
        Coordinates first = updatedCoordinates.get(0);
        Coordinates last = updatedCoordinates.get(updatedCoordinates.size() - 1);
        int startingRow = first.getRow();
        int startingCol = first.getCol();
        int endingRow = last.getRow();
        int endingCol = last.getCol();
        this.stringCarTreeMap.replace(car.getCarName(), new Car(car.getCarName(), startingRow, startingCol
                , endingRow, endingCol));

        /** plots the new car along with its new coordinates on the game board */
        updatedCoordinates.forEach(currCord -> {
            this.gameBoard[currCord.getRow()][currCord.getCol()] = car.getCarName().charAt(0);
        });
    }

    /**
     * Gets the neighbors of the current Jam Configuration
     *
     * @return Collection of configuration neighbors that are generated
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new LinkedList<>();

        for (String currentStringKeySet : this.stringCarTreeMap.keySet()) {
            Car currentCar = this.stringCarTreeMap.get(currentStringKeySet);
            ArrayList<Coordinates> currCarCordsArrayList = this.getAllCarCoordinates(currentCar);

            if (currentCar.movesSideways()) {
                if (currCarCordsArrayList.get(currCarCordsArrayList.size() - 1).getCol() < numCols - 1) {
                    if (this.gameBoard[currentCar.getStartingRow()]
                            [currCarCordsArrayList.get(currCarCordsArrayList.size() - 1).getCol() + 1] == EMPTY) {
                        ArrayList<Coordinates> newCordsToPassIn = new ArrayList<>();
                        currCarCordsArrayList.forEach(currCord -> {
                            newCordsToPassIn.add(new Coordinates(currCord.getRow(), currCord.getCol() + 1));
                        });
                        neighbors.add(new JamConfiguration(this, currentCar, newCordsToPassIn));
                    }
                }
                if (currCarCordsArrayList.get(0).getCol() > 0) {
                    if (this.gameBoard[currentCar.getStartingRow()]
                            [currCarCordsArrayList.get(0).getCol() - 1] == EMPTY) {
                        ArrayList<Coordinates> newCordsToPassIn = new ArrayList<>();
                        currCarCordsArrayList.forEach(currCord -> {
                            newCordsToPassIn.add(new Coordinates(currCord.getRow(), currCord.getCol() - 1));
                        });
                        neighbors.add(new JamConfiguration(this, currentCar, newCordsToPassIn));
                    }
                }
            }

            if (!currentCar.movesSideways()) {
                if (currCarCordsArrayList.get(currCarCordsArrayList.size() - 1).getRow() < numRows - 1) {
                    if (this.gameBoard[currCarCordsArrayList.get(currCarCordsArrayList.size() - 1).getRow() + 1]
                            [currentCar.getStartingCol()] == EMPTY) {
                        ArrayList<Coordinates> newCordsToPassIn = new ArrayList<>();
                        currCarCordsArrayList.forEach(currCord -> {
                            newCordsToPassIn.add(new Coordinates(currCord.getRow() + 1, currCord.getCol()));
                        });
                        neighbors.add(new JamConfiguration(this, currentCar, newCordsToPassIn));
                    }
                }
                if (currCarCordsArrayList.get(0).getRow() > 0) {
                    if (this.gameBoard[currCarCordsArrayList.get(0).getRow() - 1]
                            [currentCar.getStartingCol()] == EMPTY) {
                        ArrayList<Coordinates> newCordsToPassIn = new ArrayList<>();
                        currCarCordsArrayList.forEach(currCord -> {
                            newCordsToPassIn.add(new Coordinates(currCord.getRow() - 1, currCord.getCol()));
                        });
                        neighbors.add(new JamConfiguration(this, currentCar, newCordsToPassIn));
                    }
                }
            }
        }
        return neighbors;
    }

    /**
     * Getter method
     *
     * @param currCar is the car that is passed in to find all its positions
     * @return ArrayList of all the coordinates of the car that is passed in
     */
    public ArrayList<Coordinates> getAllCarCoordinates(Car currCar) {
        ArrayList<Coordinates> coordinatesArrayList = new ArrayList<>();
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (this.gameBoard[r][c] == currCar.getCarName().charAt(0)) {
                    coordinatesArrayList.add(new Coordinates(r, c));
                }
            }
        }
        return coordinatesArrayList;
    }

    /**
     * Is this current config a solution?
     * Is the "X" car at the last column and out of traffic?
     *
     * @return true if the "X" car is out of traffic
     */
    @Override
    public boolean isSolution() {
        Car currCar = this.stringCarTreeMap.get("X");
        ArrayList<Coordinates> carCords = this.getAllCarCoordinates(currCar);
        Coordinates lastCord = carCords.get(carCords.size() - 1);
        Coordinates endCord = new Coordinates(currCar.getStartingRow(), numCols - 1);
        return lastCord.equals(endCord);
    }

    /**
     * Passes in other Object and checks if their game boards are the same
     *
     * @param other instance of JamConfig class
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof JamConfiguration otherJamConfig) {
            result = Arrays.deepEquals(this.gameBoard, otherJamConfig.gameBoard);
        }
        return result;
    }

    /**
     * Prints the game board
     *
     * @return string version of the game board which includes numbered rows and columns
     */
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        res.append("   ");
        for (int col = 0; col < this.getNumCols(); ++col) {
            res.append(col).append(" ");
        }
        res.append(System.lineSeparator());

        res.append("  ");
        for (int col = 0; col < this.getNumCols(); ++col) {
            res.append("--");
        }
        res.append(System.lineSeparator());

        for (int i = 0; i < numRows; i++) {
            res.append(i).append("| ");
            for (int j = 0; j < numCols; j++) {
                res.append(this.gameBoard[i][j]).append(" ");
            }
            res.append(System.lineSeparator());
        }

        return res.toString();
    }

    /**
     * Gets the hashcode for the game board
     *
     * @return int hashcode
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.gameBoard);
    }

    /**
     * @return char that is at the certain row, col input
     */
    public char getCharAtCord(int row, int col) {
        return this.gameBoard[row][col];
    }

    /**
     * @return int number of rows
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * @return int number of columns
     */
    public int getNumCols() {
        return numCols;
    }

}
