package commonJam.solver;

/**
 * Coordinates class that is used to represent and the coordinates of the cars on the game board.
 * It is implemented in algorithms to store and keep track of car positions to therefore know
 * where it can and can't move to.
 *
 * @author Andrew Photinakis
 */
public record Coordinates( int row, int col )  {

    /**
     * Constructor for the class that takes in 2 string numbers and turns then into int values
     *
     * @param rowStr row number
     * @param colStr column number
     */
    public Coordinates( String rowStr, String colStr ) {
        this( Integer.parseInt( rowStr ), Integer.parseInt( colStr ) );
    }

    /**
     * @return row of the Coordinate instance
     */
    public int getRow(){
        return row;
    }

    /**
     * @return column of the Coordinate instance
     */
    public int getCol(){
        return col;
    }

    /**
     * @return string representation of the Coordinate instance
     */
    @Override
    public String toString() {
        return "(" + this.row + ',' + this.col + ')';
    }

}
