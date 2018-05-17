package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.game.Player;
import it.polimi.ingsw.server.model.objectivecard.PublicObjective;
import it.polimi.ingsw.server.model.dicebag.DiceBag;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveFactory;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveStrategy;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.WindowFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Board {

    private List<ObjectiveCard> publObj;
    private ObjectiveStrategy objectiveStrategy;
    private ObjectiveFactory objectiveFactory;
    private List<ToolCard> toolCard;
    private DiceBag diceBag;
    private Draft draft;
    private RoundTrack roundTrack;
    private WindowFactory windowFactory;
    private int nPlayer;

    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Board(int nPlayer) throws IDNotFoundException {
        objectiveStrategy = new ObjectiveStrategy();
        objectiveFactory = new ObjectiveFactory(objectiveStrategy);
        publObj = new ArrayList<>();
        this.nPlayer = nPlayer;
        toolCard = new ArrayList<>();
        diceBag = new DiceBag();
        draft = new Draft(diceBag, (nPlayer*2)+1);
        roundTrack = new RoundTrack(draft);
        windowFactory = new WindowFactory();
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("PublObj: " + getPublObj() + " ObjStrat: " + getObjectiveStrategy() +
                " ObjFact: " + getObjectiveFactory() + " ToolCard: " + getToolCard() +
                " DiceBag: " + getDiceBag() + " Draft: " + getDraft() + " RoundTrack: "
                + getRoundTrack() + " WindowFact: " + getWindowFactory() + " nPlayer: " + getnPlayer());
    }

    public void setPublObj(ObjectiveCard obj1, ObjectiveCard obj2, ObjectiveCard obj3) {
        this.publObj.add(obj1);
        this.publObj.add(obj2);
        this.publObj.add(obj3);
    }

    public void setToolCard(ToolCard obj1, ToolCard obj2, ToolCard obj3) {
        this.toolCard.add(obj1);
        this.toolCard.add(obj2);
        this.toolCard.add(obj3);
    }

    public List<ObjectiveCard> getPublObj() {
        return publObj;
    }

    public ObjectiveStrategy getObjectiveStrategy() {
        return objectiveStrategy;
    }

    public ObjectiveFactory getObjectiveFactory() {
        return objectiveFactory;
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

    public WindowFactory getWindowFactory() {
        return windowFactory;
    }

    public int getnPlayer() {
        return nPlayer;
    }
}