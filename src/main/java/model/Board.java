package model;

import exception.IDNotFoundException;
import model.dicebag.DiceBag;
import model.dicebag.Draft;
import model.objectivecard.ObjectiveCard;
import model.objectivecard.ObjectiveFactory;
import model.objectivecard.ObjectiveStrategy;
import model.objectivecard.PublicObjective;
import model.roundtrack.RoundTrack;
import model.toolcard.ToolCard;
import model.windowcard.WindowFactory;

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

    public void setPublObj(PublicObjective obj1, PublicObjective obj2, PublicObjective obj3) {
        this.publObj.add(obj1);
        this.publObj.add(obj2);
        this.publObj.add(obj3);
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