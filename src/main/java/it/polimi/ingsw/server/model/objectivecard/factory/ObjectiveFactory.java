package it.polimi.ingsw.server.model.objectivecard.factory;

import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;

public interface ObjectiveFactory {

    ObjectiveCard makeCard(int id, String descr, int point);

    default void setParameter(ObjectiveCard objectiveCard, String type, String scope, String grad, String dir) {
        objectiveCard.setParameter(type, scope, grad, dir);
    }
}
