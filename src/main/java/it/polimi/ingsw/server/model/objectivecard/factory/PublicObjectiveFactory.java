package it.polimi.ingsw.server.model.objectivecard.factory;

import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;

public class PublicObjectiveFactory implements ObjectiveFactory {

    /**
     * @param id          parameter of the card
     * @param description parameter of the card
     * @param point       parameter of the card
     * @return Objective Card created
     */
    @Override
    public ObjectiveCard makeCard(int id, String description, int point) {
        return new PublicObjective(id, description, point);
    }

}
