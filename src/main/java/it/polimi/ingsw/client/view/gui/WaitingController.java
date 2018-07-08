package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.List;

public class WaitingController implements ControlInterface{

    private static final String STARTING_GAME = "GAME_WILL_START";

    @FXML
    public TextArea textArea;

    private GuiSystem guiSystem;

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
        ViewMessageParser dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.textArea.setText(dictionary.getMessage(STARTING_GAME));

    }

    @Override
    public void setList(List<WindowCard> cards) {

    }

    @Override
    public void printDraft(List<Dice> draft) {

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
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {

    }

    @Override
    public void isMyTurn(Boolean turnBoolean) {

    }

    public void print(String s) {

        this.textArea.appendText("\n"+s);

    }

}
