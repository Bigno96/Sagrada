package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class BoardController3 implements ControlInterface {

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
    public Pane draft;

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

    String baseURL = "/img/WindowCard/";
    String exp = ".png";
    GuiSystem guiSystem;

    @Override
    public void print(String message) {

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
    public void printDraft(List<Dice> Draft) {

    }

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

        Image imageTool0 = new Image("/img/ToolCard/" + toolCards.get(0).getId() + exp);
        tool0.setImage(imageTool0);
        Image imageTool1 = new Image("/img/ToolCard/" + toolCards.get(1).getId() + exp);
        tool1.setImage(imageTool1);
        Image imageTool2 = new Image("/img/ToolCard/" + toolCards.get(2).getId() + exp);
        tool2.setImage(imageTool2);
/*
        Image imagePubl0 = new Image("/img/Public Objective/" + guiSystem.getPulicCards().get(0).getId() + exp);
        publ2.setImage(imagePubl0);
        Image imagePubl1 = new Image("/img/Public Objective/" + guiSystem.getPulicCards().get(1).getId() + exp);
        publ2.setImage(imagePubl1);
        Image imagePubl2 = new Image("/img/Public Objective/" + guiSystem.getPulicCards().get(2).getId() + exp);
        publ2.setImage(imagePubl2);
*/
    }

    @Override
    public void printListPublObj(List<ObjectiveCard> publObj) {

        Image imagePubl0 = new Image("/img/Public Objective/" + publObj.get(0).getId() + exp);
        publ0.setImage(imagePubl0);
        Image imagePubl1 = new Image("/img/Public Objective/" + publObj.get(1).getId() + exp);
        publ1.setImage(imagePubl1);
        Image imagePubl2 = new Image("/img/Public Objective/" + publObj.get(2).getId() + exp);
        publ2.setImage(imagePubl2);

    }

    @Override
    public void updateCard(WindowCard window) {

    }

    @Override
    public void updateRoundTrack(RoundTrack roundTrack) {

    }
}
