package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.util.List;

public class RankingController implements ControlInterface {

    @FXML
    public Text firstPlayer;
    @FXML
    public Text secondPlayer;
    @FXML
    public Text thirdPlayer;
    @FXML
    public Text fourthPlayer;
    @FXML
    public Text pointFirst;
    @FXML
    public Text pointSecond;
    @FXML
    public Text pointThird;
    @FXML
    public Text pointFourth;

    GuiSystem guiSystem;

    @Override
    public void print(String message) {

    }

    @Override
    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;

        int sizeRanking = guiSystem.ranking.size();

        firstPlayer.setText(Integer.toString(guiSystem.ranking.lastKey()));
        pointFirst.setText(guiSystem.ranking.get(guiSystem.ranking.lastKey()));
        guiSystem.ranking.remove(guiSystem.ranking.lastKey(), guiSystem.ranking.lastKey());

        secondPlayer.setText(Integer.toString(guiSystem.ranking.lastKey()));
        pointSecond.setText(guiSystem.ranking.get(guiSystem.ranking.lastKey()));
        guiSystem.ranking.remove(guiSystem.ranking.lastKey(), guiSystem.ranking.lastKey());


        if(sizeRanking == 2 ){

            thirdPlayer.setText(Integer.toString(guiSystem.ranking.lastKey()));
            pointThird.setText(guiSystem.ranking.get(guiSystem.ranking.lastKey()));
            guiSystem.ranking.remove(guiSystem.ranking.lastKey(), guiSystem.ranking.lastKey());

        }

        if(sizeRanking == 3){

            fourthPlayer.setText(Integer.toString(guiSystem.ranking.lastKey()));
            pointFourth.setText(guiSystem.ranking.get(guiSystem.ranking.lastKey()));
            guiSystem.ranking.remove(guiSystem.ranking.lastKey(), guiSystem.ranking.lastKey());

        }

    }

    @Override
    public void setList(List<WindowCard> cards) {

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
}
