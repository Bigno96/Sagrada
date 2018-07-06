package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.out;

public class BoardController implements ControlInterface {

    @FXML
    public GridPane roundTrack;

    @FXML
    public ImageView myWind;
    @FXML
    public ImageView wind0;
    @FXML
    public ImageView wind1;
    @FXML
    public ImageView wind2;

    @FXML
    public ImageView publ0;
    @FXML
    public ImageView publ1;
    @FXML
    public ImageView publ2;

    @FXML
    public ImageView tool0;
    @FXML
    public ImageView tool1;
    @FXML
    public ImageView tool2;

    @FXML
    public Pane paneDraft;

    @FXML
    public ImageView priv;

    @FXML
    public GridPane myTabel;
    @FXML
    public GridPane tabel0;
    @FXML
    public GridPane tabel1;
    @FXML
    public GridPane tabel2;
    @FXML
    public TextArea textArea;
    @FXML
    public Group groupDraft;

    String baseURL = "/img/WindowCard/";
    String exp = ".png";
    GuiSystem guiSystem;

    double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;

    public void print(String s) {

        this.textArea.appendText("\n" + s);

    }

    @Override
    public void setGuiSystem(GuiSystem guiSystem) {

        this.guiSystem = guiSystem;
        Image myWindowImage = new Image(baseURL + guiSystem.getMyWindowCard().getName() + exp);
        myWind.setImage(myWindowImage);

    }

    @Override
    public void setList(List<WindowCard> cards) {

    }

    @Override
    public void newCard() {

    }

    @Override
    public void printDraft(List<Dice> draft) {

        out.println("draft");
        String diceURL = "/img/Dices/";

        groupDraft.getChildren().clear();

        Platform.runLater(() -> {

            for (int i = 0; draft.size() > i; i++) {

                Image imageDice = new Image(diceURL + draft.get(i).getColor() + "-" + draft.get(i).getValue() + exp);
                Rectangle rectangle = new Rectangle(30, 30);
                rectangle.setFill(new ImagePattern(imageDice));
                if (i % 2 == 1) {
                    rectangle.setX(30);
                }
                rectangle.setY(i / 2 * 30);

                rectangle.setCursor(Cursor.HAND);
                rectangle.setOnMousePressed(circleOnMousePressedEventHandler);
                rectangle.setOnMouseDragged(circleOnMouseDraggedEventHandler);

                groupDraft.getChildren().add(rectangle);

            }

        });
    }

    EventHandler<MouseEvent> circleOnMousePressedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    orgSceneX = t.getSceneX();
                    orgSceneY = t.getSceneY();
                    orgTranslateX = ((Rectangle)(t.getSource())).getTranslateX();
                    orgTranslateY = ((Rectangle)(t.getSource())).getTranslateY();
                }
            };

    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    double offsetX = t.getSceneX() - orgSceneX;
                    double offsetY = t.getSceneY() - orgSceneY;
                    double newTranslateX = orgTranslateX + offsetX;
                    double newTranslateY = orgTranslateY + offsetY;

                    ((Rectangle)(t.getSource())).setTranslateX(newTranslateX);
                    ((Rectangle)(t.getSource())).setTranslateY(newTranslateY);
                }
            };

    @Override
    public void printPrivateObj(ObjectiveCard privObj) {

        Image myPrivCard = new Image("/img/Private Objective/" + privObj.getId() + exp);
        priv.setImage(myPrivCard);

        Image windowPlayer2 = new Image(baseURL + guiSystem.getWindowCards().get(0).getName() + exp);
        wind0.setImage(windowPlayer2);

        if(guiSystem.getWindowCards().size() > 1){

            Image windowPlayer3 = new Image(baseURL + guiSystem.getWindowCards().get(1).getName() + exp);
            wind1.setImage(windowPlayer3);

        }

        if(guiSystem.getWindowCards().size() > 2){

            Image windowPlayer4 = new Image(baseURL + guiSystem.getWindowCards().get(2).getName() + exp);
            wind2.setImage(windowPlayer4);

        }

    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {

        String baseTool = "/img/ToolCard/";

        Image imageTool0 = new Image(baseTool + toolCards.get(0).getId() + exp);
        tool0.setImage(imageTool0);
        Image imageTool1 = new Image(baseTool + toolCards.get(1).getId() + exp);
        tool1.setImage(imageTool1);
        Image imageTool2 = new Image(baseTool + toolCards.get(2).getId() + exp);
        tool2.setImage(imageTool2);

    }

    @Override
    public void printListPublObj(List<ObjectiveCard> publObj) {

        String basePubl = "/img/Public Objective/";

        Image imagePubl0 = new Image(basePubl + publObj.get(0).getId() + exp);
        publ0.setImage(imagePubl0);
        Image imagePubl1 = new Image(basePubl + publObj.get(1).getId() + exp);
        publ1.setImage(imagePubl1);
        Image imagePubl2 = new Image(basePubl + publObj.get(2).getId() + exp);
        publ2.setImage(imagePubl2);

    }

    @Override
    public void updateCard(WindowCard window) {

    }

    @Override
    public void updateRoundTrack(RoundTrack roundTrack) {

    }

}
