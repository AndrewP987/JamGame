package jam;

import commonJam.Observer;
import commonJam.solver.Configuration;
import commonJam.solver.Coordinates;
import commonJam.solver.Solver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static jam.JamConfiguration.EMPTY;
/**
 * Class for creating the JamModel which will hold the
 * needs for the game.
 *
 * @author Andrew Photinakis
 */
public class JamModel {

    /**
     * the collection of observers of this model
     */
    private final List<Observer<JamModel, String>> observers = new LinkedList<>();

    /**
     * Game states that may be used to represent different states of the game
     */
    public enum GameState {
        ONGOING, ALREADY_SOLVED, RESET, QUIT, STARTING_UP, LOADING, HINT, SELECTION, ILLEGAL_SELECTION
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<JamModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    public void notifyObservers(String message) throws IOException {
        for (Observer<JamModel, String> obs : this.observers) {
            obs.update(this, message);
        }
    }

    /**
     * Holds the game state
     */
    private GameState gameState;
    /**
     * Holds the jam configuration
     */
    private JamConfiguration jamConfig;
    /**
     * Holds the coordinates
     */
    private Coordinates cords = null;
    /**
     * Holds the game file
     */
    private String gameFile;

    /**
     * Constructor for the JamModel that takes in a game file
     *
     * @param gameFile file for the model
     * @throws IOException if the file is not found when inputed
     */
    public JamModel(String gameFile) throws IOException {
        this.gameFile = gameFile;
        this.jamConfig = new JamConfiguration(this.gameFile);
        this.gameState = GameState.ONGOING;
    }

    /**
     * Handles the hint method. When the method is called,
     * if the current configuration state of the puzzle is
     * solvable, the puzzle will advance to the next step.
     * If it is not solvable, then notifyObservers is called
     *
     * @throws IOException if the file is not valid
     */
    public void hint() throws IOException {
        if (this.jamConfig.isSolution()) {
            this.gameState = GameState.ALREADY_SOLVED;
            this.notifyObservers("Already solved!");
        } else {
            Solver solver = new Solver();
            List<Configuration> configurationCollection = solver.solve(this.jamConfig);
            if(configurationCollection.isEmpty()){
                this.notifyObservers("No solution");
            }else{
                this.jamConfig = (JamConfiguration) configurationCollection.get(1);
                this.gameState = GameState.HINT;
                this.notifyObservers("Next Step!");
            }
        }
    }

    /**
     * Handles the load method. When the method is called,
     * a file name is passed in to be the new game file.
     * If the file is not valid and can't be loaded,
     * that will be notified. Else, the file will be loaded,
     * the jam config will be updated and so will the board.
     *
     * @param filename game file to be changed to
     * @throws IOException if the file is not valid
     */
    public void load(String filename) throws IOException {
        try {
            this.jamConfig = new JamConfiguration(filename);
            this.gameState = GameState.LOADING;
            String[] gameFileSplit = filename.split("/");
            this.gameFile = filename;
            this.notifyObservers("Loaded: " + gameFileSplit[gameFileSplit.length - 1]);
        } catch (Exception e) {
            this.notifyObservers("Failed to load: " + filename);
        }
    }

    /**
     * Sets the game state to Ongoing
     */
    public void setGameStateONGOING() {
        this.gameState = GameState.ONGOING;
    }

    /**
     * Resets the puzzle by loading it first,
     * then setting the gamestate to reset and
     * notifying observers of the puzzle reset.
     *
     * @throws IOException if the file is not valid
     */
    public void reset() throws IOException {
        this.load(this.gameFile);
        this.gameState = GameState.RESET;
        this.notifyObservers("Puzzle reset! ");
    }

    /**
     * Quits the program and terminates its run.
     * Sets the game state to quit and
     * notifies the observers with a goodbye message
     *
     * @throws IOException if the file is not valid
     */
    public void quit() throws IOException {
        this.gameState = GameState.QUIT;
        this.notifyObservers("Quitting the game...Come back soon!");
        System.exit(0);
    }

