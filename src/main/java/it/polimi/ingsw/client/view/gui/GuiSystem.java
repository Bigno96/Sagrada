package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static java.lang.System.out;

public class GuiSystem implements ViewInterface{

    private static final String TITLE = "TITLE_GAME";
    private static final String YOUR_TURN_KEY = "YOUR_TURN";
    private static final String YOU_USED_TOOL = "Hai correttamente usato la carta strumento: ";
    private static final String OTHER_USED_TOOL = " ha correttamente usato la carta strumento: ";

    private HashMap<String, ServerSpeaker> connParam;
    private Stage primaryStage;
    private ViewMessageParser dictionary;

    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private WindowCard myWindowCard;
    public List<String> otherUsername;
    public List<WindowCard> windowCards;
    public List<ToolCard> toolCards;
    public RoundTrack roundTrack;
    private BackgroundImage backgroundImage;

    private ControlInterface ctrl;
    private List<ObjectiveCard> publicCards;

    /**
     * Constructor of GuiSystem
     * @param primaryStage from ClientMain
     */
    public GuiSystem(Stage primaryStage){

        this.primaryStage = primaryStage;
        this.connParam = new HashMap<>();
        dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        windowCards = new ArrayList<WindowCard>();
        publicCards = new ArrayList<ObjectiveCard>();

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
            primaryStage.setOnCloseRequest(e -> closeProgram());

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

        if(!userName.equals(user)){

            windowCards.add(card);
            otherUsername.add(user);

        }

    }

    /**
     * @param window window card to be printed
     */
    @Override
    public void printWindowCard(WindowCard window) {

        ctrl.updateCard(windowCards, window);

    }

    @Override
    public void printPrivateObj(ObjectiveCard privObj) {

        ctrl.printPrivateObj(privObj);

    }

    @Override
    public void printListPublicObj(List<ObjectiveCard> publObj) {


        publicCards = publObj;
        ctrl.printListPublObj(publObj);

    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {

        this.toolCards = toolCards;
        ctrl.printListToolCard(toolCards);
        ctrl.printListPublObj(publicCards);

    }

    @Override
    public void showDraft(List<Dice> draft) {

        ctrl.printDraft(draft);

    }

    @Override
    public void showRoundTrack(RoundTrack roundTrack) {

        this.roundTrack = roundTrack;

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

        ctrl.succefulPlacementDice(username, dest, moved);

    }

    @Override
    public void wrongPlacementDice(String errorMsg) {

        ctrl.print(errorMsg);

    }

    @Override
    public void printFavorPoints(int point) {

        ctrl.favorPoints(point);

    }

    @Override
    public void printRanking(SortedMap<Integer, String> ranking) {



    }

    @Override
    public void successfulUsedTool(String username, ToolCard card) {

        if(username.equals(userName)){
            ctrl.print(YOU_USED_TOOL+ card.getId());
        }else{
            ctrl.print("Il giocatore" + username + OTHER_USED_TOOL + card.getId());
        }

    }

    /**
     * @param username = game.getCurrentPlayer().getId()
     */
    @Override
    public void isTurn(String username) {

        if(username.equals(userName)){
            ctrl.print(dictionary.getMessage(YOUR_TURN_KEY) + username);
        }else{
            ctrl.print("Sta giocando " + username);
        }

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
            primaryStage.setOnCloseRequest(e -> closeProgram());

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
            primaryStage.setOnCloseRequest(e -> closeProgram());

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
            root.setId("rootID");
            primaryStage.setScene(new Scene(root));

            //root.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
            primaryStage.setOnCloseRequest(e -> closeProgram());

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

    public List<PublicObjective> getPulicCards(){

        return getPulicCards();

    }

    public void setWindowCard(WindowCard windowCard) {

        myWindowCard = windowCard;

    }

    public void moveDice(int index, int row, int col){

        serverSpeaker.placementDice(userName, index, row, col);

    }

    public void askRoundTrack() {

        serverSpeaker.askRoundTrack(userName);

    }

    public void endTurn(){

        serverSpeaker.endTurn(userName);

    }

    private void closeProgram(){

        primaryStage.close();

    }

    public List<String> getOtherUsername(){

        return otherUsername;

    }
}
