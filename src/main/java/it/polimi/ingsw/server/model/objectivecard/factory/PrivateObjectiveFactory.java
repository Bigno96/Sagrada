package it.polimi.ingsw.server.model.objectivecard.factory;

import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;

public class PrivateObjectiveFactory implements ObjectiveFactory {

    @Override
    public ObjectiveCard makeCard(int id, String descr, int point) {
        return new PrivateObjective(id, descr);
    }

}
