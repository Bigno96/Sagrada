package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

import static java.lang.System.out;

public class BoardController implements ControlInterface {

    @FXML
    public ImageView img0;
    @FXML
    public ImageView img1;
    @FXML
    public ImageView img2;
    @FXML
    public ImageView img3;
    @FXML
    public TextArea textArea;

    private GuiSystem guiSystem;

    private WindowCard windowCard;

    @Override
    public void print(String s) {

        this.textArea.appendText("\n"+s);

    }

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
    }

    @Override
    public void setList(List<WindowCard> cards) {

    }

    @Override
    public void newCard() {

        String baseURL = "/img/WindowCard/";
        String exp = ".png";

        Image firstWindow = new Image(baseURL + guiSystem.getMyWindowCard().getName() + exp);
        img0.setImage(firstWindow);

        if(guiSystem.windowCards.size() == 1){

            Image secondWindow = new Image(baseURL + guiSystem.windowCards.get(0).getName() + exp);
            img1.setImage(secondWindow);

        }

        if(guiSystem.windowCards.size() == 2){

            Image thirdWindow = new Image(baseURL + guiSystem.windowCards.get(0).getName() + exp);
            img2.setImage(thirdWindow);

        }

        if(guiSystem.windowCards.size() == 3){

            Image fourthWindow = new Image(baseURL + guiSystem.windowCards.get(0).getName() + exp);
            img3.setImage(fourthWindow);

        }

    }

    @Override
    public void printDraft(List<Dice> Draft) {

    }

    @Override
    public void printPrivateObj(ObjectiveCard privObj) {

    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {

    }

    @Override
    public void printListPublObj(List<ObjectiveCard> publObj) {

    }

    @Override
    public void updateCard(WindowCard window) {

    }

    @Override
    public void updateRoundTrack(RoundTrack roundTrack) {

    }

    public void win0(ActionEvent event) {
    }

    public void win1(ActionEvent event) {
    }

    public void win2(ActionEvent event) {
    }

    public void win3(ActionEvent event) {
    }
}