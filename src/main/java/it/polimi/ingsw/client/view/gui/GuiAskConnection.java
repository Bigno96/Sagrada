package it.polimi.ingsw.client.view.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import it.polimi.ingsw.client.network.ServerSpeaker;

import java.util.HashMap;
import java.util.Scanner;

import static java.lang.System.out;

public class GuiAskConnection{

    private boolean socketConnection;
    private boolean rmiConnection;
    private boolean connect = false;
    private Stage loginWindow;
    private TextField userName = new TextField();
    private TextField ip = new TextField();
    private ServerSpeaker serverSpeaker;
    private HashMap<String, ServerSpeaker> connParam;

    public GuiAskConnection(){
        socketConnection = false;
        rmiConnection = false;
        connParam = new HashMap<>();
    }

    HashMap<String, ServerSpeaker> display(Stage window){

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
        window.show();

        return connParam;

    }

    //Set Connection
    private void getChoice(ChoiceBox<String> choiceBox){
        do {
            if (choiceBox.getValue().equals("RMI")) {
                socketConnection = false;
            } else {
                socketConnection = true;
            }

            if (!validIP(ip.getText())) {               //If IP is incorrect open IncorrectIPWindow and ConnectionWinodow
                Platform.runLater(() -> {
                    GuiAskConnection connectionWindows = new GuiAskConnection();
                    Stage window = new Stage();
                    try {
                        connectionWindows.display(window);
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
            tryToConnect();


        }while (!connect);
            System.out.println("Ti sei connesso");
            closeWindow();
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

    private void tryToConnect() {


        serverSpeaker.setIp(ip.getText());
        serverSpeaker.connect(userName.getText());
        serverSpeaker.login(userName.getText());

        //connParam.put(userName.getText(), serverSpeaker);

        connect = true;

    }

    private void closeWindow(){
        loginWindow.close();
    }

}