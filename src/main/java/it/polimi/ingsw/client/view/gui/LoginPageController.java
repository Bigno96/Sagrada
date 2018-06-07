package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;

public class LoginPageController {

    @FXML
    private TextField usernameText;
    @FXML
    private TextField ipText;
    @FXML
    private RadioButton socket;
    @FXML
    private RadioButton rmi;
    private String username;
    private String ip;
    private GuiSystem guiSystem;

    private boolean socketConnection;
    private boolean rmiConnection;
    private Stage loginWindow;

    private ServerSpeaker serverSpeaker;
    private final HashMap<String, ServerSpeaker> connParam;

    public LoginPageController(){
        this.socketConnection = false;
        this.rmiConnection = false;
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

    public void submit() {
        startConnection(guiSystem);
        //call change scene

    }

    HashMap<String, ServerSpeaker> startConnection(GuiSystem guiSystem) {

        username = usernameText.getText();
        ip = ipText.getText();
        if (rmi.isSelected()) {
            //if rmi RadioButton is selected return HashMap(username, ServerSpeaker)
            serverSpeaker = new SocketServerSpeaker(ipText.getText(), guiSystem);
        } else {
            serverSpeaker = new RmiServerSpeaker(ipText.getText(), usernameText.getText(), guiSystem);
        }

        while (!serverSpeaker.connect(username)){
            //Alert ip wrong
            //clean ip
            System.out.println("rename");
        }

        while (!serverSpeaker.login(username)) {
            //Alert username già connesso
            //clean username
            System.out.println("ip");
        }

        connParam.put(username, serverSpeaker);

        return connParam;

    }

}