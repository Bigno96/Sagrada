package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

public class Board extends Observable implements Serializable {

    private static final String DUMP_PUBLIC_MSG = "PublicObj: ";
    private static final String DUMP_TOOL_MSG = " ToolCard: ";
    private static final String DUMP_DICE_BAG_MSG = " DiceBag: ";
    private static final String DUMP_DRAFT_MSG = " Draft: ";
    private static final String DUMP_ROUND_TRACK_MSG = " RoundTrack: ";
    private static final String DUMP_N_PLAYER_MSG = " nPlayer: ";

    private static final String PUBLIC_OBJECTIVE_OBSERVER_MSG = "PublicObjective";
    private static final String TOOL_CARD_OBSERVER_MSG = "ToolCard";

    private List<ObjectiveCard> publicObj;
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
        this.publicObj = new ArrayList<>();
        this.nPlayer = nPlayer;
        this.toolCard = new ArrayList<>();
        this.diceBag = new DiceBag();
        this.draft = new Draft(diceBag, (nPlayer*2)+1);
        this.roundTrack = new RoundTrack(draft);
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info(DUMP_PUBLIC_MSG + getPublicObj() + DUMP_TOOL_MSG + getToolCard() +
                DUMP_DICE_BAG_MSG + getDiceBag() + DUMP_DRAFT_MSG + getDraft() + DUMP_ROUND_TRACK_MSG
                + getRoundTrack() + DUMP_N_PLAYER_MSG + getNumberPlayer());
    }

    /**
     * Set 3 Public Objective Card on Board
     * @param obj1 != null && obj1 != obj2 && obj1 != obj3 && obj1 > 0 && obj < 11
     * @param obj2 != null && obj2 != obj1 && obj2 != obj3 && obj2 > 0 && obj < 11
     * @param obj3 != null && obj3 != obj1 && obj3 != obj2 && obj3 > 0 && obj < 11
     */
    public void setPublicObj(ObjectiveCard obj1, ObjectiveCard obj2, ObjectiveCard obj3) {
        this.publicObj.add(obj1);
        this.publicObj.add(obj2);
        this.publicObj.add(obj3);

        setChanged();
        notifyObservers(PUBLIC_OBJECTIVE_OBSERVER_MSG);
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

        setChanged();
        notifyObservers(TOOL_CARD_OBSERVER_MSG);
    }

    /**
     * @return copy of list of public objective
     */
    public List<ObjectiveCard> getPublicObj() {
        List<ObjectiveCard> ret = new ArrayList<>();

        publicObj.forEach(obj -> ret.add(obj.copy()));

        return ret;
    }

    /**
     * @return copy of list of tool card
     */
    public List<ToolCard> getToolCard() {
        List<ToolCard> ret = new ArrayList<>();

        toolCard.forEach(card -> ret.add(card.copy()));

        return ret;
    }

    /**
     * @return dice bag of the current game
     */
    public DiceBag getDiceBag() {
        return diceBag;
    }

    /**
     * @return draft of the current game
     */
    public Draft getDraft() {
        return draft;
    }

    /**
     * @return round track of the current game
     */
    public RoundTrack getRoundTrack() {
        return roundTrack;
    }

    /**
     * @return number of player in the current game
     */
    public int getNumberPlayer() {
        return nPlayer;
    }
}