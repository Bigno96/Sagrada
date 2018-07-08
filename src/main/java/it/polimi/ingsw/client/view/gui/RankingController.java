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
import java.util.SortedMap;

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

    private GuiSystem guiSystem;

    @Override
    public void print(String message) {
        // not needed
    }

    @Override
    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
        SortedMap<Integer, String> ranking = guiSystem.getRanking();

        int sizeRanking = ranking.size();

        firstPlayer.setText(Integer.toString(ranking.lastKey()));
        pointFirst.setText(ranking.get(ranking.lastKey()));
        ranking.remove(ranking.lastKey(), ranking.lastKey());

        secondPlayer.setText(Integer.toString(ranking.lastKey()));
        pointSecond.setText(ranking.get(ranking.lastKey()));
        ranking.remove(ranking.lastKey(), ranking.lastKey());


        if(sizeRanking == 2 ){

            thirdPlayer.setText(Integer.toString(ranking.lastKey()));
            pointThird.setText(ranking.get(ranking.lastKey()));
            ranking.remove(ranking.lastKey(), ranking.lastKey());

        }

        if(sizeRanking == 3){

            fourthPlayer.setText(Integer.toString(ranking.lastKey()));
            pointFourth.setText(ranking.get(ranking.lastKey()));
            ranking.remove(ranking.lastKey(), ranking.lastKey());

        }

    }

    @Override
    public void setList(List<WindowCard> cards) {
        // not needed
    }

    @Override
    public void printDraft(List<Dice> draft) {
        // not needed
    }

    @Override
    public void printPrivateObj(ObjectiveCard privObj) {
        // not needed
    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {
        // not needed
    }

    @Override
    public void printListPublObj(List<ObjectiveCard> publObj) {
        // not needed
    }

    @Override
    public void updateCard(List<WindowCard> windowCards, WindowCard window) {
        // not needed
    }

    @Override
    public void updateRoundTrack(RoundTrack roundTrack) {
        // not needed
    }

    @Override
    public void favorPoints(int point) {
        // not needed
    }

    @Override
    public void setDiceFromDraft(Integer columnIndex, Integer rowIndex) {
        // not needed
    }

    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {
        // not needed
    }

    @Override
    public void isMyTurn(Boolean turnBoolean) {
        // not needed
    }
}
