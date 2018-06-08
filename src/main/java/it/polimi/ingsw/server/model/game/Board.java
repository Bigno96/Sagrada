package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

public class Board extends Observable {

    private List<ObjectiveCard> publObj;
    private List<ToolCard> toolCard;
    private DiceBag diceBag;
    private Draft draft;
    private RoundTrack roundTrack;
    private int nPlayer;

    private static final Logger logger = Logger.getLogger(Player.class.getName());

    /**
     * Constructor
     * @param nPlayer!= null, number of player of the game
     * @throws IDNotFoundException when DiceBag throws IDNotFoundException
     */
    public Board(int nPlayer) throws IDNotFoundException {
        publObj = new ArrayList<>();
        this.nPlayer = nPlayer;
        toolCard = new ArrayList<>();
        diceBag = new DiceBag();
        draft = new Draft(diceBag, (nPlayer*2)+1);
        roundTrack = new RoundTrack(draft);
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("PublObj: " + getPublObj() + " ToolCard: " + getToolCard() +
                " DiceBag: " + getDiceBag() + " Draft: " + getDraft() + " RoundTrack: "
                + getRoundTrack() + " nPlayer: " + getnPlayer());
    }

    /**
     * Set 3 Public Objective Card on Board
     * @param obj1 != null && obj1 != obj2 && obj1 != obj3 && obj1 > 0 && obj < 11
     * @param obj2 != null && obj2 != obj1 && obj2 != obj3 && obj2 > 0 && obj < 11
     * @param obj3 != null && obj3 != obj1 && obj3 != obj2 && obj3 > 0 && obj < 11
     */
    public void setPublObj(ObjectiveCard obj1, ObjectiveCard obj2, ObjectiveCard obj3) {
        this.publObj.add(obj1);
        this.publObj.add(obj2);
        this.publObj.add(obj3);

        setChanged();
        notifyObservers("PublicObjective");
    }

    /**
     * Set 3 ToolCard on Board
     * @param obj1 != null && obj1 != obj2 && obj1 != obj3 && obj1 > 0 && obj < 13
     * @param obj2 != null && obj2 != obj1 && obj2 != obj3 && obj2 > 0 && obj < 13
     * @param obj3 != null && obj3 != obj1 && obj3 != obj2 && obj3 > 0 && obj < 13
     */
    public void setToolCard(ToolCard obj1, ToolCard obj2, ToolCard obj3) {
        this.toolCard.add(obj1);
        this.toolCard.add(obj2);
        this.toolCard.add(obj3);
    }

    public List<ObjectiveCard> getPublObj() {
        return publObj;
    }

    public List<ToolCard> getToolCard() {
        return toolCard;
    }

    public DiceBag getDiceBag() {
        return diceBag;
    }

    public Draft getDraft() {
        return draft;
    }

    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    public int getnPlayer() {
        return nPlayer;
    }
}