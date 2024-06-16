package it.polimi.ingsw.network.socket.client;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.message.request.MessageServer2Client;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.network.ClientAbstract;
import it.polimi.ingsw.ui.GUI.GUI;
import it.polimi.ingsw.ui.TextUserInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends ClientAbstract {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private String username;

    public Client(ModelView modelView, String host, int port) throws IOException {
        super(modelView);

        this.socket = new Socket(host, port);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());

       /* if (opt.equals("cli")) {
            this.uiStrategy = new TextUserInterface(this);
        } else {
            this.uiStrategy = new GUI();
            this.uiStrategy.setClient(this);
                new Thread() {
                    @Override
                    public void run() {
                        javafx.application.Application.launch(GUI.class);
                    }
                }.start();
                this.uiStrategy.setViewModel(modelView);
            }

        */
        this.uiStrategy = new TextUserInterface(this);
        }

    public void start(String[] args) throws IOException, ClassNotFoundException {
        //username = args[0];
        //System.out.print("Enter your username: ");
        //System.out.println(args[0]);
        //outputStream.writeObject(args[0]);
        String usernameAsk = uiStrategy.askUsername();
        outputStream.writeObject(usernameAsk);

        // wait for server asking (join/create)
        String message = (String) inputStream.readObject();
        System.out.print(message);

        while (!message.contains("join/create")) {
            usernameAsk = uiStrategy.askUsername();
            outputStream.writeObject(usernameAsk);
            message = (String) inputStream.readObject();
            System.out.print(message);
        }


       // System.out.println(args[1]);
        //outputStream.writeObject(args[1]);
        String joinCreate = uiStrategy.askJoinCreate();
        outputStream.writeObject(joinCreate);

        // TODO: remove later, for testing

       // String in = args[1];
        String in = joinCreate;
        if(in.equals("join")) {
            message = (String) inputStream.readObject();
            if (!message.contains("ID")) {
                System.out.println("No game sessions available, creating a new one...");
                in = "create";
            }
        }
        if (in.equals("join")) {
            // get list of available game sessions
            System.out.println(message);
            System.out.print("Enter game id to join: ");
            //System.out.println(args[2]);
            //outputStream.writeObject(args[2]);

            String gameId = uiStrategy.askGameId(in, message);
            outputStream.writeObject(gameId);
        } else if (in.equals("create")) {
            // create new game session
            System.out.print("Enter the game id: ");
            //System.out.println(args[2]);
            //outputStream.writeObject(args[2]);
            String gameId = uiStrategy.askGameId(in, null);
            outputStream.writeObject(gameId);

            System.out.print("Insert number of players (2-4): ");
            //System.out.println(args[3]);
            //outputStream.writeObject(args[3]);
            int numberOfPlayers = uiStrategy.askNumberOfPlayers();
            outputStream.writeObject(numberOfPlayers);
        }

        //todo ask cli or gui
        String opt = uiStrategy.askUI();
        if (opt.equals("cli")) {
            this.uiStrategy = new TextUserInterface(this);
        } else {
            this.uiStrategy = new GUI();
            this.uiStrategy.setClient(this);
                new Thread() {
                    @Override
                    public void run() {
                        javafx.application.Application.launch(GUI.class);
                    }
                }.start();
                this.uiStrategy.setViewModel(modelView);
            }

        System.out.println("\nWaiting for server updates...\n\n");
        listenForUpdates();
    }

    public void listenForUpdates() {
        try {
            while (!socket.isClosed()) {
                Object data = inputStream.readObject();
                if (data instanceof MessageServer2Client message) {
                    message.doAction(this);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection closed or error receiving data: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /*
    public void handleEvent(GameEvent event) {
        switch (event.getType()) {
            case "loadedPawn"-> {
                observerUI.updateUI(event);
                Color colors = (Color) event.getData();
            }
            case "lastTurn" -> {
                System.out.println("Last turn! \n");
            }
            case "reachedPoints" -> {
                uiStrategy.displayMessage("You have reached 20 points, wait for the other players to finish their turn!\n");
            }
            case "endGame" -> {
                uiStrategy.displayMessage("Game over!\nResults:\n");
                for(int i = 1; i<((List<Pair<String, Integer>>) event.getData()).size()+1; i++){
                    uiStrategy.displayMessage(i+") Player: "+((List<Pair<String, Integer>>) event.getData()).get(i-1).getKey() + " Points: " + ((List<Pair<String, Integer>>) event.getData()).get(i-1).getValue() + "\n");
                }
                closeConnection();
            }
            case "mexIncoming" -> {
                if (!inCurrentTurn)
                    uiStrategy.displayMessage("New message received! You have not read it yet\n");
                unreadedMessages++;
                chat.addMsg((Message) event.getData());
            }
        }
    }
    */

    private void closeConnection() {
        try {
            if (inputStream != null) inputStream.close();
            if (outputStream != null) outputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing connections: " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(MessageClient2Server message) {
        message.appendToType(" from " + username);
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ModelView modelView = new ModelView();
        try {
            Client client = new Client(modelView, "localhost", 12345);
            client.start(null);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
    public void handleMoveUI(GameEvent gameEvent) {
        switch (gameEvent.getType()) {
            case "displayChat" -> {
                uiStrategy.displayChat(chat, username);
                if (!inCurrentTurn)
                    uiStrategy.selectFromMenu();
            }
            case "writeNewMex" -> {
                // send list of players connected to the same game session
                List<String> playersConnected = new ArrayList<>();

                if (username.equals("us1"))
                    playersConnected.add("us2");
                else
                    playersConnected.add("us1");

                gameEvent.setData(playersConnected);
                observerUI.updateUI(gameEvent);

                if (!inCurrentTurn)
                    uiStrategy.selectFromMenu();
            }
            case "sendNewMex" -> {
                // send message to server
                Message message = (Message) gameEvent.getData();
                message.setSender(username);
                chat.addMsg(message);
                try {
                    outputStream.writeObject(new GameEvent("newMessage", message));
                } catch (IOException e) {
                    System.out.println("Error sending card ID: " + e.getMessage());
                }
            }
        }
    }*/
}
