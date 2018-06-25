package it.polimi.ingsw.server.model.objectivecard.factory;

import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;

public interface ObjectiveFactory {

    /**
     * Used to create card, only with id, description and point parameters
     * @param id parameter of the card
     * @param description parameter of the card
     * @param point parameter of the card
     * @return ObjectiveCard created
     */
    ObjectiveCard makeCard(int id, String description, int point);

    /**
     * Used to set strategy parameter of a given objective card
     * @param objectiveCard whose strategy parameter need to be set
     * @param type json parameter type
     * @param scope json parameter scope
     * @param grad json parameter gradation
     * @param dir json parameter direction
     */
    default void setParameter(ObjectiveCard objectiveCard, String type, String scope, String grad, String dir) {
        objectiveCard.setParameter(type, scope, grad, dir);
    }
}
