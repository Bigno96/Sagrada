package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
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
import java.util.SortedMap;

public class GuiSystem extends Thread implements ViewInterface{

    private static final String TITLE = "TITLE_GAME";
    private static final String YOUR_TURN_KEY = "YOUR_TURN";

    private HashMap<String, ServerSpeaker> connParam;
    private Stage primaryStage;
    private ViewMessageParser dictionary;

    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;

    private ControlInterface ctrl;

    /**
     * Constructor of GuiSystem
     * @param primaryStage from ClientMain
     */
    public GuiSystem(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.connParam = new HashMap<>();
        dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
    }

    /**
     * Load WindowCardPage
     * Set List of Cards
     * Return to server the card
     * @param cards cards.size() = 4
     */
    @Override
    public void chooseWindowCard(List<WindowCard> cards) {

        System.out.println("choose");

        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WindowCardsPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setTitle(TITLE);

            assert root != null;
            primaryStage.setScene(new Scene(root));

            ctrl = loader.getController();
            ctrl.setGuiSystem(this);

            ctrl.setList(cards);

            primaryStage.show();
        });

    }

    @Override
    public void showCardPlayer(String user, WindowCard card) {

    }

    @Override
    public void printWindowCard(WindowCard window) {

    }

    @Override
    public void printPrivateObj(ObjectiveCard privObj) {

    }

    @Override
    public void printPublicObj(List<ObjectiveCard> publObj) {

    }

    @Override
    public void showDraft(List<Dice> draft) {

    }

    /**
     * @param s to be printed
     */
    @Override
    public void print(String s) {

        ctrl.print(s);

    }

    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {

    }

    @Override
    public void wrongPlacementDice(String errorMsg) {

    }

    @Override
    public void printRanking(SortedMap<Integer, String> ranking) {

    }

    /**
     * @param username = game.getCurrentPlayer().getId()
     */
    @Override
    public void isTurn(String username) {

        ctrl.print(dictionary.getMessage(YOUR_TURN_KEY) + username);

    }

    /**
     *
     */
    @Override
    public void startGraphic() {
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();

        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/LoginPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setTitle(dictionary.getMessage(TITLE));

            assert root != null;
            primaryStage.setScene(new Scene(root));

            ctrl = loader.getController();
            ctrl.setGuiSystem(this);

            primaryStage.show();
        });

    }

    public void setUsername(String userName){
        this.userName = userName;
    }

    void setServerSpeaker(ServerSpeaker serverSpeaker){
        this.serverSpeaker = serverSpeaker;
    }

    void waitingPage(){

        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WaitingPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setTitle(dictionary.getMessage(TITLE));

            assert root != null;
            primaryStage.setScene(new Scene(root));

            ctrl = loader.getController();
            ctrl.setGuiSystem(this);

            primaryStage.show();
        });

    }

    void inizializeBoard() {
        /*Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Board.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            primaryStage.setTitle(dictionary.getMessage(TITLE));

            assert root != null;
            primaryStage.setScene(new Scene(root));

             //ImageView img = new ImageView(this);
             //img.setImageResource(R.drawable.my_image);

            ctrlBoardController = loader.getController();
            ctrlBoardController.setGuiSystem(this);

            ctrlBoardController.setMyWindowCard(myCard);
            ctrlBoardController.setCardOtherPlayerList(listOtherWindowCards);
            ctrlBoardController.setPrivCard(privList);
            ctrlBoardController.setPublCard(publList);
            ctrlBoardController.setToolCard(listToolCards);

            ctrlBoardController.init();

            primaryStage.show();
        }); */
    }

}