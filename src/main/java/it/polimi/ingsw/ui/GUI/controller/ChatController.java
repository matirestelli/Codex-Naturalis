package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.message.response.messageBroadcast;
import it.polimi.ingsw.core.model.message.response.messagePrivate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import it.polimi.ingsw.ui.GUI.GUI;


public class ChatController extends GUI{
    private Stage stage;
    private double x,y;

    @FXML
    private TextArea messageAreaBroadcast, messageArea1, messageArea2, messageArea3;
    @FXML
    private TextField inputBox = new TextField();
    @FXML
   // private Tab chatBroadcast, chatDirect, chatPlayer1, chatPlayer2, chatPlayer3;
    private Tab chatBroadcast, chatDirect;
    @FXML
    private TabPane broadcastTabPane, directTabPane;

    private int numPlayers;
    private List<String> listPlayersUsername;
    private Map<Player, List<String[]>> directMessageClient;
    private String myUsername;
    private Chat chat;
    private Tab privateTab;
    private TextArea privateTextArea;
    private AnchorPane privateAnchorPane;
    private ImageView imageChat;

    /**
     * Initializes the chat interface with player names and previous messages.
     * Retrieves the list of players, current username, chat history, and sets
     * unread message count to zero. Creates tabs for each player (except the current user)
     * with a text area to display messages. Displays previous chat messages if they exist.
     * <p>
     * This method populates the chat interface with tabs for each player, where each tab contains
     * a text area to display messages. If there are previous private messages, they are displayed
     * in the corresponding tabs. Broadcast messages are displayed in the broadcast message area.
     * </p>
     * <p>
     * Note: This method assumes that the necessary data (players, username, chat history)
     * has been set in the client's model view.
     * </p>
     */
    public void initialize() {
        //inizializza la chat inserendo i nomi dei giocatori della partita e recupera i vecchi messaggi se ci sono quando hai chiudo
        //la chat l'ultima volta
        this.numPlayers = client.getModelView().getPlayers().size();
        this.listPlayersUsername = client.getModelView().getPlayers();
        this.myUsername = client.getModelView().getMyUsername();
        this.chat = client.getModelView().getChat();
        client.getModelView().setMyUnreadedMessages(0);

        for(String username : listPlayersUsername){
            if(!username.equals(myUsername)) {
                Tab tab = new Tab(username);
                //id of the tab is the username of the player I'm chatting with
                tab.setId(username);
               // System.out.println("tab id: " + tab.getId());
                tab.setText(username);
                TextArea textArea = new TextArea();
                textArea.setEditable(false);
                textArea.setId("messageArea" + username);
                textArea.setMinWidth(0);
                textArea.setMinHeight(0);
                textArea.setPrefHeight(380);
                textArea.setPrefWidth(320);
                textArea.setMaxHeight(380);
                textArea.setMaxWidth(320);
                textArea.setLayoutX(20);
                textArea.setLayoutY(8);
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.setMinWidth(0);
                anchorPane.setMinHeight(0);
                anchorPane.setPrefHeight(380);
                anchorPane.setPrefWidth(320);
                anchorPane.setMaxHeight(380);
                anchorPane.setMaxWidth(320);
                anchorPane.setStyle("-fx-border-radius: 18 18 18 18 ; -fx-background-radius: 18 18 18 18 ;-fx-alignment: center");
                anchorPane.getChildren().add(textArea);
                tab.setContent(anchorPane);
                directTabPane.getTabs().add(tab);
            }
        }


        for (Message m : chat.getMsgs()) {
            if (m instanceof MessagePrivate) {
                MessagePrivate mPrivate = (MessagePrivate) m;
                if (mPrivate.getSender().equals("You")) {
                    privateTab = getTabById(directTabPane, m.whoIsReceiver());
                    privateAnchorPane = (AnchorPane) privateTab.getContent();
                    privateTextArea = (TextArea) privateAnchorPane.getChildren().getFirst();
                    privateTextArea.setText(privateTextArea.getText() + "(" + m.getTime().getHour()  + ":" + m.getTime().getMinute() + ")" + " "+ " you: " + m.getText() + "\n");
                }
                else {
                    privateTab = getTabById(directTabPane, m.getSender());
                    privateAnchorPane = (AnchorPane) privateTab.getContent();
                    privateTextArea = (TextArea) privateAnchorPane.getChildren().getFirst();
                    privateTextArea.setText(privateTextArea.getText() + "(" + m.getTime().getHour()  + ":" + m.getTime().getMinute() + ")" + " " + "" + m.getSender() + ": " + m.getText() + "\n");
                }
            }
            else {
                if(m.getSender().equals("You"))
                    messageAreaBroadcast.setText(messageAreaBroadcast.getText() + "(" + m.getTime().getHour()  + ":" + m.getTime().getMinute()  + ")" + " " + " you: " + m.getText() + "\n");
                else
                    messageAreaBroadcast.setText(messageAreaBroadcast.getText() + "(" + m.getTime().getHour() + ":" + m.getTime().getMinute()  + ")" + " " + m.getSender() + ": " + m.getText() + "\n");
            }
        }


    }

