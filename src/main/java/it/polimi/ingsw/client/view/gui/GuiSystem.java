package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static java.lang.System.out;

public class GuiSystem implements ViewInterface{

    private static final String TITLE = "TITLE_GAME";
    private static final String YOUR_TURN_KEY = "YOUR_TURN";

    private HashMap<String, ServerSpeaker> connParam;
    private Stage primaryStage;
    private ViewMessageParser dictionary;

    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private WindowCard myWindowCard;
    public List<WindowCard> windowCards;

    private ControlInterface ctrl;

    /**
     * Constructor of GuiSystem
     * @param primaryStage from ClientMain
     */
    public GuiSystem(Stage primaryStage){

        this.primaryStage = primaryStage;
        this.connParam = new HashMap<>();
        dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        windowCards = new ArrayList<WindowCard>();

    }

    /**
     * Load WindowCardPage
     * Set List of Cards
     * Return to server the card
     * @param cards cards.size() = 4
     */
    @Override
    public void chooseWindowCard(List<WindowCard> cards) {

        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/WindowCardsPage.fxml"));
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

            ctrl.setList(cards);

            primaryStage.show();
        });

    }

    /**
     * set Card at the star of game
     * @param user = Player.getId()
     * @param card = Player.getWindowCard().getName()
     */
    @Override
    public void showCardPlayer(String user, WindowCard card) {

        if(userName.equals(user)){

            myWindowCard = card;

        }else{

            windowCards.add(card);

        }

        ctrl.newCard();

    }

    /**
     * @param window window card to be printed
     */
    @Override
    public void printWindowCard(WindowCard window) {

        ctrl.updateCard(window);

    }

    @Override
    public void printPrivateObj(ObjectiveCard privObj) {

        ctrl.printPrivateObj(privObj);

    }

    @Override
    public void printListPublicObj(List<ObjectiveCard> publObj) {

      ctrl.printListPublObj(publObj);

    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {

        ctrl.printListToolCard(toolCards);

    }

    @Override
    public void showDraft(List<Dice> draft) {

        ctrl.printDraft(draft);

    }

    @Override
    public void showRoundTrack(RoundTrack roundTrack) {

        ctrl.updateRoundTrack(roundTrack);

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
    public void printFavorPoints(int point) {

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
        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/BoardPage.fxml"));
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

    String getUserName() {
        return userName;
    }

    ServerSpeaker getServerSpeaker() {
        return serverSpeaker;
    }

    void setConnParam(HashMap<String, ServerSpeaker> connParam) {
        this.connParam = connParam;
    }

    public WindowCard getMyWindowCard() {
        return myWindowCard;
    }

    public List<WindowCard> getWindowCards(){

        return windowCards;

    }
}
