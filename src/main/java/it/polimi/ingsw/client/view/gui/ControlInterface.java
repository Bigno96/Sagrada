package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.List;

public interface ControlInterface {

    void print(String message);

    void setGuiSystem(GuiSystem guiSystem);

    void setList(List<WindowCard> cards);

    void printDraft(List<Dice> Draft);

    void printPrivateObj(ObjectiveCard privObj);

    void printListToolCard(List<ToolCard> toolCards);

    void printListPublObj(List<ObjectiveCard> publObj);

    void updateCard(List<WindowCard> windowCards, WindowCard window);

    void updateRoundTrack(RoundTrack roundTrack);

    void favorPoints(int point);

    void setDiceFromDraft(Integer columnIndex, Integer rowIndex);

    void succefulPlacementDice(String username, Cell dest, Dice moved);

    void isMyTurn(Boolean turnBoolean);
}