    /**
     * Retrieves a Tab object from a TabPane based on its ID.
     *
     * @param tabPane The TabPane containing the tabs to search.
     * @param id The ID of the tab to retrieve.
     * @return The Tab object with the specified ID, or {@code null} if no such tab is found.
     */
    private Tab getTabById(TabPane tabPane, String id) {
       // System.out.println("id: " + id);
        for (Tab tab : tabPane.getTabs()) {
            if (id.equals(tab.getId())) {
                //System.out.printf("tab returning\n");
                return tab;
            }
        }
        return null; // Return null if no tab with the specified ID is found
    }

    /**
     * Sends a message when the Enter key is pressed in the input box.
     * Determines the type of message (broadcast or private) based on the selected tab.
     *
     * @param keyEvent The KeyEvent triggered by pressing a key (Enter key in this case).
     */
    public void sendMessage(KeyEvent keyEvent) {
        Tab tab = broadcastTabPane.getSelectionModel().getSelectedItem();
        if (keyEvent.getCode() == KeyCode.ENTER) {
            String temp =  "you: " + inputBox.getText(); // message to send
            String messageToSend = inputBox.getText();
            if(tab.equals(chatBroadcast)) {
                Message m = new Message(messageToSend, myUsername);
                client.sendMessage(new messageBroadcast("messageToAll", m));
            }
            else if(tab.equals(chatDirect)){
                Tab tab2 = directTabPane.getSelectionModel().getSelectedItem();
                MessagePrivate m = new MessagePrivate(messageToSend, myUsername, tab2.getId());
                client.sendMessage(new messagePrivate("messageToUser", m));
            }

            inputBox.setText(""); // remove text from input box
            messageJustSent=true;

        }
    }

    /**
     * Closes the chat pop-up window and performs necessary clean-up actions.
     * Sets the chatOpen flag to false, resets the chat icon, and closes the
     * JavaFX stage associated with the pop-up window.
     *
     * @param actionEvent The action event that triggers the closure of the pop-up window.
     */
    public void closePopUp(ActionEvent actionEvent) {
        chatOpen = false;
        imageChat.setImage(new Image("icons/iconChat.png"));
        messageJustSent = false;
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

    /**
     * Updates the chat interface with a new message.
     * Sets the unread message count to zero in the client's model view.
     * Depending on whether the message is private or broadcast, updates the
     * corresponding text areas with the new message content.
     *
     * @param message The message to be displayed in the chat interface.
     */
    public void updateChat(Message message) {
        client.getModelView().setMyUnreadedMessages(0);
        if (message instanceof MessagePrivate) {
            MessagePrivate mPrivate = (MessagePrivate) message;
            //todo ask perchè you e non myUsername
            if (mPrivate.getSender().equals("You")) {
                privateTab = getTabById(directTabPane, mPrivate.whoIsReceiver());
                privateAnchorPane = (AnchorPane) privateTab.getContent();
                privateTextArea = (TextArea) privateAnchorPane.getChildren().getFirst();
                privateTextArea.setText(privateTextArea.getText() + "(" + message.getTime().getHour() + ":" + message.getTime().getMinute() + ")" + " " + " you: " + message.getText() + "\n");
            }
            else {
                privateTab = getTabById(directTabPane, message.getSender());
                privateAnchorPane = (AnchorPane) privateTab.getContent();
                privateTextArea = (TextArea) privateAnchorPane.getChildren().getFirst();
                privateTextArea.setText(privateTextArea.getText() + "(" + message.getTime().getHour() + ":" + message.getTime().getMinute() + ")" + " " + message.getSender() + ": " + message.getText() + "\n");
            }
        }
        else {
            if(message.getSender().equals("You"))
                messageAreaBroadcast.setText(messageAreaBroadcast.getText() + "(" + message.getTime().getHour() + ":" + message.getTime().getMinute() + ")" + " " + " you: " + message.getText() + "\n");
            else
                messageAreaBroadcast.setText(messageAreaBroadcast.getText() + "(" + message.getTime().getHour() + ":" + message.getTime().getMinute() + ")" + " " + message.getSender() + ": " + message.getText() + "\n");
        }

    }

    public void setImageChat(ImageView imageChat) {
        this.imageChat = imageChat;
    }
}

