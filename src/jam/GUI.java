package jam;

import commonJam.Observer;
import commonJam.solver.Coordinates;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Creates the class for the GUI version of the Jam game.
 *
 * @author Andrew Photinakis
 */
public class GUI extends Application implements Observer<JamModel, String> {

    /**
     * The resources' directory is located directly underneath the gui package
     */
    private final static String RESOURCES_DIR = "resources/";
    /**
     * holds the jamModel
     */
    private JamModel jamModel;
    /**
     * is the game initialized and ready?
     */
    private boolean initialized;
    /**
     * holds all the content of the gui to be set to the scene
     */
    private BorderPane borderPane;
    /**
     * holds the stage which holds the scene
     */
    private Stage stage;
    /**
     * the tool used to hold the game board
     */
    private GridPane gameBoardGridPane;
    /**
     * hexadecimal for the x car
     */
    public final static String X_CAR_COLOR = "#DF0101";
    /**
     * button font size for the game board
     */
    public final static int BUTTON_FONT_SIZE = 20;
    /**
     * the size of the buttons
     */
    public final static int ICON_SIZE = 75;
    /**
     * holds the told label which will be used to display messages
     */
    private Label topLabel;
    /**
     * hashmap that holds the button name and its color
     */
    private HashMap<String, String> buttonColorHash;
    /**
     * holds the game file
     */
    private static String gameFile;

    /**
     * Method that creates the model and does the start-up stuff
     *
     * @throws IOException thrown if the file is not valid
     */
    @Override
    public void init() throws IOException {
        this.initialized = false;
        this.jamModel = new JamModel(gameFile);
        this.jamModel.addObserver(this);
    }

    /**
     * Start method that creates the frontend of the GUI.
     * This means it creates the scene, stage, etc.
     * Most importantly it creates the labels,
     * buttons, and game board.
     *
     * @param stage holds the scene content
     * @throws Exception thrown if the file is not valid
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.initialized = true;
        this.borderPane = new BorderPane();
        this.stage = stage;
        this.borderPane.setAccessibleText("Jam GUI");

        ArrayList<Button> buttonArrayList = new ArrayList<>();
        Button loadButton = new Button("Load");
        Button resetButton = new Button("Reset");
        Button hintButton = new Button("Hint");

        buttonArrayList.add(hintButton);
        buttonArrayList.add(loadButton);
        buttonArrayList.add(resetButton);
        setButtonStyle(buttonArrayList);

        HBox bottomHbox = new HBox(loadButton, resetButton, hintButton);
        bottomHbox.setAlignment(Pos.CENTER);
        bottomHbox.setPadding(new Insets(5, 5, 5, 5));
        bottomHbox.setSpacing(5);
        this.borderPane.setBottom(bottomHbox);

        this.topLabel = new Label();
        this.topLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 15));
        this.topLabel.setStyle("-fx-text-alignment: CENTER ;");
        this.borderPane.setTop(this.topLabel);

        this.jamModel.load(gameFile);
        this.gameBoardGridPane = gameStateLoad();
        this.borderPane.setCenter(this.gameBoardGridPane);

        loadButton.setOnAction(actionEvent -> {
            try {
                loadButtonAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        resetButton.setOnAction(actionEvent -> {
            try {
                resetButtonAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        hintButton.setOnAction(actionEvent -> {
            try {
                hintButtonAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Scene scene = new Scene(this.borderPane);
        this.stage.sizeToScene();
        this.stage.setTitle("Jam GUI");
        this.stage.setScene(scene);
        this.stage.show();
    }


    /**
     * Method is used by whatever calls it needs and the
     * model needs to changes the UI displayed.
     *
     * @param jamModel model for the GUI
     * @param msg      the message that is made when notifyObservers is called
     */
    @Override
    public void update(JamModel jamModel, String msg) {
        if (!this.initialized) {
            return;
        }

        this.topLabel.setText(msg);
        if (jamModel.getGameState() == JamModel.GameState.LOADING) {
            this.gameBoardGridPane = gameStateLoad();
        } else {
            this.gameBoardGridPane = passOver();
        }
        this.borderPane.setCenter(this.gameBoardGridPane);
        this.stage.sizeToScene();
        jamModel.setGameStateONGOING();
    }

    /**
     * Passes the board when reset it called. Makes sure nothing is changed
     * that shouldn't be changed.
     *
     * @return call to the completeGridWithX() method which just returns
     * a finished gridPane game board with X car in it.
     */
    public GridPane passOver() {
        GridPane grid = new GridPane();
        this.jamModel.getConfig().stringCarTreeMap.keySet().forEach(c -> {
            Car car = this.jamModel.getConfig().stringCarTreeMap.get(c);
            boardHelpMaker(grid, car, this.buttonColorHash.get(car.getCarName()));
        });
        return completeGridWithX(grid);
    }

