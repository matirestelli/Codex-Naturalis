package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorPopUpController extends GUI{
    private Stage stage;
    private Scene scene;
    @FXML
    private Text messageText;
    private double x,y;

    @FXML
    private Button closePopUpButton;

    private Font myFont = Font.font("Poor Richard", 20);



    /**
     * Closes the current pop-up window associated with the given action event.
     *
     * <p>This method retrieves the stage (window) from which the action event originated,
     * typically a pop-up window, and closes it.</p>
     *
     * @param actionEvent the action event triggered by a user interaction
     */
    public void closePopUp(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the mouse pressed event to capture the coordinates relative to the scene.
     *
     * <p>This method is invoked when a mouse press event ({@code MouseEvent}) occurs,
     * capturing the x and y coordinates of the event relative to the scene. These coordinates
     * are typically used for tasks such as dragging or determining the location of user input.</p>
     *
     * @param event the MouseEvent representing the mouse press event
     */
    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    /**
     * Handles the mouse dragged event to move the window based on mouse movement.
     *
     * <p>This method is invoked when a mouse drag event ({@code MouseEvent}) occurs on a draggable node,
     * typically a window or pane. It calculates the new position of the window based on the difference
     * between the current mouse position and the initial mouse press position (stored in {@code x} and {@code y}).
     * The window is then moved accordingly by updating its x and y coordinates.</p>
     *
     * @param event the MouseEvent representing the mouse drag event
     */
    @FXML void dragged(MouseEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    /**
     * Sets and displays a message using a specified font in a text component.
     *
     * <p>This method updates the text content of {@code messageText} with the provided {@code errMessage}.
     * It also sets the font of {@code messageText} to {@code myFont} for consistent display styling.</p>
     *
     * @param errMessage the error message or text to be displayed
     */
    //il messaggio viene personalizzato nel momento della chiamata
    public void showMessage(String errMessage){
        messageText.setText(errMessage);
        messageText.setFont(myFont);
    }


}
