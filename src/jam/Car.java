package jam;

/**
 * Car class that is used to make objects to represent each
 * car on the Jam game board
 *
 * @author Andrew Photinakis
 */
public class Car {

    /**
     * holds the car name
     */
    private String carName;
    /**
     * holds the starting row
     */
    private int startingRow;
    /**
     * holds the starting column
     */
    private int startingCol;
    /**
     * holds the ending row
     */
    private int endingRow;
    /**
     * holds the ending column
     */
    private int endingCol;

    /**
     * Constructor that creates the car object
     *
     * @param carName     the name of the car
     * @param startingRow the starting row of the car
     * @param startingCol the starting column of the car
     * @param endingRow   the ending row of the car
     * @param endingCol   the ending column of the car
     */
    public Car(String carName, int startingRow, int startingCol, int endingRow, int endingCol) {
        this.carName = carName;
        this.startingRow = startingRow;
        this.startingCol = startingCol;
        this.endingRow = endingRow;
        this.endingCol = endingCol;
    }

    /**
     * Does the car move sideways?
     * Checks the starting row and ending row
     *
     * @return true the car does move sideways
     * if the statement is true false otherwise
     */
    public boolean movesSideways() {
        return this.startingRow == this.endingRow;
    }

    /**
     * @return Getter method that returns starting row
     */
    public int getStartingRow() {
        return this.startingRow;
    }

    /**
     * @return Getter method that returns starting column
     */
    public int getStartingCol() {
        return this.startingCol;
    }

    /**
     * @return Getter method that returns ending row
     */
    public int getEndingRow() {
        return this.endingRow;
    }

    /**
     * @return Getter method that returns ending column
     */
    public int getEndingCol() {
        return this.endingCol;
    }

    /**
     * @return Getter method that returns the car name
     */
    public String getCarName() {
        return this.carName;
    }

}
