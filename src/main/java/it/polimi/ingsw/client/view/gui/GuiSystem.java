package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.out;

public class GuiSystem implements ViewInterface{

    private Stage primaryStage;
    private LoginPageController connectionWindow;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private HashMap<String, ServerSpeaker> connParam;

    public GuiSystem(Stage primaryStage){
        connParam = new HashMap<>();
        connectionWindow = new LoginPageController();
        this.primaryStage = primaryStage;
    }

    public void start() {
        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WaitingPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setTitle("Welcome to Sagrada! Choose your connection");

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        });

        displayAskConnection();
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
                out.println("Exception");
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