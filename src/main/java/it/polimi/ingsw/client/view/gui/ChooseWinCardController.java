package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class ChooseWinCardController implements ControlInterface {

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
    private List<WindowCard> cards;

    @Override
    public void print(String s) {

        this.textArea.appendText("\n"+s);

    }

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
    }

    @Override
    public void setList(List<WindowCard> cards) {

        this.cards = cards;

        String baseURL = "/img/WindowCard/";
        String exp = ".png";

        Image firstWindow = new Image(baseURL + cards.get(0).getName() + exp);
        img0.setImage(firstWindow);

        Image secondWindow = new Image(baseURL + cards.get(1).getName() + exp);
        img1.setImage(secondWindow);

        Image thirdWindow = new Image(baseURL + cards.get(2).getName() + exp);
        img2.setImage(thirdWindow);

        Image fourthWindow = new Image(baseURL + cards.get(3).getName() + exp);
        img3.setImage(fourthWindow);

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
    public void updateCard(List<WindowCard> windowCards, WindowCard window) {

    }

    @Override
    public void updateRoundTrack(RoundTrack roundTrack) {

    }

    @Override
    public void favorPoints(int point) {

    }

    @Override
    public void setDiceFromDraft(Integer columnIndex, Integer rowIndex) {

    }

    @Override
    public void succefulPlacementDice(String username, Cell dest, Dice moved) {

    }

    @Override
    public void isMyTurn(Boolean turnBoolean) {

    }

    public void win0(ActionEvent event) {

        guiSystem.setWindowCard(cards.get(0));
        guiSystem.inizializeBoard();
        guiSystem.getServerSpeaker().setWindowCard(guiSystem.getUserName(), cards.get(0).getName());

    }

    public void win1(ActionEvent event) {

        guiSystem.setWindowCard(cards.get(1));
        guiSystem.inizializeBoard();
        guiSystem.getServerSpeaker().setWindowCard(guiSystem.getUserName(), cards.get(1).getName());

    }

    public void win2(ActionEvent event) {

        guiSystem.setWindowCard(cards.get(2));
        guiSystem.inizializeBoard();
        guiSystem.getServerSpeaker().setWindowCard(guiSystem.getUserName(), cards.get(2).getName());

    }

    public void win3(ActionEvent event) {

        guiSystem.setWindowCard(cards.get(3));
        guiSystem.inizializeBoard();
        guiSystem.getServerSpeaker().setWindowCard(guiSystem.getUserName(), cards.get(3).getName());

    }
}