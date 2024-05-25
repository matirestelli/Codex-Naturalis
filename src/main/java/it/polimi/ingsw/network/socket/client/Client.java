package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.ui.*;
import it.polimi.ingsw.ui.GUI.GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Client implements ObserverUI{
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Scanner scanner = new Scanner(System.in);

    private UserInterfaceStrategy uiStrategy;

    private ResourceCard starterCard;
    private List<Card> hand;
    private List<Card> codex;
    private Map<Resource, Integer> resources;
    private Card cardToPlay;

    private ObserverUI observerUI;

    private ViewModel viewModel;
    private Player player;

    //to set de view as an observer so that it can be updated
    public void setObserverUI(ObserverUI observerUI) {
        this.observerUI = observerUI;
    }


    public Client(String host, int port, UserInterfaceStrategy uiStrategy) throws IOException {
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        this.viewModel = new ViewModel();
        //TODO costruttore nel view model game che crea tutto vuoto
        this.codex = new ArrayList<>();
        this.hand = new ArrayList<>();

        this.observerUI = (ObserverUI) uiStrategy;
        this.uiStrategy = uiStrategy;
        if(uiStrategy instanceof GUI){
            new Thread() {
                @Override
                public void run() {
                    javafx.application.Application.launch(GUI.class);
                }
            }.start();
            //TODO poi fare classe astratta che estende gui e cli così si spostano lì questi attributi
            ((GUI) uiStrategy).setObserverUIClient((ObserverUI)this);
            ((GUI) uiStrategy).setViewModel(viewModel);
        }
        this.uiStrategy.initialize();


        //se è gui faccio partire il thread a parte per lei


    }

    public void start(String[] args) throws IOException, ClassNotFoundException {
        System.out.print("Enter your username: ");
        //TODO farlo ad eventi
       // observerUI.updateUI(new GameEvent("username", "username"));
        // String in = scanner.nextLine();
        // outputStream.writeObject(in);
        //TODO mettere username nel ViewModel
        System.out.println(args[0]);
        outputStream.writeObject(args[0]);

        // wait for server response (join/create)
        String message = (String) inputStream.readObject();
        System.out.print(message);

        // in = scanner.nextLine();
        System.out.println(args[1]);
        outputStream.writeObject(args[1]);
        // outputStream.writeObject(in);

        // TODO: remove later, for testing
        String in = args[1];
        if (in.equals("join")) {
            // get list of available game sessions
            message = (String) inputStream.readObject();
            System.out.println(message);
            System.out.print("Enter game id to join: ");
            // send gameId to server
            // in = scanner.nextLine();
            outputStream.writeObject(args[2]);
            // outputStream.writeObject(in);
        } else if (in.equals("create")) {
            // create new game session
            System.out.print("Enter the game id: ");
            // in = scanner.nextLine();
            // outputStream.writeObject(in);
            System.out.println(args[2]);
            outputStream.writeObject(args[2]);

            System.out.print("Insert number of players: ");
            // int id = scanner.nextInt();
            // outputStream.writeObject(id);
            System.out.println(args[3]);
            outputStream.writeObject(args[3]);
        }

        System.out.println("\nWaiting for server updates...\n\n");
        listenForUpdates();
    }

    public void listenForUpdates() {
        try {
            while (!socket.isClosed()) {
                Object data = inputStream.readObject();
                if (data instanceof GameEvent) {
                    handleEvent((GameEvent) data);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection closed or error receiving data: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    private void handleEvent(GameEvent event) {
        switch (event.getType()) {
            case "notYourTurn" -> {
                // display message
                observerUI.updateUI(event);
                System.out.println("Not your turn event arrivet to client\n");
               // uiStrategy.displayMessage("Not your turn! Wait for your turn...\n");
            }
            case "loadedStarter"-> {
                System.out.println("Starter card loaded event arrived to client\n");
                // get starter card from server
                observerUI.updateUI(event);
                viewModel.setMyStarterCard((ResourceCard) event.getData());

                //starterCard = (ResourceCard) event.getData();
                // TODO: change parameters and set as class attribute
                // useful for the visualization of the starter card (TUI)
                //TODO: non so se mi serve la prossima riga
               // starterCard.setCentre(new Coordinate(10 / 2 * 7 - 5,10 / 2 * 3 - 5));

                // add starter card to codex nel VieModel
                viewModel.getMyCodex().add((Card) event.getData());
                // display starter card back
                //uiStrategy.visualizeStarterCard(starterCard);
            }
            case "updateDecks" -> {
                //TODO pensare a come parametrizzarlo meglio (se necessario)
                System.out.println("Update decks event arrived to client\n");
                List<Card> updatedDecks = (List<Card>) event.getData();
                viewModel.getResourceCardsVisible().add(updatedDecks.get(0));
                viewModel.getResourceCardsVisible().add(updatedDecks.get(1));
                viewModel.getGoldCardsVisible().add(updatedDecks.get(2));
                viewModel.getGoldCardsVisible().add(updatedDecks.get(3));
                viewModel.setDeckRBack(updatedDecks.get(4));
                viewModel.setDeckGBack(updatedDecks.get(5));
                observerUI.updateUI(event);
            }

            case "starterSide"-> {
                System.out.println("Starter side event arrived to client\n");
                // ask user to set side of the starter card
                //boolean side = uiStrategy.setStarterSide();
                observerUI.updateUI(event);
                // set side of the starter card
               // starterCard.setSide(side);

                // place starter card on the board
                //uiStrategy.placeCard(starterCard, null);

                // display board
                //uiStrategy.displayBoard();

                // send side to server
                /*
                try {
                    outputStream.writeObject(new GameEvent("starterSideSelection", side));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }

                 */
            }
            case "askWhereToDraw"-> {
                System.out.println("Ask where to draw event arrived to client\n");
                //TODO: PUT IN CLI:
                //String input = uiStrategy.askWhereToDraw((List<Card>) event.getData());
                observerUI.updateUI(event);

                //try {
                //    outputStream.writeObject(new GameEvent("drawCard", input));
               // } catch (IOException e) {
                //    System.out.println("Error sending card ID: " + e.getMessage());
               // }
            }
            case "loadedCommonObjective" -> {
                System.out.println("Common objective event arrived to client\n");
               //TODO PUT IN CLI: uiStrategy.displayCommonObjective((List<Objective>) event.getData());
                observerUI.updateUI(event);
                //in realtà la gui non ci deve fare nulla con questo evento perchè quando si inizializza prende dal view model
                viewModel.setCommonObj((List<Objective>) event.getData());
            }

            case "chooseObjective" -> {
                System.out.println("Choose objective event arrived to client\n");
                observerUI.updateUI(event);
                //TODO PUT IN CLI:
             //   Objective card = uiStrategy.chooseObjective((List<Objective>) event.getData());

               // try {
               //     outputStream.writeObject(new GameEvent("secretObjectiveSelection", card));
               // } catch (IOException e) {
               //     System.out.println("Error sending card ID: " + e.getMessage());
              //  }
            }
            case "askAngle" -> {
                System.out.println("Ask angle event arrived to client\n");
                //String input = uiStrategy.displayAngle((List<Coordinate>) event.getData());

                //TODO PUT IN CLI:
                // get card from player's hand by id
                // TODO: create object for handling card selection
               // String[] splitCardToPlay = input.split("\\.");
               // int cardToAttachId = Integer.parseInt(splitCardToPlay[0]);

                // card where to attach the selected card
               // Card targetCard = codex.stream().filter(c -> c.getId() == cardToAttachId).findFirst().orElse(null);

                //  uiStrategy.place(cardToPlay, targetCard, Integer.parseInt(splitCardToPlay[1]));

               // uiStrategy.displayBoard();
                System.out.println("ask angle arrived to client ");
                observerUI.updateUI(event);

                //try {
                //    outputStream.writeObject(new GameEvent("angleSelection", new CardToAttachSelected(input)));
               // } catch (IOException e) {
                //    System.out.println("Error sending angles: " + e.getMessage());
               // }
            }
            case "updateHand" -> {
                System.out.println("Update hand event arrived to client\n");
                //hand = (List<Card>) event.getData();
                viewModel.setMyHand((List<Card>) event.getData());
                //TODO PUT IN CLI:
                //uiStrategy.displayHand(hand);
                observerUI.updateUI(event);
                //la gui/cli stampano la mano aggiornata del giocatore
            }

            case "updateCodex" -> {
                //TODO capire come mettere e passare il codex e matrice degli altri giocatori
                System.out.println("Update codex event arrived to client\n");
                codex = (List<Card>) event.getData();
            }
            case "beforeTurnEvent" -> {
                System.out.println("Before turn event arrived to client\n");
                resources = (Map<Resource, Integer>) event.getData();
            }
            case "currentPlayerTurn" -> {
                System.out.println("Current player turn event arrived to client\n");
                observerUI.updateUI(event);

                //TODO PUT IN CLI:
                // CardSelection cs = uiStrategy.askCardSelection((PlayableCardIds) event.getData(), hand);
                //cardToPlay = hand.stream().filter(c -> c.getId() == cs.getId()).findFirst().orElse(null);
                //hand.remove(cardToPlay);
            }
            case "lastTurn" -> {
                System.out.println("Last turn event arrived to client\n");
                // TODO: bug fix
                System.out.println("Last turn! Select a card ID to play: ");
                int cardId = scanner.nextInt();

                try {
                    outputStream.writeObject(new CardSelection(cardId, false));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
            case "endGame" -> {
                System.out.println("End game event arrived to client\n");
                uiStrategy.displayMessage("Game over!");
                closeConnection();
            }
        }
    }

    private void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing connections: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        UserInterfaceStrategy uiStrategy;
        //GUI gui = new GUI();
      //  gui.main(null);

        //String opt = "cli";
        String opt = "gui";
        if (opt.equals("cli")) {
            uiStrategy = new TextUserInterface();
        } else {
           //uiStrategy = new GraphicalUserInterface();
            uiStrategy = new GUI();
           // ((GUI) uiStrategy).main(null);
            //Application.launch(GUI.class, args);
        }

        try {
            Client client = new Client("localhost", 12345, uiStrategy);
            client.start(args);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUI(GameEvent gameEvent) {
        switch (gameEvent.getType()) {
            case "starterSide" -> {
                // set side of the starter card

                boolean side = (boolean) gameEvent.getData();
                //added the side choosen to the starter card in the view model
                viewModel.getMyStarterCard().setSide(side);
                //added the starter card to the codex in the view model
                viewModel.getMyMatrix()[40][40] = viewModel.getMyStarterCard().getId();

                // place starter card on the board
               // uiStrategy.placeCard(starterCard, null);

                // display board
              //  uiStrategy.displayBoard();

                // send side to server
                try {
                    outputStream.writeObject(new GameEvent("starterSideSelection", side));
                    System.out.printf("side: %b\n", side);
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }

            case "chooseObjective" -> {
                Objective card = (Objective) gameEvent.getData();
                //added the objective card to the view model
                viewModel.setSecretObj(card);
                try {
                    outputStream.writeObject(new GameEvent("secretObjectiveSelection", card));
                    System.out.printf("card: %d\n", card.getId());
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }

            case "cardToPlaySelected" -> {
                CardSelection cs = (CardSelection) gameEvent.getData();
                try {
                    outputStream.writeObject(new GameEvent("cardSelection", cs));
                    System.out.printf("card: %d\n", cs.getId());
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }

            case "angleSelected" -> {
                try {
                    outputStream.writeObject(new GameEvent("angleSelection", new CardToAttachSelected((String) gameEvent.getData())));
                       System.out.printf("angle: %s\n", (String) gameEvent.getData());
                     } catch (IOException e) {
                        System.out.println("Error sending angles: " + e.getMessage());
                    }
            }

            case "whereToDrawSelected" -> {
                String input = (String) gameEvent.getData();
                try {
                    outputStream.writeObject(new GameEvent("drawCard", input));
                    System.out.printf("where to draw: %s\n", (String) gameEvent.getData());
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }

        }


    }
}
