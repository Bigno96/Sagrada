package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
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

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

public class GuiSystem implements ViewInterface{

    private static final String ALERT_TITLE_ISTURN = "E' il tuo turno";
    private static final String ALERT_HEADER_ISTURN = "E' il tuo turno";
    private static final String ALERT_CONTENT_ISTURN = "Fa la tua mossa";
    private static final String ALERT_SERVER_MESSAGE = "Messaggio dal server";
    private static final String TITLE = "Sagrada";

    private HashMap<String, ServerSpeaker> connParam;
    private Stage primaryStage;
    private ViewMessageParser dictionary;

    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private String userName;
    private LoginPageController ctrl;
    private int nRound = 0;

    public GuiSystem(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.connParam = new HashMap<>();
    }


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
            primaryStage.setTitle(TITLE);

            assert root != null;
            primaryStage.setScene(new Scene(root));

            ctrl = loader.getController();
            ctrl.setGuiSystem(this);

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

    public void placementDice(String username, Cell dest, Dice moved) {
        
    }

    @Override
    public void print(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(dictionary.getMessage(ALERT_SERVER_MESSAGE));
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
    public void isTurn(String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(dictionary.getMessage(ALERT_TITLE_ISTURN));
        alert.setHeaderText(dictionary.getMessage(ALERT_HEADER_ISTURN));
        alert.setContentText(dictionary.getMessage(ALERT_CONTENT_ISTURN));

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
            primaryStage.setTitle(TITLE);

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

    public void inizializeBoard() {
        Platform.runLater(() -> {
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

            ctrl = loader.getController();
            ctrl.setGuiSystem(this);

            primaryStage.show();
        });
    }

    boolean chooseCard() {

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

            HBox hBoxImage = new HBox(8);
            hBoxImage.setAlignment( Pos.CENTER );
            hBoxImage.setStyle( "-fx-border-color: black;" );

            InputStream is = loader.getClassLoader().getResourceAsStream("fxml/PublicCard.fxml");

            Image imageEarthRise = new Image( is );

            ImageView imageView = new ImageView();
            imageView.setImage( imageEarthRise );
            imageView.setCache( true );
            imageView.setOpacity( .9 );
            imageView.setSmooth( true );
            imageView.setPreserveRatio( true );
            imageView.setFitHeight( 200 );
            imageView.setFitWidth( 200 );

            hBoxImage.getChildren().add(  imageView );

            primaryStage.show();
        });

        return true;
    }

    public int getnRound(){
        return  nRound;
    }
}