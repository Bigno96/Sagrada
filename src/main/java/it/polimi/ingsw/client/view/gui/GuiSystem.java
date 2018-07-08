package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
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
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class GuiSystem implements ViewInterface{

    private static final String TITLE = "TITLE_GAME";
    private static final String YOUR_TURN_KEY = "YOUR_TURN";
    private static final String YOU_USED_TOOL = "Hai correttamente usato la carta strumento: ";
    private static final String OTHER_USED_TOOL = " ha correttamente usato la carta strumento: ";
    ObjectiveCard privObj;
    private Stage primaryStage;
    private ViewMessageParser dictionary;

    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private WindowCard myWindowCard;
    private List<String> otherUsername;
    private List<WindowCard> windowCards;
    private List<ToolCard> toolCards;
    private RoundTrack roundTrack;

    private ControlInterface ctrl;
    private List<ObjectiveCard> publicCards;

    private SortedMap<Integer, String> ranking;
    private Map<String, ServerSpeaker> connParam;

    /**
     * Constructor of GuiSystem
     * @param primaryStage from ClientMain
     */
    public GuiSystem(Stage primaryStage){
        this.connParam = new HashMap<>();
        this.primaryStage = primaryStage;
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        otherUsername = new ArrayList<>();
        windowCards = new ArrayList<>();
        publicCards = new ArrayList<>();

    }

    /**
     * @return SortedMap<Integer, String> ranking
     */
    public SortedMap<Integer, String> getRanking() {
        return ranking;
    }

    /**
     * @return RoundTrack round track
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
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
                out.println(e.getMessage());
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

       this.privObj = privObj;

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

        ctrl.successfulPlacementDice(username, dest, moved);

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

        this.ranking = ranking;

        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/RankingPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                out.println(e.getMessage());
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

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
        if(username.equals(userName)){
            ctrl.print(dictionary.getMessage(YOUR_TURN_KEY) + username);
            ctrl.isMyTurn(true);
        }else{
            ctrl.print("Sta giocando " + username);
            ctrl.isMyTurn(false);
        }

    }

    @Override
    public void startGraphic() {
        Platform.runLater(() -> {
            Parent root = null;
            FXMLLoader loader  = new FXMLLoader(getClass().getClassLoader().getResource("fxml/LoginPage.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                out.println(e.getMessage());
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

    private void closeProgram(){

        primaryStage.close();

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
                out.println(e.getMessage());
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
                out.println(e.getMessage());
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

    String getUserName() {
        return userName;
    }

    ServerSpeaker getServerSpeaker() {
        return serverSpeaker;
    }

    WindowCard getMyWindowCard() {
        return myWindowCard;
    }

    List<WindowCard> getWindowCards(){

        return windowCards;

    }

    List<ObjectiveCard> getPublicCards(){

        return publicCards;

    }

    public void setWindowCard(WindowCard windowCard) {

        myWindowCard = windowCard;

    }

    void moveDice(int index, int row, int col){

        serverSpeaker.placementDice(userName, index, row, col);

    }

    void askRoundTrack() {

        serverSpeaker.askRoundTrack(userName);

    }

    public void endTurn(){

        serverSpeaker.endTurn(userName);

    }

    void forfait(){

        serverSpeaker.quit(userName);
        primaryStage.close();

    }

    List<String> getOtherUsername(){

        return otherUsername;

    }

    void setConnParam(Map<String,ServerSpeaker> connParam) {
        this.connParam = connParam;
    }

    public List<ToolCard> getToolCards() {
        return toolCards;
    }
}
