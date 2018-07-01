package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;

public class LoginPageController {

    @FXML
    public TextField usernameText;
    @FXML
    public TextField ipText;
    @FXML
    public RadioButton socket;
    @FXML
    public RadioButton rmi;
    @FXML
    public Button submit;

    private String username;
    private GuiSystem guiSystem;

    private Stage loginWindow;

    private ServerSpeaker serverSpeaker;
    private final HashMap<String, ServerSpeaker> connParam;

    public LoginPageController(){
        boolean socketConnection = false;
        boolean rmiConnection = false;
        this.connParam = new HashMap<>();
    }

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
    }

    private boolean validIP(String ip) {
        if (ip.isEmpty())
            return false;

        String[] parts = ip.split("\\.");

        if (parts.length != 4 && parts.length != 6)
            return false;

        for (String s : parts) {
            int i = Integer.parseInt(s);
            if (i < 0 || i > 255)
                return false;
        }

        return !ip.endsWith(".");
    }

    /**
     * If connection has been successful change Stage
     * LoginPage -> WaitingPage
     */
    public void submitAction() {
        startConnection(guiSystem);

        guiSystem.setUsername(usernameText.getText());
        guiSystem.setServerSpeaker(serverSpeaker);

        guiSystem.waitingPage();
    }

    /**
     * Choice of connection, constructor serverSpeaker
     * @param guiSystem from GuiSystem
     * @return HashMap = username + serverSpeaker
     */
    HashMap<String, ServerSpeaker> startConnection(GuiSystem guiSystem) {

        username = usernameText.getText();
        if (socket.isSelected()) {
            //if rmi RadioButton is selected return HashMap(username, ServerSpeaker)
            serverSpeaker = new SocketServerSpeaker(ipText.getText(), guiSystem);
        } else {
            serverSpeaker = new RmiServerSpeaker(ipText.getText(), usernameText.getText(), guiSystem);
        }

        if(validIP(ipText.getText())){

            if (!serverSpeaker.connect(username)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Connection Server");
                alert.setHeaderText("Il server non risponde");
                alert.setContentText("Cambiare IP");

                alert.showAndWait();

                ipText.setText("");
            }

            if (!serverSpeaker.login(username)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error Username");
                alert.setHeaderText("Username già in uso");
                alert.setContentText("Cambiare Username");

                alert.showAndWait();

                usernameText.setText("");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Invalid IP");
            alert.setHeaderText("IP errato");
            alert.setContentText("Nuovo IP");

            alert.showAndWait();

            ipText.setText("");
        }

        connParam.put(username, serverSpeaker);

        guiSystem.waitingPage();

        return connParam;

    }

}