    /**
     * Passes in a GridPane and puts the X car on the grid
     * along with its properties
     *
     * @param grid the unfinished grid
     * @return finish game board
     */
    private GridPane completeGridWithX(GridPane grid) {
        for (int i = 0; i < this.jamModel.getConfig().getNumRows(); i++) {
            for (int j = 0; j < this.jamModel.getConfig().getNumRows(); j++) {
                if (this.jamModel.getConfig().gameBoard[i][j] == JamConfiguration.EMPTY) {
                    Button xButton = new Button(Character.toString(JamConfiguration.EMPTY));
                    xButton.setStyle(
                            "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-font-weight: bold;" +
                                    "-fx-text-alignment: CENTER;" +
                                    "-fx-border-color: black;");
                    xButton.setMinSize(ICON_SIZE, ICON_SIZE);
                    xButton.setMaxSize(ICON_SIZE, ICON_SIZE);
                    grid.add(xButton, j, i);
                    int finalI1 = i;
                    int finalJ1 = j;
                    xButton.setOnAction(e -> {
                        try {
                            this.jamModel.selection(finalI1, finalJ1);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        }
        grid.setAlignment(Pos.CENTER);
        return grid;
    }

    /**
     * Called when loading a new game file. Reason being because it
     * chooses all new colors for the cars, in which we don't
     * need to happen when a car is selected per se.
     *
     * @return GridPane that represents the finished game board
     */
    public GridPane gameStateLoad() {
        GridPane grid = new GridPane();
        this.buttonColorHash = new HashMap<>();
        for (String c : this.jamModel.getConfig().stringCarTreeMap.keySet()) {
            Car car = this.jamModel.getConfig().stringCarTreeMap.get(c);
            if (car.getCarName().equals("X")) {
                this.buttonColorHash.put(car.getCarName(), X_CAR_COLOR);
                boardHelpMaker(grid, car, X_CAR_COLOR);
            } else {
                Color rand = getNewCarColor();
                String hex = "#" + Integer.toHexString(rand.getRGB()).substring(2);
                this.buttonColorHash.put(car.getCarName(), hex);
                boardHelpMaker(grid, car, hex);
            }
        }
        return completeGridWithX(grid);
    }

    /**
     * Takes in an unfinished board, car, and color.
     * Creates all the buttons for the car and places them
     * on the board
     *
     * @param grid  represents the game board
     * @param car   car to be taken care of
     * @param color the color the car will have
     */
    private void boardHelpMaker(GridPane grid, Car car, String color) {
        for (Coordinates carCord : this.jamModel.getConfig().getAllCarCoordinates(car)) {
            Button button = new Button(car.getCarName());
            button.setStyle(
                    "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                            "-fx-background-color: " + color + ";" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-alignment: CENTER;" +
                            "-fx-border-color: black;");
            button.setMinSize(ICON_SIZE, ICON_SIZE);
            button.setMaxSize(ICON_SIZE, ICON_SIZE);
            grid.add(button, carCord.getCol(), carCord.getRow());
            button.setOnAction(e -> {
                try {
                    this.jamModel.selection(carCord.getRow(), carCord.getCol());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    /**
     * Handles the action when the load button is pressed
     * Opens the dialog via the FileChooser and
     * gets that file path and loads it.
     *
     * @throws IOException thrown if there is a IO problem
     */
    private void loadButtonAction() throws IOException {
        String newFileName = "";
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(this.stage);
        newFileName += file.getPath();
        this.jamModel.load(newFileName);
    }

    /**
     * Handles the action when the hint button is pressed
     * Calls to the models hint function.
     *
     * @throws IOException thrown if there is a IO problem
     */
    private void hintButtonAction() throws IOException {
        this.jamModel.hint();
    }

    /**
     * Handles the action when the reset button is pressed
     * Calls to the models reset function.
     *
     * @throws IOException thrown if there is a IO problem
     */
    private void resetButtonAction() throws IOException {
        this.jamModel.reset();
    }

    /**
     * @return random color by RGB and random floats so that no car color is the same
     */
    public static Color getNewCarColor() {
        Random rand = new Random();
        return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    }

    /**
     * Sets the style of all the buttons in the arraylist
     *
     * @param currButtonArrayList arraylist of buttons
     */
    private void setButtonStyle(ArrayList<Button> currButtonArrayList) {
        for (Button currButton : currButtonArrayList) {
            currButton.setStyle("-fx-font: 15px Menlo; -fx-arc-width: 50px;");
        }
    }

    /**
     * Main method that takes in a file name for the args.
     * if the file is not there, the program terminates
     * and the Exception method is printed
     *
     * @param args filename passed in
     */
    public static void main(String[] args) {
        try {
            gameFile = args[0];
            Application.launch(args);
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
            System.exit(0);
        }
    }

}
