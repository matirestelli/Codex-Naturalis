package it.polimi.ingsw.ui.GUI;

import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.ui.GUI.boardstate.TurnStateEnum;
import it.polimi.ingsw.ui.GUI.controller.*;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.ui.ObserverUI;
import it.polimi.ingsw.ui.UserInterfaceStrategy;
import it.polimi.ingsw.ui.ViewModel;
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



import java.io.IOException;
import java.util.List;


public class GUI extends Application implements UserInterfaceStrategy, ObserverUI {

    private static Stage currStage;
    private Parent root;
    private double xStage;
    private double yStage;
 //TODO volgio che sia visibile ai figli, non so se protected è il miglior modo
 protected static ObserverUI observerClient; //il client che possiede la view

    //il player del client che possiede alla view
    private Player client;

    //il viewmodel del gioco che verrà modificato dopo gli update del server
    //NB il view model anche del client della view viene modificato solo dopo l'update del server non mentre mando al server la mossa
    //questo perchè se il server ritiene la mossa non valida non la registra e non manda un update
    protected static ViewModel viewModel;

    //lista dei controller delle varie scene
    private static StartingSceneController startingSceneController;
    private static JoinAGameController joinAGameController;
    private static  WaitingForPlayersController waitingForPlayersController;
    private static LobbyGamesController lobbyGamesController;
    private static BoardViewController boardViewController;
    private static SettingUsernameController settingUsernameController;
    private static CreatingNewGameController creatingNewGameController;
    private static SettingViewController settingViewController;
    private static ErrorPopUpController errorPopUpController;
    private static ScoreboardController scoreboardController;
    private static TurnStateEnum turnState; //lo stato del turno in cui si trova ora il client
    private static ChatController chatController;
    private static ChoosingObjectiveController choosingObjectiveController;
    private static ChoosingStarterController choosingStarterController;

    private static CardGame starterCard;
    private static Boolean test;
    private static List<Objective> secretObjs;


    private static Parent joinAGameScene, waitingForPlayersScene, lobbyGamesScene, settingUsernameScene, creatingNewGameScene, settingViewScene, boardScene, choosingObjectiveScene, choosingStarterScene;
    private static Parent startingScene;


