package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

public class GuiSystem implements ViewInterface{
/*
    private static final String ALERT_TITLE_ISTURN = "E' il tuo turno";
    private static final String ALERT_HEADER_ISTURN = "E' il tuo turno";
    private static final String ALERT_CONTENT_ISTURN = "Fa la tua mossa";
    private static final String ALERT_SERVER_MESSAGE = "Messaggio dal server";
    private static final String TITLE = "Sagrada";
*/
    private HashMap<String, ServerSpeaker> connParam;
    private Stage primaryStage;
    private WindowCard myCard;
    private List<WindowCard> listOtherWindowCards;
    private List<ToolCard> listToolCards;
    private List<PublicObjective> publList;
    private List<PrivateObjective> privList;
    private ViewMessageParser dictionary;

    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private LoginPageController ctrl;
    private LoginPageController ctrlChooseController;
    private LoginPageController ctrlBoardController;
    private int nRound = 0;

    /**
     * Constructor
     * @param primaryStage from ClientMain
     */
    public GuiSystem(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.connParam = new HashMap<>();
    }

    /**
     * Open WindowCardsPage
     * @param cards cards.size() = 4
     */
    @Override
    public void chooseWindowCard(List<WindowCard> cards) {
        /*Platform.runLater(() -> {
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

            ctrlChooseController = loader.getController();
            ctrlChooseController.setGuiSystem(this);

            //ctrlChooseController.setList(cards);

            primaryStage.show();
        });*/
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

    //@Override
    public void placementDice(String username, Cell dest, Dice moved) {
        
    }

    @Override
    public void print(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Messaggio dal server    ");
        alert.setHeaderText(serverSpeaker.toString());
        alert.setContentText(serverSpeaker.toString());

        alert.showAndWait();
    }

    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {

    }

    @Override
    public void wrongPlacementDice() {


    }

    @Override
    public void printRanking(SortedMap<Integer, String> ranking) {

    }

    @Override
    public void isTurn(String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("E' il tuo turno");
        alert.setHeaderText("E' il tuo turno");
        alert.setContentText("Fa la tua mossa");

        alert.showAndWait();
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
            primaryStage.setTitle("Sagrada");

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
/*
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
        });*/
    }

    public void inizializeBoard() {
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

    public void setWindowCard(WindowCard card){
        myCard = card;
    }

    public int getnRound(){
        return  nRound;
    }
}