package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;


public class ChatController {
    private Stage stage;
    private double x,y;


    /* parte socket che non so fare
    private static final DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket(); // init to any available port
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private static final InetAddress address;

    static {
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


     */

    private static final int SERVER_PORT = 8000; // send to server
    @FXML
    private TextArea messageAreaBroadcast, messageArea1, messageArea2, messageArea3;
    @FXML
    private TextField inputBox = new TextField();
    @FXML
    private Tab chatBroadcast, chatDirect, chatPlayer1, chatPlayer2, chatPlayer3;
    @FXML
    private TabPane broadcastTabPane, directTabPane;
    private ModelView viewModel;
    private Player client;
    private int numPlayers;
    private List<String> listPlayersUsername;
    private List<Player> contectedPlayers;
    private Map<Player, List<String[]>> directMessageClient;

    public void initialize(ModelView viewModel, Player client) {
        //inizializza la chat inserendo i nomi dei giocatori della partita e recupera i vecchi messaggi se ci sono quando hai chiudo
        //la chat l'ultima volta
        /*
        this.viewModelGame = viewModelGame;
        this.client = client;
        this.numPlayers = viewModelGame.getNumPlayers();
        this.directMessageClient = client.getPrivateChat();
        for(int i = 0; i < numPlayers; i++){
            if(!viewModelGame.getPlayer(i).getUsername().equals(client.getUsername()))
            listPlayers.add(viewModelGame.getPlayer(i).getUsername());
            contactedPlayers.add(viewModelGame.getPlayer(i));
        }
        switch (numPlayers) {
            case 2:
                chatPlayer1.setText(listPlayers.get(0));
                for(int i = 0; i < directMessageClient.get(contactedPlayers.get(0).size(); i++){
                    messageArea1.setText(messageArea1.getText() + directMessageClient.get(contactedPlayers.get(0)).get(i)[0] + ": " + directMessageClient.get(contactedPlayers.get(0)).get(i)[1] + "\n");
                }
                chatPlayer2.setDisable(true);
                chatPlayer3.setDisable(true);
                break;
            case 3:
                chatPlayer1.setText(listPlayers.get(0));
                for(int i = 0; i < directMessageClient.get(contactedPlayers.get(0).size(); i++){
                    messageArea1.setText(messageArea1.getText() + directMessageClient.get(contactedPlayers.get(0)).get(i)[0] + ": " + directMessageClient.get(contactedPlayers.get(0)).get(i)[1] + "\n");
                }
                chatPlayer2.setText(listPlayers.get(1));
                for(int i = 0; i < directMessageClient.get(contactedPlayers.get(1).size(); i++){
                    messageArea1.setText(messageArea1.getText() + directMessageClient.get(contactedPlayers.get(1)).get(i)[0] + ": " + directMessageClient.get(contactedPlayers.get(1)).get(i)[1] + "\n");
                }
                chatPlayer3.setDisable(true);
                break;
            case 4:
                chatPlayer1.setText(listPlayers.get(0));
                for(int i = 0; i < directMessageClient.get(contactedPlayers.get(0).size(); i++){
                    messageArea1.setText(messageArea1.getText() + directMessageClient.get(contactedPlayers.get(0)).get(i)[0] + ": " + directMessageClient.get(contactedPlayers.get(0)).get(i)[1] + "\n");
                }
                chatPlayer2.setText(listPlayers.get(1));
                for(int i = 0; i < directMessageClient.get(contactedPlayers.get(1).size(); i++){
                    messageArea1.setText(messageArea1.getText() + directMessageClient.get(contactedPlayers.get(1)).get(i)[0] + ": " + directMessageClient.get(contactedPlayers.get(1)).get(i)[1] + "\n");
                }
                chatPlayer3.setText(listPlayers.get(2));
                for(int i = 0; i < directMessageClient.get(contactedPlayers.get(2).size(); i++){
                    messageArea1.setText(messageArea1.getText() + directMessageClient.get(contactedPlayers.get(2)).get(i)[0] + ": " + directMessageClient.get(contactedPlayers.get(2)).get(i)[1] + "\n");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid number of players");
                break;
        }


         */

    }
    public void sendMessage(KeyEvent keyEvent) {
        Tab tab = broadcastTabPane.getSelectionModel().getSelectedItem();
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String temp =  "you: " + inputBox.getText(); // message to send
            if(tab.equals(chatBroadcast))
                messageAreaBroadcast.setText(messageAreaBroadcast.getText() + temp + "\n"); // update messages on screen

            else if(tab.equals(chatDirect)){
                Tab tab2 = directTabPane.getSelectionModel().getSelectedItem();
                if(tab2.equals(chatPlayer1))
                    messageArea1.setText(messageArea1.getText() + temp + "\n"); // update messages on screen
                else if(tab2.equals(chatPlayer2))
                    messageArea2.setText(messageArea2.getText() + temp + "\n"); // update messages on screen
                else if(tab2.equals(chatPlayer3))
                    messageArea3.setText(messageArea3.getText() + temp + "\n"); // update messages on screen
            }


            byte[] msg = temp.getBytes(); // convert to bytes
            inputBox.setText(""); // remove text from input box

                    /* manda il messaggio con socket
                    // create a packet & send
                    DatagramPacket send = new DatagramPacket(msg, msg.length, address, SERVER_PORT);
                    try {
                        socket.send(send);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                     */
        }
    }

    public void closePopUp(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
    @FXML void dragged(MouseEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }
}

