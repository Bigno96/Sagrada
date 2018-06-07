package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.System.in;
import static java.lang.System.out;

public class LoginPageController {

    @FXML
    private TextField username;
    @FXML
    private TextField ip;
    @FXML
    private RadioButton socket;
    @FXML
    private RadioButton rmi;
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

    public void Submit() {
        startConnection(guiSystem);
        //call change scene

    }

    HashMap<String, ServerSpeaker> startConnection(GuiSystem guiSystem) {

        String userName = username.getText();
        String ipText = ip.getText();
        if (rmi.isSelected()) {
            //if rmi RadioButton is selected return HashMap(username, ServerSpeaker)
            serverSpeaker = new SocketServerSpeaker(ip.getText(), guiSystem);
        } else {
            serverSpeaker = new RmiServerSpeaker(ip.getText(), username.getText(), guiSystem);
        }

        while (!serverSpeaker.connect(userName)){
            //Alert ip wrong
            //clean ip
            System.out.println("rename");
        }

        while (!serverSpeaker.login(userName)) {
            //Alert username già connesso
            //clean username
            System.out.println("ip");
        }

        connParam.put(userName, serverSpeaker);

        return connParam;

    }







    /*HashMap<String, ServerSpeaker> display(GuiSystem guiSystem, Stage window){

        this.guiSystem = guiSystem;
        loginWindow = window;

        loginWindow.initModality(Modality.APPLICATION_MODAL);
        loginWindow.setTitle("Choose connection");
        Button button = new Button("Continue");

        //ChoiceBoxes
        ChoiceBox<String> choiceBox = new ChoiceBox<>();

        //getItems
        choiceBox.getItems().add("RMI");
        choiceBox.getItems().add("socket");

        //set Default value
        choiceBox.setValue("RMI");

        button.setOnAction(e -> getChoice(choiceBox));

        //Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20 , 20));
        layout.getChildren().addAll(choiceBox, button, userName, ip);

        Label label = new Label();
        label.setText("Set your data");
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 400);
        window.setScene(scene);
        window.showAndWait();

        return connParam;

    }*/
/*
    //Set Connection
    private void getChoice(ChoiceBox<String> choiceBox){
        do {
            if (choiceBox.getValue().equals("RMI")) {
                socketConnection = false;
                serverSpeaker = new SocketServerSpeaker(ip.getText(), guiSystem);
            } else {
                socketConnection = true;
                serverSpeaker = new RmiServerSpeaker(ip.getText(), username.getText(), guiSystem);
            }

            while (!validIP(ip.getText())) {               //If IP is incorrect open IncorrectIPWindow and ConnectionWinodow
                Platform.runLater(() -> {
                    LoginPageController connectionWindows = new LoginPageController();
                    Stage window = new Stage();
                    try {
                        connectionWindows.display(guiSystem, window);
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }

                });
                Platform.runLater(() -> {
                    IncorrectIP incorrectIPWindow = new IncorrectIP();
                    Stage window = new Stage();
                    try {
                        incorrectIPWindow.display(window);
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }

                });
            }
            //If IP is correct try to connect

            serverSpeaker.setIp(ip.getText());
            serverSpeaker.connect(username.getText());
            connect = serverSpeaker.login(username.getText());

            connParam.put(username.getText(), serverSpeaker);

        }while (!connect);
            closeWindow();
    }

        private void closeWindow(){
        loginWindow.close();
    }*/


/*
    public void Submit() {
        if (rmi.isSelected()) {
            //if rmi RadioButton is selected return HashMap(username, ServerSpeaker)
            guiSystem.setRMIConnection();
            guiSystem.setServerSpeaker(new SocketServerSpeaker(ip.getText(), guiSystem));
        } else {
            guiSystem.setSocketConnection();
            guiSystem.setServerSpeaker(new RmiServerSpeaker(ip.getText(), username.getText(), guiSystem));
        }
        boolean connect;
        do{
        while (!validIP(ip.getText())) {               //If IP is incorrect open IncorrectIPWindow and ConnectionWinodow
            Platform.runLater(() -> {
                IncorrectIP incorrectIPWindow = new IncorrectIP();
                Stage window = new Stage();
                try {
                    incorrectIPWindow.display(window);
                } catch (Exception e) {
                    out.println(e.getMessage());
                }

            });
        }
        guiSystem.getServerSpeaker().setIp(ip.getText());
        guiSystem.getServerSpeaker().connect(username.getText());
        connect = guiSystem.getServerSpeaker().login(username.getText());
    }while(!connect);
       System.out.println("Ok");

    }
*/
}