    @Override
    public void start (Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/StartingScene.fxml"));
            root = loader.load();
            startingSceneController = loader.getController();
        } catch (Exception e) {
            System.out.println("Error loading StartingScene.fxml");
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
        //primaryStage.getScene().getStylesheets().add("it/polimi/ingsw/gc38/cssFiles/style.css");
        currStage.show();
    }
    /*
     Main GUI class with JavaFX scenes and controllers.
     */
    public static void main(String[] args) {
        Application.launch(args);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/JoinAGame.fxml"));
            this.setJoinAGameScene(loader.load());
            this.setJoinAGameController(loader.getController());

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/LobbyGames.fxml"));
            this.setLobbyGamesScene(loader.load());
            this.setLobbyGamesController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/SettingUsername.fxml"));
            this.setSettingUsernameScene(loader.load());
            this.setSettingUsernameController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/CreatingNewGame.fxml"));
            this.setCreatingNewGameScene(loader.load());
            this.setCreatingNewGameController(loader.getController());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/SettingView.fxml"));
            this.setSettingViewScene(loader.load());
            this.setSettingViewController(loader.getController());

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
            System.out.println("ChoosingObjective.fxml loaded");

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

            case "/it/polimi/ingsw/scenes/JoinAGame.fxml":
                try {
                   // FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                   // root = loader.load();
                   // joinAGameController = loader.getController();
                    root = this.getJoinAGameScene();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "/it/polimi/ingsw/scenes/WaitingForPlayers.fxml":
                try {
                   // FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                   // root = loader.load();
                  //  waitingForPlayersController = loader.getController();
                    root = this.getWaitingForPlayersScene();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "/it/polimi/ingsw/scenes/LobbyGames.fxml":
                try {
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                    //root = loader.load();
                    //lobbyGamesController = loader.getController();
                    root = this.getLobbyGamesScene();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "/it/polimi/ingsw/scenes/SettingUsername.fxml":
                try {
                   // FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                   // root = loader.load();
                   // settingUsernameController = loader.getController();
                    root = this.getSettingUsernameScene();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "/it/polimi/ingsw/scenes/CreatingNewGame.fxml":
                try {
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                   // root = loader.load();
                    //creatingNewGameController = loader.getController();
                    root = this.getCreatingNewGameScene();
                    currStage.setScene(new Scene(root));
                    currStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "/it/polimi/ingsw/scenes/SettingView.fxml":
                try {
                    //FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
                   // root = loader.load();
                    //settingViewController = loader.getController();
                    root = this.getSettingViewScene();
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
    public void viewChat( Stage chatStage) throws IOException {
        double x = chatStage.getX();
        double y = chatStage.getY();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/scenes/Chat.fxml"));
            Parent root = loader.load();
            chatController = loader.getController();
            ScaleTransition st = new ScaleTransition(javafx.util.Duration.millis(50), root);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.setFromX(0);
            st.setFromY(0);
            st.setToX(1);
            st.setToY(1);

            Stage stage1 = new Stage();
            stage1.setTitle("Chat");
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


    @Override
    public void updateUI(GameEvent event) {
        switch(event.getType()){
            case "notYourTurn" -> {
                //TODO fix the fact that it is shown when i go to the board scene -> maybe not show not your turn
                //only not your turn if i try to do something
                Platform.runLater(() -> {
                    this.setTurnState(TurnStateEnum.NOT_YOUR_TURN);
                   // this.showErrorPopUp("It's not your turn", currStage);
                    this.getBoardViewController().message("It's not your turn");
                });
            }

            case "loadedStarter" ->{
                //initializeScenes();
                //TODO file fxml e controller
                // choosingStarterController.setStarterCard(event.getData());
                System.out.println("Starter card loaded");
                Platform.runLater(() -> {
                    this.setStarterCard((CardGame)event.getData());
                    System.out.printf("Starter card loaded: %d", this.getStarterCard().getId());
                    System.out.printf("Starter card loaded: %s", this.getStarterCard().getFrontCover());

                    //TODO: sarà così solo che ora il messaggio arriva troppo presto e non ho ancora l'istanza del controller
                    //perchè il messaggio arriva ancora prima di aver caricato il main dell'applicazione
                    //this.getChoosingStarterController().setStarterCard((CardGame)event.getData());
                });


            }
            case "starterSide" -> {
                Platform.runLater(() -> {
                    this.test= true;
                    System.out.println("ask for choice arrived");
                    //this.getChoosingStarterController().setStarterSide();
                });
            }

            case "chooseObjective" -> {
                System.out.println("ask for objective arrived to view");
                Platform.runLater(() -> {
                    //in questo momento non ho ancora fatto load dei controller e quidni gestisco la cosa così:
                    //this.getChoosingObjectiveController().setObjective((List<Objective>)event.getData());
                    // this.getChoosingObjectiveController().chooseObjective();
                    secretObjs = (List<Objective>)event.getData();
                    System.out.println("ask for objective arrived");
                    //this.getChoosingObjectiveController().chooseObjective();
                });
            }

            case "updateHand" -> {
                Platform.runLater(() -> {
                    System.out.println("update hand arrived to view");
                    this.getBoardViewController().updateHand((List<Card>)event.getData());
                });
            }

            case "updateDecks" -> {
                Platform.runLater(() -> {
                    //TODO ora da errore perchè non ho ancora caricato il controller, in teoria quando fa queste cose poi prima aspetta username
                    this.getBoardViewController().updateDecks((List<Card>)event.getData());
                });
            }

            case "currentPlayerTurn" -> {
                Platform.runLater(() -> {
                    System.out.println("current player turn arrived to view");
                    this.setTurnState(TurnStateEnum.SELECT_CARD);
                    this.getBoardViewController().selectCardToPlay((PlayableCardIds)event.getData());
                });
            }

            case "askAngle" -> {
                Platform.runLater(() -> {
                    System.out.println("ask for angle arrived to view");
                    this.getBoardViewController().askForAngle((List<Coordinate>)event.getData());
                });
            }

            case "askWhereToDraw" -> {
                System.out.println("ask for where to draw arrived to client");
                Platform.runLater(() -> {
                    System.out.println("ask for where to draw arrived to view");
                    this.getBoardViewController().drawFromDecks();
                });
            }

            case "updateMyPlayerstate" -> {
                Platform.runLater(() -> {
                    this.getBoardViewController().updateMyPlayerstate();
                });
            }

            case "updatePlayerstate" -> {
                Platform.runLater(() -> {
                    this.getBoardViewController().updatePlayerstate((String) event.getData());
                });

            }

            case "lastTurn" -> {
                //todo capire cosa inviano come event.data
                Platform.runLater(() -> {
                    this.getBoardViewController().message("This is your Last Turn \n Make it count!");
                    this.getBoardViewController().selectCardToPlay((PlayableCardIds)event.getData());
                });
            }

            case "endGame" -> {
                //todo capire cosa inviano come event.data
                Platform.runLater(() -> {
                    this.getBoardViewController().message("The game has ended");
                });
            }

        }
    }


    @Override
    public void initialize() {
    }

    @Override
    public void displayMessage(String message) {

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
    public boolean setStarterSide() {
        return false;
    }

    @Override
    public void displayCommonObjective(List<Objective> obj) {

    }

    @Override
    public Objective chooseObjective(List<Objective> obj) {
        return null;
    }

    @Override
    public void displayHand(List<Card> hand) {

    }

    @Override
    public void place(Card cardToPlace, Card targetCard, int position) {

    }

    @Override
    public String askWhereToDraw(List<Card> cards) {
        return null;
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
    public void setObserverClient(ObserverUI observerClient) {
        this.observerClient = observerClient;
    }
    public void setClient(Player client) {
        this.client = client;
    }
    public void setViewModel(ViewModel viewModel) {
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
        System.out.printf("Starter card loaded: %d", this.starterCard.getId());
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
    public void setJoinAGameController(JoinAGameController joinAGameController) {
        this.joinAGameController = joinAGameController;
    }
    public JoinAGameController getJoinAGameController() {
        return joinAGameController;
    }
    public void setWaitingForPlayersController(WaitingForPlayersController waitingForPlayersController) {
        this.waitingForPlayersController = waitingForPlayersController;
    }
    public WaitingForPlayersController getWaitingForPlayersController() {
        return waitingForPlayersController;
    }
    public void setLobbyGamesController(LobbyGamesController lobbyGamesController) {
        this.lobbyGamesController = lobbyGamesController;
    }
    public LobbyGamesController getLobbyGamesController() {
        return lobbyGamesController;
    }
    public void setBoardViewController(BoardViewController boardViewController) {
        this.boardViewController = boardViewController;
    }
    public BoardViewController getBoardViewController() {
        return boardViewController;
    }
    public void setSettingUsernameController(SettingUsernameController settingUsernameController) {
        this.settingUsernameController = settingUsernameController;
    }
    public SettingUsernameController getSettingUsernameController() {
        return settingUsernameController;
    }
    public void setCreatingNewGameController(CreatingNewGameController creatingNewGameController) {
        this.creatingNewGameController = creatingNewGameController;
    }
    public CreatingNewGameController getCreatingNewGameController() {
        return creatingNewGameController;
    }
    public void setSettingViewController(SettingViewController settingViewController) {
        this.settingViewController = settingViewController;
    }
    public SettingViewController getSettingViewController() {
        return settingViewController;
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

    public void setJoinAGameScene(Parent joinAGameScene) {
        this.joinAGameScene = joinAGameScene;
    }

    public Parent getJoinAGameScene() {
        return joinAGameScene;
    }
    public Parent getWaitingForPlayersScene() {
        return waitingForPlayersScene;
    }
    public void setWaitingForPlayersScene(Parent waitingForPlayersScene) {
        this.waitingForPlayersScene = waitingForPlayersScene;
    }
    public Parent getLobbyGamesScene() {
        return lobbyGamesScene;
    }
    public void setLobbyGamesScene(Parent lobbyGamesScene) {
        this.lobbyGamesScene = lobbyGamesScene;
    }
    public Parent getSettingUsernameScene() {
        return settingUsernameScene;
    }
    public void setSettingUsernameScene(Parent settingUsernameScene) {
        this.settingUsernameScene = settingUsernameScene;
    }
    public Parent getCreatingNewGameScene() {
        return creatingNewGameScene;
    }
    public void setCreatingNewGameScene(Parent creatingNewGameScene) {
        this.creatingNewGameScene = creatingNewGameScene;
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
    public Parent getSettingViewScene() {
        return settingViewScene;
    }
    public void setSettingViewScene(Parent settingViewScene) {
        this.settingViewScene = settingViewScene;
    }

    public void setChoosingStarterScene(Parent choosingStarterScene) {
        this.choosingStarterScene = choosingStarterScene;
    }
    public Parent getChoosingStarterScene() {
        return choosingStarterScene;
    }

    public void setObserverUIClient(ObserverUI observerUI) {
        this.observerClient = observerUI;
    }
    public void getObserverUIClient(ObserverUI observerUI) {
        this.observerClient = observerUI;
    }


}
