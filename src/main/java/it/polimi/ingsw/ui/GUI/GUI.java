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
 //TODO volgio che sia visibile ai figli, non so se protected è il miglior modo
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

    //todo se funziona l'idea di cambiare scena io di deafault eliminarli
    private static PlayableCardIds firstTurn;
    private static Boolean firstTurnBool = false;

    protected static ArrayList<Timer> timers = new ArrayList<>();


    private static volatile boolean javaFxLaunched = false;

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

    @Override
    public void start (Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/StartingScene.fxml"));
            root = loader.load();
            startingSceneController = loader.getController();
            startingSceneController.ifGameNotStarted();
        } catch (Exception e) {
          //  System.out.println("Error loading StartingScene.fxml");
            e.printStackTrace();
        }
        currStage = primaryStage;
        // Parent root = FXMLLoader.load(getClass().getResource("it/polimi/ingsw/scenes/StartingScene.fxml"));
        currStage.setTitle("StartingGame");
        Image icon = new Image("/it/polimi/ingsw/icons/iconaCodex.png");
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
        //primaryStage.getScene().getStylesheets().add("it/polimi/ingsw/gc38/cssFiles/style.css");
        currStage.show();
    }
    /*
     Main GUI class with JavaFX scenes and controllers.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    //TODO
    public void displayScoreboard() {
    }

    //TODO
    public void displayPersonalResources(Map<Resource, Integer> data) {
    }

    public void noFreeAngles() {
        //gui does nothing for now
    }

    public void getBoardString(String board) {
        //only for cli
    }

    public void initializeScenes() {
        //inizializzo tutte le scene e i relativi controller così da poter usare tutti i metodi sui controller anche se l'utente non ha ancora cambiato scena
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/StartingScene.fxml"));
            this.setStartingScene(loader.load());
            this.setStartingSceneController( loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/BoardScene.fxml"));
            this.setBoardScene(loader.load());
            this.setBoardViewController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/WaitingForPlayers.fxml"));
            this.setWaitingForPlayersScene(loader.load());
            this.setWaitingForPlayersController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/ChoosingStarter.fxml"));
            this.setChoosingStarterScene(loader.load());
            this.setChoosingStarterController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/ChoosingObjective.fxml"));
            this.setChoosingObjectiveScene(loader.load());
            this.setChoosingObjectiveController(loader.getController());
          //  System.out.println("ChoosingObjective.fxml loaded");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void changeScene(String sceneName, Stage stage){
        //gui ha quindi in curr stage lo stage dell'ultima scena che ha attivato
        currStage = stage;
       // initializeScenes();
        //per ora chiamo qui initializeScenes ma poi sarà nel metodo inizialize della cui generale interfaccia
        switch (sceneName){
            case "/it/polimi/ingsw/scenes/StartingScene.fxml":
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

            case "/it/polimi/ingsw/scenes/WaitingForPlayers.fxml":
                try {
                   FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
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


            case "/it/polimi/ingsw/scenes/BoardScene.fxml":

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

            case "/it/polimi/ingsw/scenes/ChoosingStarter.fxml":
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

            case "/it/polimi/ingsw/scenes/ChoosingObjective.fxml":
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
                //eccezione
                break;
        }




    }



    public void showErrorPopUp(String message, Stage errorStage) {
        double x = errorStage.getX();
        double y = errorStage.getY();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/ErrorPopUp.fxml"));
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
    public void showScoreboardPopUp(Stage errorStage){
        double x = errorStage.getX();
        double y = errorStage.getY();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/Scoreboard.fxml"));
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

    //per ora con il main ma vediamo


    public void showErrorPopUpNoStage(String message) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/ErrorPopUp.fxml"));
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

    public void gameStarted() {
        if(wasWaitingForPlayers) {
            Platform.runLater(() -> {
                changeScene("/it/polimi/ingsw/scenes/WaitingForPlayers.fxml", currStage);
            });
        }
    }
    @Override
    public void chooseObjective(List<Objective> obj) {
       // System.out.println("ask for objective arrived to view");
        Platform.runLater(() -> {
            //in questo momento non ho ancora fatto load dei controller e quidni gestisco la cosa così:
            //this.getChoosingObjectiveController().setObjective((List<Objective>)event.getData());
            // this.getChoosingObjectiveController().chooseObjective();
            secretObjs = obj;
          //  System.out.println("ask for objective arrived");
            //this.getChoosingObjectiveController().chooseObjective();
        });
    }


    @Override
    public void setStarterSide() {
        //gui does nothing for now
        //perchè il messaggio arriva ancora prima di aver caricato il main dell'applicazione
        //this.getChoosingStarterController().setStarterCard((CardGame)event.getData());
    }

    @Override
    public void visualiseStarterCardLoaded(Card card) {
        Platform.runLater(() -> {
            this.setStarterCard( card);
            //System.out.printf("Starter card loaded: %d", this.getStarterCard().getId());
            //System.out.printf("Starter card loaded: %s", this.getStarterCard().getFrontCover());

            //TODO: sarà così solo che ora il messaggio arriva troppo presto e non ho ancora l'istanza del controller
            //perchè il messaggio arriva ancora prima di aver caricato il main dell'applicazione
            //this.getChoosingStarterController().setStarterCard((CardGame)event.getData());
        });

    }

    @Override
    public void updateDecks(List<Card> updatedDecks) {
        Platform.runLater(() -> {
            //TODO ora da errore perchè non ho ancora caricato il controller, in teoria quando fa queste cose poi prima aspetta username
            if(!notYetBoardScene) {
                this.getBoardViewController().updateDecks(updatedDecks);
            }
        });
    }


    @Override
    public void currentTurn(PlayableCardIds data) {
        Platform.runLater(() -> {
            client.getModelView().setMyTurn(true);
            if(notYetBoardScene){
                //todo controllare se funziona
                changeScene("/it/polimi/ingsw/scenes/BoardScene.fxml", currStage);
            }
            this.getBoardViewController().selectCardToPlay(data);
        });
    }

    @Override
    public void updateMyPlayerState() {
        Platform.runLater(() -> {
            this.getBoardViewController().updateMyPlayerstate();
        });
    }

    @Override
    public void updatePlayerState(String player) {
        Platform.runLater(() -> {
            this.getBoardViewController().updatePlayerstate(player);
        });
    }

    @Override
    public void showAvailableAngles(List<Coordinate> data) {
        Platform.runLater(() -> {
            this.getBoardViewController().askForAngle(data);
        });
    }

    @Override
    public void askWhereToDraw(List<Card> cards) {
        Platform.runLater(() -> {
            this.getBoardViewController().drawFromDecks();
        });
    }

    @Override
    public void displayHand(List<Card> hand) {
        Platform.runLater(() -> {
            if(!notYetBoardScene){
               // System.out.println("update hand arrived to view");
                this.getBoardViewController().updateHand(hand);
            }
        });
    }

    @Override
    public void showNotYourTurn() {
        Platform.runLater(() -> {
            if(notYetBoardScene){
                //todo controllare se funziona
                changeScene("/it/polimi/ingsw/scenes/BoardScene.fxml", currStage);
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

    @Override
    public void newChatMessage(Message message){
        Platform.runLater(() -> {
            this.getBoardViewController().updateChat(message);
        });
    }

    @Override
    public void lastTurn() {
        //todo capire cosa inviano come event.data
        Platform.runLater(() -> {
            this.getBoardViewController().message("This is your Last Turn \n Play carefully!");
            //in teoria nessun dato, solo messaggio diverso
        });
    }

    @Override
    public void endGame(List<Pair<String, Integer>> rank) {
    //todo capire cosa inviano come event.data
        Platform.runLater(() -> {
            if(notYetBoardScene){
                this.gameEnded();
            }
            else{
                this.getBoardViewController().endGameRanking(rank);
            }
        });
    }



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

    }

    @Override
    public void displayCardBack(Card card) {

    }

    @Override
    public void displayStarterCardBack(ResourceCard card) {

    }

    @Override
    public String displayResourcesStarter(ResourceCard card, int index1, int index2) {
        return null;
    }

    @Override
    public void placeCard(Card card, Coordinate position) {

    }

    @Override
    public void displayBoard() {
    }

    @Override
    public CardSelection askCardSelection(PlayableCardIds ids, List<Card> cards) {
        return null;
    }

    @Override
    public String displayAngle(List<Coordinate> angles) {
        return null;
    }

    @Override
    public Coordinate placeBottomRight(Card targetCard, Card cardToPlace) {
        return null;
    }

    @Override
    public Coordinate placeTopLeft(Card targetCard, Card cardToPlace) {
        return null;
    }

    @Override
    public Coordinate placeTopRight(Card targetCard, Card cardToPlace) {
        return null;
    }

    @Override
    public Coordinate placeBottomLeft(Card targetCard, Card cardToPlace) {
        return null;
    }

    @Override
    public void visualizeStarterCard(Card card) {

    }

    @Override
    public void displayCommonObjective(List<Objective> obj) {
        // gui does that automatically
    }



    @Override
    public void place(Card cardToPlace, Card targetCard, int position) {

    }

    @Override
    public void displayChat(Chat chat, String username) {

    }

    @Override
    public void selectFromMenu() {

    }

    @Override
    public String askUsername() {
        return null;
    }

    @Override
    public String askJoinCreate() {
        return null;
    }

    @Override
    public String askGameId(String joinCreate, String gameIds) {
        return null;
    }

    @Override
    public int askNumberOfPlayers() {
        return 0;
    }

    @Override
    public void displayPawn(it.polimi.ingsw.core.model.enums.Color pawn) {

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
        return null;
    }



}
