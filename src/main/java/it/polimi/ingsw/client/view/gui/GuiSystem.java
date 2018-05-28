package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import it.polimi.ingsw.server.model.windowcard.WindowFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class GuiSystem extends Application implements ViewInterface{

    private Stage window;
    private GuiLogin loginWindow;
    private String connection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;

    public GuiSystem() {
        connection = new String();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Sagrada");
        Button button = new Button("Play");

        button.setOnAction(e -> displayLoginPage());

        //Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 100);
        window.setScene(scene);
        window.show();
    }

    private void displayLoginPage() {

        Platform.runLater(() -> {
            GuiAskConnection connectionWindows = new GuiAskConnection();
            Stage window = new Stage();
            try {
                connection = connectionWindows.display(window);
            } catch (Exception e) {
                out.println(e.getMessage());
            }

        });

    }


    @Override
    public void chooseWindowCard(List<WindowCard> cards) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

    }

    @Override
    public void showCardPlayer(String user, WindowCard card) throws IDNotFoundException, FileNotFoundException, PositionException, ValueException {

    }

    @Override
    public void printWindowCard(WindowCard window) throws IDNotFoundException {

    }

    @Override
    public void printUsers(List<String> users) {

    }

    @Override
    public void printPrivObj(PrivateObjective privObj) {

    }

    @Override
    public void printPublObj(List<PublicObjective> publObj) {

    }

    @Override
    public void setRound() {

    }

    @Override
    public void isTurn(String username) {

    }

    @Override
    public void showDraft(List<Dice> draft) throws IDNotFoundException, SameDiceException {

    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) {

    }

    @Override
    public void print(String s) {

    }

    @Override
    public void startGraphic() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

    }

}