package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import static java.lang.System.out;

public class GuiSystem extends Application implements ViewInterface{

    private Stage window;
    private GuiAskConnection connectionWindow;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    //private Scene userNameScene;
    private HashMap<String, ServerSpeaker> connParam;

    public GuiSystem(){
        connParam = new HashMap<>();
        connectionWindow = new GuiAskConnection();
        window = new Stage();
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setTitle("Sagrada");
        Button button = new Button("Play");

        button.setOnAction(e -> displayAskConnection());

        //Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(button);
        layout.setAlignment(Pos.CENTER);

        Scene initScene = new Scene(layout, 300, 100);
        window.setScene(initScene);
        window.show();
    }

    private void displayAskConnection() {
        Platform.runLater(() -> {
            Stage loginWindow = new Stage();
            try{
                connParam = connectionWindow.display(this, loginWindow);
                userName = connParam.keySet().iterator().next();
                serverSpeaker = connParam.get(userName);
            }catch (Exception e) {
                out.println(e.getMessage());
                System.out.println("Exception");
            }
        });
    }


    @Override
    public void chooseWindowCard(List<WindowCard> cards) {

    }

    @Override
    public void showCardPlayer(String user, WindowCard card) {

    }

    @Override
    public void printWindowCard(WindowCard window) {

    }

    @Override
    public void printUsers(List<String> users) {

    }

    @Override
    public void printPrivObj(ObjectiveCard privObj) {

    }

    @Override
    public void printPublObj(List<ObjectiveCard> publObj) {

    }

    @Override
    public void setRound() {

    }

    @Override
    public void isTurn(String username) {

    }

    @Override
    public void showDraft(List<Dice> draft) {

    }

    @Override
    public void placementDice(String username, Cell dest, Dice moved) {

    }

    @Override
    public void print(String s) {

    }

    @Override
    public void startGraphic() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}