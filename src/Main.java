import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Main class which launches the JavaFX application, along with building GUI elements in the game
 * @author Ben Rees (2015742), Shushovit Khanal (2023769), Fahim Samady (2035827)
 */
public class Main extends Application {
    //Constants used in the class
    private static final int ROOT_PREF_WIDTH = 1200;
    private static final int ROOT_PREF_HEIGHT = 700;
    private static final String STAGE_TITLE = "Group 15 - CW2";
    private static final int SCORE_TIME_FONT_SIZE = 20;
    private static final int SCORE_TIME_GAP_SIZE = 425;
    private static final int MESSAGE_BOX_WIDTH = 300;
    private static final int MESSAGE_BOX_HEIGHT = 600;
    private static final int MESSAGE_FONT_SIZE = 18;
    private static final int MESSAGE_BOX_PADDING = 25;

    // Top level borderpane which stores all the elements of the game, including the GUI
    private static BorderPane root;

    //instances of levels (it was intended to have multiple levels, but we could not manage to have more than one)
    private static final int levelNumber = 1;
    private static final Level level = new Level(levelNumber);

    /*
    * The size of the canvas is determined by the size of level, so that we do not need to resize every element inside
    * the game and everything fits well.
    */
    private static final int CANVAS_WIDTH = Level.getTileSize() * Level.getWidth();
    private static final int CANVAS_HEIGHT = Level.getTileSize() * Level.getHeight();

    //win and lose condition booleans which end game
    private static boolean win = false;
    private static boolean lose = false;

    // The canvas in the GUI
    private static Canvas canvas;

    /*
     * Timeline we use to keep track of time left in level, as well as to trigger movement of all characters in game
     * within a set duration of time.
     */
    private Timeline tickTimeline;

    //Main method to run the JavaFX application
    public static void main(String[] args) throws IOException, InterruptedException {
        launch(args);
    }

    /**
     * Setup the new application.
     * @param primaryStage The stage that is to be used for the application.
     */
    public void start(Stage primaryStage) throws URISyntaxException {

        //Title of the window
        primaryStage.setTitle(STAGE_TITLE);


        // Build the GUI
        root = buildGUI();

        // Create a scene from the GUI
        Scene scene = new Scene(root);

        //Event filter to call method processKeyEvent that triggers movement of player when certain keys are pressed
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> processKeyEvent(event));

        // This timeline is responsible for changing the timer and decrementing it every second:
        tickTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateGUI()));
        tickTimeline.setCycleCount(Animation.INDEFINITE);
        tickTimeline.play(); // Stop this when game ends or level is changed

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * This method changes the display of the timer, player score and player level every second.
     * Only changes these things when they need to be changed
     */
    private void updateGUI() {

        // Text to display the score
        Text scoreText = new Text("Score: " + Player.getScore());
        // Set font and weight of score text:
        scoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, SCORE_TIME_FONT_SIZE));

        // Get level number based on level user is on
        Text levelText = new Text("Level: 1");
        // Set font and weight of level text:
        levelText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, SCORE_TIME_FONT_SIZE));

        // The time left text will change every second
        Text timeLeftText = new Text("");
        // decrement the time by 1 every time this method is called
        level.decrementTime();

        // If the player has not lost:
        if (!lose) {
            // If the player has not won:
            if (!win) {
                // Display the current time left:
                timeLeftText = new Text("Time Left: " + level.getTimeToCompleteLevel());
                // If the player has won:
            } else {
                tickTimeline.stop(); // Stop the timeline that calls this method
                timeLeftText = new Text("You've won!"); // Replace timer with message telling player they've won
            }
            // If the player has lost:
        } else {
            timeLeftText = new Text("Player Loses!"); // Replace time left text with message saying lost
            tickTimeline.stop(); // Stop the timeline that calls this method
            // lose = true;
        }
        timeLeftText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, SCORE_TIME_FONT_SIZE));

        // Create a new FlowPane to store all of these elements:
        FlowPane topText = new FlowPane();
        // Gap for nice spacing
        topText.setHgap(SCORE_TIME_GAP_SIZE);
        // add all text to FlowPane
        topText.getChildren().addAll(scoreText, levelText, timeLeftText);
        // Put the flowPane on the bottom of the root Pane
        root.setBottom(topText);
    }

    /**
     * Process a key event due to a key being pressed, e.g., to move the player.
     * @param event The key event that was pressed.
     */
    private void processKeyEvent(KeyEvent event) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Player.playerMove(event, gc);
        drawGame();
        event.consume();
    }

    /**
     * Draw the game on the canvas.
     */
    public static void drawGame() {
        Level.drawLevel(canvas.getGraphicsContext2D());
    }

    /**
     * Sets player losing condition 'lose' as true
     */
    public static void setPlayerLost() {
        lose = true;
    }

    /**
     * Checks if player has lost or not
     * @return lose condition boolean 'lose'
     */
    public static boolean hasPlayerLost() {
        return lose;
    }

    /**
     * Sets player win condition 'win' as true
     */
    public static void setPlayerWin() {
        win = true;
    }

    /**
     * Checks if player has won or not
     * @return player winning condition boolean 'win'
     */
    public static boolean hasPlayerWon() {
        return win;
    }

    /**
     * Creates the GUI.
     * @return The panel that contains the created GUI.
     */
    private BorderPane buildGUI() {
        // Create top-level panel that will hold all GUI nodes.
        BorderPane root = new BorderPane();
        root.setPrefSize(ROOT_PREF_WIDTH, ROOT_PREF_HEIGHT);
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        root.setCenter(canvas);
        drawGame();

        MenuBar mainMenu = new MenuBar();
        Menu File = new Menu("File");
        Menu Edit = new Menu("Edit");
        mainMenu.getMenus().add(File);
        mainMenu.getMenus().add(Edit);
        root.setTop(mainMenu);

        //Label to display message of the day
        Label messageBox = new Label();
        messageBox.setPrefSize(MESSAGE_BOX_WIDTH,MESSAGE_BOX_HEIGHT);
        MessageOfTheDay textToDisplay;
        try{
            textToDisplay = new MessageOfTheDay();
            messageBox.setText(textToDisplay.getMessageOfTheDay());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        };
        messageBox.setWrapText(true);
        messageBox.setFont(Font.font(MESSAGE_FONT_SIZE));
        messageBox.setPadding(new Insets(MESSAGE_BOX_PADDING));
        root.setRight(messageBox);
        
        return root;
    }
}