    /**
     * Selection handles selecting the cars to move.
     *
     * @param xCord row of the char on the game board
     * @param yCord column of the char on the game board
     * @throws IOException if the file is not valid
     */
    public void selection(int xCord, int yCord) throws IOException {
        if (this.gameState == GameState.ONGOING && cords == null) {
            Coordinates guessCords = new Coordinates(xCord, yCord);
            if (this.jamConfig.gameBoard[guessCords.getRow()][guessCords.getCol()] == EMPTY) {
                this.notifyObservers("No car at " + guessCords);
            } else {
                cords = guessCords;
                this.notifyObservers("Selected " + guessCords);
            }
        } else if (this.gameState == GameState.ONGOING) {
            Coordinates secondCords = new Coordinates(xCord, yCord);
            String car = String.valueOf(this.jamConfig.gameBoard[cords.getRow()][cords.getCol()]);
            Car curr = this.jamConfig.stringCarTreeMap.get(car);

            if (curr.movesSideways()) {
                for (int col = curr.getStartingCol(); col <= secondCords.getCol(); col++) {
                    if (this.jamConfig.gameBoard[curr.getStartingRow()][col] != EMPTY
                            && this.jamConfig.gameBoard[curr.getStartingRow()][col] != curr.getCarName().charAt(0)) {
                        this.notifyObservers("Can't move from " + cords + " to " + secondCords);
                        cords = null;
                        return;
                    }
                }

                for (int col = curr.getStartingCol(); col >= secondCords.getCol(); col--) {
                    if (this.jamConfig.gameBoard[curr.getStartingRow()][col] != EMPTY
                            && this.jamConfig.gameBoard[curr.getStartingRow()][col] != curr.getCarName().charAt(0)) {
                        this.notifyObservers("Can't move from " + cords + " to " + secondCords);
                        cords = null;
                        return;
                    }
                }

                if (cords.getRow() != secondCords.getRow()) {
                    this.notifyObservers("Can't move from " + cords + " to " + secondCords);
                    cords = null;
                    return;
                }

                ArrayList<Coordinates> currCarCords = this.jamConfig.getAllCarCoordinates(curr);
                Coordinates firstCurrCords = currCarCords.get(0);
                Coordinates lastingCurrCords = currCarCords.get(currCarCords.size() - 1);
                ArrayList<Coordinates> toPassIn = new ArrayList<>();

                if (secondCords.getCol() < firstCurrCords.getCol()) {
                    int distanceToMove = firstCurrCords.getCol() - secondCords.getCol();
                    for (Coordinates currCord : currCarCords) {
                        int row = currCord.getRow();
                        int col = currCord.getCol() - distanceToMove;
                        toPassIn.add(new Coordinates(row, col));
                    }
                    this.jamConfig = new JamConfiguration(this.jamConfig, curr, toPassIn);
                    toPassIn.clear();
                } else if (secondCords.getCol() > lastingCurrCords.getCol()) {
                    int distanceToMove = secondCords.getCol() - lastingCurrCords.getCol();
                    for (Coordinates currCord : currCarCords) {
                        int row = currCord.getRow();
                        int col = currCord.getCol() + distanceToMove;
                        toPassIn.add(new Coordinates(row, col));
                    }
                    this.jamConfig = new JamConfiguration(this.jamConfig, curr, toPassIn);
                    toPassIn.clear();
                }
                this.notifyObservers("Moved from " + cords + " to " + secondCords);

            } else if (!curr.movesSideways()) {
                for (int row = curr.getStartingRow(); row <= secondCords.getRow(); row++) {
                    if (this.jamConfig.gameBoard[row][curr.getStartingCol()] != EMPTY
                            && this.jamConfig.gameBoard[row][curr.getStartingCol()] != curr.getCarName().charAt(0)) {
                        this.notifyObservers("Can't move from " + cords + " to " + secondCords);
                        cords = null;
                        return;
                    }
                }

                for(int row = curr.getStartingRow(); row >= secondCords.getRow(); row--){
                    if (this.jamConfig.gameBoard[row][curr.getStartingCol()] != EMPTY
                            && this.jamConfig.gameBoard[row][curr.getStartingCol()] != curr.getCarName().charAt(0)) {
                        this.notifyObservers("Can't move from " + cords + " to " + secondCords);
                        cords = null;
                        return;
                    }
                }


                if (cords.getCol() != secondCords.getCol()) {
                    this.notifyObservers("Can't move from " + cords + " to " + secondCords);
                    cords = null;
                    return;
                }



                ArrayList<Coordinates> currCarCords = this.jamConfig.getAllCarCoordinates(curr);
                Coordinates firstCurrCords = currCarCords.get(0);
                Coordinates lastingCurrCords = currCarCords.get(currCarCords.size() - 1);
                ArrayList<Coordinates> toPassIn = new ArrayList<>();

                if (secondCords.getRow() < firstCurrCords.getRow()) {
                    int distanceToMove = firstCurrCords.getRow() - secondCords.getRow();
                    for (Coordinates currCord : currCarCords) {
                        int row = currCord.getRow() - distanceToMove;
                        int col = currCord.getCol();
                        toPassIn.add(new Coordinates(row, col));
                    }
                    this.jamConfig = new JamConfiguration(this.jamConfig, curr, toPassIn);
                    toPassIn.clear();
                } else if (secondCords.getRow() > lastingCurrCords.getRow()) {
                    int distanceToMove = secondCords.getRow() - lastingCurrCords.getRow();
                    for (Coordinates currCord : currCarCords) {
                        int row = currCord.getRow() + distanceToMove;
                        int col = currCord.getCol();
                        toPassIn.add(new Coordinates(row, col));
                    }
                    this.jamConfig = new JamConfiguration(this.jamConfig, curr, toPassIn);
                    toPassIn.clear();
                }
                this.notifyObservers("Moved from " + cords + " to " + secondCords);
            }
            cords = null;
        }
    }

    /**
     * @return the number of rows in the jam config
     */
    public int getThisConfigRows() {
        return this.jamConfig.getNumRows();
    }

    /**
     * @return the number of columns in the jam config
     */
    public int getThisConfigCols() {
        return this.jamConfig.getNumCols();
    }

    /**
     * @return the instance of the jamcConfig
     */
    public JamConfiguration getConfig() {
        return this.jamConfig;
    }

    /**
     * @return the game state of the game
     */
    public GameState getGameState() {
        return this.gameState;
    }

}
