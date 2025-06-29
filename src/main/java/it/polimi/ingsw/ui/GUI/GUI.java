package it.polimi.ingsw.ui.GUI;

import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.response.ExitGame;
import it.polimi.ingsw.core.model.message.response.checkConnection;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.network.ClientAbstract;
import it.polimi.ingsw.ui.GUI.boardstate.TurnStateEnum;
import it.polimi.ingsw.ui.GUI.controller.*;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.ui.UserInterfaceStrategy;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class GUI extends Application implements UserInterfaceStrategy {

    private static Stage currStage;
    private Parent root;
    private double xStage;
    private double yStage;
   protected static ClientAbstract client ; //il client che possiede la view

    //il viewmodel del gioco che verrà modificato dopo gli update del server
    //NB il view model anche del client della view viene modificato solo dopo l'update del server non mentre mando al server la mossa
    //questo perchè se il server ritiene la mossa non valida non la registra e non manda un update
    protected static ModelView viewModel;

    //lista dei controller delle varie scene
    private static StartingSceneController startingSceneController;
    private static WaitingForPlayersController waitingPlayersController;
    private static WaitingForPlayersController waitingForPlayersController;
    private static BoardViewController boardViewController;
    private static ErrorPopUpController errorPopUpController;
    private static ScoreboardController scoreboardController;
    private static TurnStateEnum turnState; //lo stato del turno in cui si trova ora il client
    private static ChatController chatController;
    private static ChoosingObjectiveController choosingObjectiveController;
    private static ChoosingStarterController choosingStarterController;

    private static CardGame starterCard;
    private static Boolean test;
    private static List<Objective> secretObjs;


    private static Parent  waitingForPlayersScene, boardScene, choosingObjectiveScene, choosingStarterScene;
    private static Parent startingScene;

    private static Boolean notYetBoardScene = true;
    protected static Boolean chatOpen = false;
    protected static Boolean messageJustSent = false;
    protected static Boolean wasWaitingForPlayers = false;

    protected static Timer timer;

    private static PlayableCardIds firstTurn;
    private static Boolean firstTurnBool = false;

    protected static ArrayList<Timer> timers = new ArrayList<>();


    private static volatile boolean javaFxLaunched = false;

    /**
     * Launches a JavaFX application in a separate thread if it's the first time the method is called.
     * For subsequent calls, it initializes and starts a new instance of the provided application class on the JavaFX Application Thread.
     *
     * It's a way to avoid the fact that an Application subclass can be launched only once, with this method you can play how mani games you want choosing every time between cli or gui
     * @param applicationClass the class of the JavaFX Application to be launched
     */
    public static void myLaunch(Class<? extends Application> applicationClass) {
        if (!javaFxLaunched) { // First time
            Platform.setImplicitExit(false);
            new Thread(()->Application.launch(applicationClass)).start();
            javaFxLaunched = true;
        } else { // Next times
            Platform.runLater(()->{
                try {
                    Application application = applicationClass.newInstance();
                    Stage primaryStage = new Stage();
                    application.start(primaryStage);
                    notYetBoardScene = true;
                    chatOpen = false;
                    messageJustSent = false;
                    timers = new ArrayList<>();
                    //System.out.println("JavaFX application thread started");
                } catch (Exception e) {
                    //System.out.println("Error starting JavaFX application thread");
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Initializes and starts the primary stage for the JavaFX application.
     *
     * <p>This method sets up the initial scene by loading the "StartingScene.fxml" file, setting the stage title,
     * icon, and scene, and then displaying the stage. It also sets up a close request handler for the stage.</p>
     *
     * @param primaryStage the primary stage for this JavaFX application
     * @throws Exception if there is any issue loading the FXML file or setting up the stage
     */
    @Override
    public void start (Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/StartingScene.fxml"));
            root = loader.load();
            startingSceneController = loader.getController();
            startingSceneController.ifGameNotStarted();
        } catch (Exception e) {
          //  System.out.println("Error loading StartingScene.fxml");
            e.printStackTrace();
        }
        currStage = primaryStage;
        // Parent root = FXMLLoader.load(getClass().getResource("scenes/StartingScene.fxml"));
        currStage.setTitle("StartingGame");
        Image icon = new Image("icons/iconaCodex.png");
        currStage.getIcons().add(icon);
        currStage.setScene(new Scene(root));
        xStage = currStage.getX();
        yStage = currStage.getY();
        initializeScenes();
        primaryStage.setOnCloseRequest(event -> {
            // Handle the close request
            //System.out.println("Window is closing");
            this.closeError();
        });
        //primaryStage.getScene().getStylesheets().add("cssFiles/style.css");
        currStage.show();
    }
    /*
     Main GUI class with JavaFX scenes and controllers.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }


    public void displayScoreboard() {
        //gui does nothing
    }


    public void displayPersonalResources(Map<Resource, Integer> data) {
        //gui does nothing
    }

    public void noFreeAngles() {
        //gui does nothing
    }

    public void getBoardString(String board) {
        //gui does nothing
    }

    /**
     * Initializes all the scenes and their respective controllers.
     *
     * <p>This method loads several FXML files for different scenes and sets their controllers.
     * This allows the application to access and use methods from these controllers even if the user has not yet navigated to these scenes.</p>
     *
     * <p>The scenes initialized are:</p>
     * <ul>
     *   <li>Starting Scene</li>
     *   <li>Board Scene</li>
     *   <li>Waiting for Players Scene</li>
     *   <li>Choosing Starter Scene</li>
     *   <li>Choosing Objective Scene</li>
     * </ul>
     *
     * <p>If any FXML file fails to load, the exception is caught and the stack trace is printed.</p>
     */
    public void initializeScenes() {
        //inizializzo tutte le scene e i relativi controller così da poter usare tutti i metodi sui controller anche se l'utente non ha ancora cambiato scena
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/StartingScene.fxml"));
            this.setStartingScene(loader.load());
            this.setStartingSceneController( loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/BoardScene.fxml"));
            this.setBoardScene(loader.load());
            this.setBoardViewController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/WaitingForPlayers.fxml"));
            this.setWaitingForPlayersScene(loader.load());
            this.setWaitingForPlayersController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/ChoosingStarter.fxml"));
            this.setChoosingStarterScene(loader.load());
            this.setChoosingStarterController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/ChoosingObjective.fxml"));
            this.setChoosingObjectiveScene(loader.load());
            this.setChoosingObjectiveController(loader.getController());
          //  System.out.println("ChoosingObjective.fxml loaded");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * Changes the current scene displayed in the provided stage to the specified scene.
     *
     * <p>This method switches the current scene to the one specified by the `sceneName` parameter.
     * It initializes the new scene and its controller as needed. The method supports several predefined scenes:</p>
     * <ul>
     *   <li>Starting Scene</li>
     *   <li>Waiting for Players Scene</li>
     *   <li>Board Scene</li>
     *   <li>Choosing Starter Scene</li>
     *   <li>Choosing Objective Scene</li>
     * </ul>
     *
     * @param sceneName the name of the FXML file for the scene to be displayed
     * @param stage the stage in which the scene will be displayed
     */
    public void changeScene(String sceneName, Stage stage){
        //gui ha quindi in curr stage lo stage dell'ultima scena che ha attivato
        currStage = stage;
       // initializeScenes();
        //per ora chiamo qui initializeScenes ma poi sarà nel metodo inizialize della cui generale interfaccia
        switch (sceneName){
            case "scenes/StartingScene.fxml":
                try {
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                    // root = loader.load();
                    //startingSceneController = loader.getController();
                    root = this.getStartingScene();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "scenes/WaitingForPlayers.fxml":
                try {
                   FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(sceneName));
                   root = loader.load();
                   waitingForPlayersController = loader.getController();
                    //root = this.getWaitingForPlayersScene();
                   // this.getWaitingForPlayersController().initializeMessage();
                    waitingForPlayersController.initializeMessage();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case "scenes/BoardScene.fxml":

                 try {
                     //FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                     //root = loader.load();
                     //  boardViewController = loader.getController();
                     root = this.getBoardScene();
                     notYetBoardScene = false;
                     this.getBoardViewController().setUpBoard();
                     currStage.setScene(new Scene(root));
                     currStage.show();
                    } catch (Exception e) {
                     e.printStackTrace();
                    }
                break;

            case "scenes/ChoosingStarter.fxml":
                try {
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                    //root = loader.load();
                    //choosingStarterController = loader.getController();
                    root = this.getChoosingStarterScene();
                   // System.out.printf("Starter card loaded: %d", this.getStarterCard().getId());
                   this.getChoosingStarterController().setStarterCard(this.getStarterCard());
                   this.getChoosingStarterController().chooseStarterSide();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "scenes/ChoosingObjective.fxml":
                try {
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                    //root = loader.load();
                    //choosingObjectiveController = loader.getController();
                    root = this.getChoosingObjectiveScene();
                    //da fare qui perchè prima non ho il controller
                    this.getChoosingObjectiveController().setObjective(secretObjs);
                    this.getChoosingObjectiveController().chooseObjective();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }




    }



    /**
     * Displays an error pop-up window with the specified message.
     *
     * <p>This method creates a new pop-up window to show an error message. The pop-up is centered near the
     * specified error stage's current position. The pop-up is loaded from the "ErrorPopUp.fxml" file and
     * uses a scale transition for its appearance animation.</p>
     *
     * @param message the error message to be displayed in the pop-up
     * @param errorStage the stage near which the error pop-up will be displayed
     */
    public void showErrorPopUp(String message, Stage errorStage) {
        double x = errorStage.getX();
        double y = errorStage.getY();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/ErrorPopUp.fxml"));
            Parent root = loader.load();
            errorPopUpController = loader.getController();
            errorPopUpController.showMessage(message);
            ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(50), root);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);

            Stage stage1 = new Stage();
            stage1.setTitle("Error");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setResizable(false);
            stage1.setScene(scene);
            stage1.show();
            stage1.setX(x + 150);
            stage1.setY(y + 150);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Displays a scoreboard pop-up window near the specified stage.
     *
     * <p>This method creates a new pop-up window to display the scoreboard. The pop-up is centered near the
     * specified stage's current position. The pop-up is loaded from the "Scoreboard.fxml" file and
     * uses a scale transition for its appearance animation.</p>
     *
     * <p>The scoreboard pop-up is shown as a modal, non-resizable, and transparent window.</p>
     *
     * @param errorStage the stage near which the scoreboard pop-up will be displayed
     */
    public void showScoreboardPopUp(Stage errorStage){
        double x = errorStage.getX();
        double y = errorStage.getY();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/Scoreboard.fxml"));
            Parent root = loader.load();
            scoreboardController = loader.getController();
            scoreboardController.setPawns();
            ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(50), root);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);

            Stage stage1 = new Stage();
            stage1.setTitle("Scoreboard");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setResizable(false);
            stage1.setScene(scene);
            stage1.show();
            stage1.setX(x + 150);
            stage1.setY(y + 150);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Displays an error pop-up window with the specified message.
     *
     * <p>This method creates a new pop-up window to show an error message. The pop-up is loaded from the
     * "ErrorPopUp.fxml" file and uses a scale transition for its appearance animation. The position of the
     * pop-up is determined by the coordinates stored in `xStage` and `yStage`.</p>
     *
     * <p>The error pop-up is shown as a modal, non-resizable, and transparent window that is always on top.</p>
     *
     * @param message the error message to be displayed in the pop-up
     */
    public void showErrorPopUpNoStage(String message) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("scenes/ErrorPopUp.fxml"));
            Parent root = loader.load();
            errorPopUpController = loader.getController();
            errorPopUpController.showMessage(message);
            ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(50), root);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);

            Stage stage1 = new Stage();
            stage1.setTitle("Error");
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage1.initModality(Modality.APPLICATION_MODAL);
            stage1.initStyle(StageStyle.TRANSPARENT);
            stage1.setResizable(false);
            stage1.setScene(scene);
            stage1.isAlwaysOnTop();
            stage1.show();
            stage1.setX( xStage + 150);
            stage1.setY(yStage + 150);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //metodi chiamati dai message.execute, comuni con tui grazie a uiStrategy

    /**
     * Handles the transition to the "Waiting for Players" scene if the game was waiting for players.
     *
     * <p>This method checks if the game was in a state of waiting for players (indicated by the `wasWaitingForPlayers` flag).
     * If so, it changes the current scene to the "Waiting for Players" scene on the JavaFX Application Thread.</p>
     */
    public void gameStarted() {
        if(wasWaitingForPlayers) {
            Platform.runLater(() -> {
                changeScene("scenes/WaitingForPlayers.fxml", currStage);
            });
        }
    }

    /**
     * Initiates the selection of objectives on the JavaFX Application Thread.
     *
     * <p>This method receives a list of objectives and prepares to handle the selection process on the JavaFX Application Thread.
     * It sets the received objectives as the secret objectives and prepares to invoke the {@code chooseObjective()} method on the
     * appropriate controller once it's loaded.</p>
     *
     * @param obj the list of objectives to be selected
     */
    @Override
    public void chooseObjective(List<Objective> obj) {
        Platform.runLater(() -> {
            secretObjs = obj;
        });
    }


    @Override
    public void setStarterSide() {
        //gui does nothing
    }

    /**
     * Visualizes the starter card loaded asynchronously on the JavaFX Application Thread.
     *
     * <p>This method sets the starter card using the provided {@code card} parameter. It ensures that this
     * operation is performed on the JavaFX Application Thread using {@code Platform.runLater()}.</p>
     *
     * @param card the starter card to be visualized
     */
    @Override
    public void visualiseStarterCardLoaded(Card card) {
        Platform.runLater(() -> {
            this.setStarterCard( card);
        });

    }

    /**
     * Updates the decks of cards asynchronously on the JavaFX Application Thread.
     *
     * <p>This method updates the decks of cards with the provided {@code updatedDecks} parameter. It checks if
     * the board scene has been loaded before attempting to update the decks, ensuring that the update operation
     * is performed only when the board scene is ready.</p>
     *
     * <p>If the board scene is ready (indicated by {@code notYetBoardScene}), it calls {@code updateDecks()} on
     * the board view controller to apply the updates.</p>
     *
     * @param updatedDecks the updated list of decks to be applied
     */
    @Override
    public void updateDecks(List<Card> updatedDecks) {
        Platform.runLater(() -> {
            if(!notYetBoardScene) {
                this.getBoardViewController().updateDecks(updatedDecks);
            }
        });
    }


    /**
     * Updates the UI to indicate the current player's turn asynchronously on the JavaFX Application Thread.
     *
     * <p>This method sets the current player's turn status to true in the client's model view. If the board scene
     * has not been loaded yet (indicated by {@code notYetBoardScene}), it initiates a scene change to the "BoardScene.fxml".
     * After ensuring the board scene is ready, it delegates the task of selecting a card to play to the board view controller.</p>
     *
     * @param data the playable card IDs for the current turn
     */
    @Override
    public void currentTurn(PlayableCardIds data) {
        Platform.runLater(() -> {
            client.getModelView().setMyTurn(true);
            if(notYetBoardScene){
                changeScene("scenes/BoardScene.fxml", currStage);
            }
            this.getBoardViewController().selectCardToPlay(data);
        });
    }

    /**
     * Updates the state of the current player asynchronously on the JavaFX Application Thread.
     *
     * <p>This method updates the state of the current player by invoking {@code updateMyPlayerstate()} on the
     * board view controller. This operation is performed asynchronously on the JavaFX Application Thread to ensure
     * UI-related tasks are handled properly.</p>
     */
    @Override
    public void updateMyPlayerState() {
        Platform.runLater(() -> {
            this.getBoardViewController().updateMyPlayerstate();
        });
    }

    /**
     * Updates the state of a specific player asynchronously on the JavaFX Application Thread.
     *
     * <p>This method updates the state of the player identified by {@code player} by invoking {@code updatePlayerstate(player)}
     * on the board view controller. This operation is performed asynchronously on the JavaFX Application Thread to ensure
     * UI-related tasks are handled properly.</p>
     *
     * @param player the name or identifier of the player whose state is to be updated
     */
    @Override
    public void updatePlayerState(String player) {
        Platform.runLater(() -> {
            this.getBoardViewController().updatePlayerstate(player);
        });
    }

    /**
     * Displays available angles for a player's action asynchronously on the JavaFX Application Thread.
     *
     * <p>This method displays the available angles for a player's action by invoking {@code askForAngle(data)}
     * on the board view controller. This operation is performed asynchronously on the JavaFX Application Thread
     * to ensure UI-related tasks are handled properly.</p>
     *
     * @param data a list of coordinates representing the available angles
     */
    @Override
    public void showAvailableAngles(List<Coordinate> data) {
        Platform.runLater(() -> {
            this.getBoardViewController().askForAngle(data);
        });
    }

    /**
     * Asks the player where to draw cards from asynchronously on the JavaFX Application Thread.
     *
     * <p>This method prompts the player to choose where to draw cards from by invoking {@code drawFromDecks()}
     * on the board view controller. This operation is performed asynchronously on the JavaFX Application Thread
     * to ensure UI-related tasks are handled properly.</p>
     *
     * @param cards a list of cards indicating the available decks or sources from which cards can be drawn
     */
    @Override
    public void askWhereToDraw(List<Card> cards) {
        Platform.runLater(() -> {
            this.getBoardViewController().drawFromDecks();
        });
    }

    /**
     * Displays the player's hand of cards asynchronously on the JavaFX Application Thread.
     *
     * <p>This method displays the player's hand of cards by invoking {@code updateHand(hand)} on the board view controller,
     * provided that the board scene has already been loaded (indicated by {@code notYetBoardScene} being false). This operation
     * is performed asynchronously on the JavaFX Application Thread to ensure UI-related tasks are handled properly.</p>
     *
     * @param hand a list of cards representing the player's current hand
     */
    @Override
    public void displayHand(List<Card> hand) {
        Platform.runLater(() -> {
            if(!notYetBoardScene){
               // System.out.println("update hand arrived to view");
                this.getBoardViewController().updateHand(hand);
            }
        });
    }

    /**
     * Displays a message indicating that it's not the player's turn asynchronously on the JavaFX Application Thread.
     *
     * <p>This method checks if the board scene has not been loaded yet (indicated by {@code notYetBoardScene}),
     * and if so, it initiates a scene change to "BoardScene.fxml". If {@code messageJustSent} is false, it displays
     * a message indicating that it's not the player's turn using {@code message("It's not your turn")} on the board
     * view controller. It also updates the client's model view to reflect that it's not the player's turn and manages
     * a timer task to periodically check the connection status by sending a message.</p>
     */
    @Override
    public void showNotYourTurn() {
        Platform.runLater(() -> {
            if(notYetBoardScene){
                changeScene("scenes/BoardScene.fxml", currStage);
            }
            if(!messageJustSent) {
                this.getBoardViewController().message("It's not your turn");
                client.getModelView().setMyTurn(false);
                if(timer!=null){
                    timers.add(timer);
                }
                timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run () {
                        try {
                            client.sendMessage(new checkConnection("checkConnection", null));
                        }catch(Exception e){
                            timer.cancel();
                        }
                    }
                };
                timer.schedule(task, 0, 5000);
            }
        });
    }

    /**
     * Processes a new chat message asynchronously on the JavaFX Application Thread.
     *
     * <p>This method updates the chat interface with a new message by invoking {@code updateChat(message)}
     * on the board view controller. This operation is performed asynchronously on the JavaFX Application Thread
     * to ensure UI-related tasks are handled properly.</p>
     *
     * @param message the new chat message to be displayed
     */
    @Override
    public void newChatMessage(Message message){
        Platform.runLater(() -> {
            this.getBoardViewController().updateChat(message);
        });
    }

    /**
     * Notifies the player that it is their last turn asynchronously on the JavaFX Application Thread.
     *
     * <p>This method displays a message indicating that it is the player's last turn by invoking {@code message("This is your Last Turn \n Play carefully!")}
     * on the board view controller. This operation is performed asynchronously on the JavaFX Application Thread to ensure
     * UI-related tasks are handled properly.</p>
     */
    @Override
    public void lastTurn() {
        Platform.runLater(() -> {
            this.getBoardViewController().message("This is your Last Turn \n Play carefully!");
            //in teoria nessun dato, solo messaggio diverso
        });
    }

    /**
     * Processes the end of the game asynchronously on the JavaFX Application Thread.
     *
     * <p>This method handles the end of the game by invoking {@code gameEnded()} if the board scene has not been loaded yet
     * (indicated by {@code notYetBoardScene}). Otherwise, it updates the game interface with the final ranking by invoking
     * {@code endGameRanking(rank)} on the board view controller. This operation is performed asynchronously on the JavaFX
     * Application Thread to ensure UI-related tasks are handled properly.</p>
     *
     * @param rank a list of pairs containing player names and their corresponding scores in the final ranking
     */
    @Override
    public void endGame(List<Pair<String, Integer>> rank) {
        Platform.runLater(() -> {
            if(notYetBoardScene){
                this.gameEnded();
            }
            else{
                this.getBoardViewController().endGameRanking(rank);
            }
        });
    }


    /**
     * Closes any active error handling and disconnects the client from the game.
     *
     * <p>This method cancels any active timers and sends a message to the client to exit the game. It is typically called
     * to handle the closure of error pop-ups or to manage disconnections from the game.</p>
     */
    public void closeError() {
        if(timer!=null){
            timer.cancel();
            //System.out.println("Timer cancelled");
        }
        if(timers!=null){
            for(Timer t: timers){
                t.cancel();
            }
        }

        client.sendMessage(new ExitGame("exitGame", null));
        System.out.println("You have been disconnected from the game");
    }

    /**
     * Handles the end of the game asynchronously on the JavaFX Application Thread.
     *
     * <p>This method cancels any active timers and closes the current stage (window) associated with the game.
     * It is typically called when the game has ended to perform cleanup tasks and close the game window.</p>
     */
    public void gameEnded(){
        Platform.runLater(() -> {
            if(timer!=null){
                timer.cancel();
                //System.out.println("Timer cancelled");
            }
            if(timers!=null){
                for(Timer t: timers){
                    t.cancel();
                }
            }

            currStage.close();

        });
    }


    public ClientAbstract getClient(){
        return client;
    }

    public void setClient (ClientAbstract client){
        this.client = client;
    }


    @Override
    public void displayCard(Card card) {
        //gui does nothing
    }

    @Override
    public void displayCardBack(Card card) {

    }

    @Override
    public void displayStarterCardBack(ResourceCard card) {
//gui does nothing
    }

    @Override
    public String displayResourcesStarter(ResourceCard card, int index1, int index2) {
        return null;
    }

    @Override
    public void placeCard(Card card, Coordinate position) {
        //gui does nothing
    }

    @Override
    public void displayBoard() {
        //gui does nothing
    }

    @Override
    public CardSelection askCardSelection(PlayableCardIds ids, List<Card> cards) {
        //gui does nothing
        return null;
    }

    @Override
    public String displayAngle(List<Coordinate> angles) {
       //gui does nothing
        return null;
    }

    @Override
    public Coordinate placeBottomRight(Card targetCard, Card cardToPlace) {
        //gui does nothing
        return null;
    }

    @Override
    public Coordinate placeTopLeft(Card targetCard, Card cardToPlace) {
       //gui does nothing
        return null;
    }

    @Override
    public Coordinate placeTopRight(Card targetCard, Card cardToPlace) {
        //gui does nothing
        return null;
    }

    @Override
    public Coordinate placeBottomLeft(Card targetCard, Card cardToPlace) {
       //gui does nothing
        return null;
    }

    @Override
    public void visualizeStarterCard(Card card) {
        //gui does nothing
    }

    @Override
    public void displayCommonObjective(List<Objective> obj) {
        // gui does that automatically
    }



    @Override
    public void place(Card cardToPlace, Card targetCard, int position) {
        //gui does nothing
    }

    @Override
    public void displayChat(Chat chat, String username) {
        //gui does nothing
    }

    @Override
    public void selectFromMenu() {
        //gui does nothing
    }

    @Override
    public String askUsername() {
        //gui does nothing
        return null;
    }

    @Override
    public String askJoinCreate() {
        //gui does nothing
        return null;
    }

    @Override
    public String askGameId(String joinCreate, String gameIds) {
        //gui does nothing
        return null;
    }

    @Override
    public int askNumberOfPlayers() {
        //gui does nothing
        return 0;
    }

    @Override
    public void displayPawn(it.polimi.ingsw.core.model.enums.Color pawn) {
        //gui does nothing
    }


    private CardGame getStarterCard() {
        return this.starterCard;
    }

    public ChoosingStarterController getChoosingStarterController() {
        return choosingStarterController;
    }
    public void setChoosingStarterController(ChoosingStarterController choosingStarterController) {
        this.choosingStarterController = choosingStarterController;
    }

    public void setViewModel(ModelView viewModel) {
        this.viewModel = viewModel;
    }
    public void setTurnState(TurnStateEnum turnState) {
        this.turnState = turnState;
    }
    public TurnStateEnum getTurnState() {
        return turnState;
    }
    public void setStarterCard(CardGame starterCard) {
        this.starterCard = starterCard;
      //  System.out.printf("Starter card loaded: %d", this.starterCard.getId());
    }
    public void setCurrStage(Stage currStage) {
        this.currStage = currStage;
    }
    public void setRoot(Parent root) {
        this.root = root;
    }
    public void setxStage(double xStage) {
        this.xStage = xStage;
    }
    public void setyStage(double yStage) {
        this.yStage = yStage;
    }
    public void setStartingSceneController(StartingSceneController startingSceneController) {
        this.startingSceneController = startingSceneController;
    }
    public StartingSceneController getStartingSceneController() {
        return startingSceneController;
    }

    public void setBoardViewController(BoardViewController boardViewController) {
        this.boardViewController = boardViewController;
    }
    public BoardViewController getBoardViewController() {
        return boardViewController;
    }

    public void setErrorPopUpController(ErrorPopUpController errorPopUpController) {
        this.errorPopUpController = errorPopUpController;
    }
    public ErrorPopUpController getErrorPopUpController() {
        return errorPopUpController;
    }
    public void setScoreboardController(ScoreboardController scoreboardController) {
        this.scoreboardController = scoreboardController;
    }
    public ScoreboardController getScoreboardController() {
        return scoreboardController;
    }
    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }
    public ChatController getChatController() {
        return chatController;
    }
    public void setChoosingObjectiveController(ChoosingObjectiveController choosingObjectiveController) {
        this.choosingObjectiveController = choosingObjectiveController;
    }
    public ChoosingObjectiveController getChoosingObjectiveController() {
        return choosingObjectiveController;
    }

    public Parent getWaitingForPlayersScene() {
        return waitingForPlayersScene;
    }
    public void setWaitingForPlayersScene(Parent waitingForPlayersScene) {
        this.waitingForPlayersScene = waitingForPlayersScene;
    }

    public Parent getBoardScene() {
        return boardScene;
    }
    public void setBoardScene(Parent boardScene) {
        this.boardScene = boardScene;
    }
    public Parent getChoosingObjectiveScene() {
        return choosingObjectiveScene;
    }
    public void setChoosingObjectiveScene(Parent choosingObjectiveScene) {
        this.choosingObjectiveScene = choosingObjectiveScene;
    }
    public Parent getStartingScene() {
        return startingScene;
    }
    public void setStartingScene(Parent startingScene) {
        this.startingScene = startingScene;
    }


    public void setChoosingStarterScene(Parent choosingStarterScene) {
        this.choosingStarterScene = choosingStarterScene;
    }
    public Parent getChoosingStarterScene() {
        return choosingStarterScene;
    }

    public void setWaitingForPlayersController(WaitingForPlayersController waitingForPlayersController) {
        this.waitingPlayersController = waitingForPlayersController;
    }

    public WaitingForPlayersController getWaitingForPlayersController() {
        return waitingPlayersController;
    }

    public String askUI(){
        //gui does nothing
        return null;
    }



}
