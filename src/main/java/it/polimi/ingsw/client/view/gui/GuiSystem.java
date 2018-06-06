package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
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
import java.util.List;

public class GuiSystem implements ViewInterface{

    private Stage primaryStage;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private Boolean socketConnection = false;
    private Boolean rmiConnection = false;

    public GuiSystem(Stage primaryStage){
        this.primaryStage = primaryStage;
    }

    public void start() {
        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/LoginPage.fxml"));
            LoginPageController ctrl = loader.getController();
            //ctrl.setGuiSystem(this);
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            primaryStage.setTitle("Welcome to Sagrada! Choose your connection");

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
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

    public void setSocketConnection() {
        this.socketConnection = true;
    }

    public void setRMIConnection(){
        this.rmiConnection = true;
    }

    public void setServerSpeaker(SocketServerSpeaker serverSpeaker) {
        this.serverSpeaker = serverSpeaker;
    }

    public void setServerSpeaker(RmiServerSpeaker rmiServerSpeaker) {
        this.serverSpeaker = rmiServerSpeaker;
    }

    public ServerSpeaker getServerSpeaker() {
        return serverSpeaker;
    }

}