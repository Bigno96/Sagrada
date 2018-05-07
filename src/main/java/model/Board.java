package model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Board {

    private List<PublicObjective> publObj;
    private ObjectiveStrategy objectiveStrategy;
    private ObjectiveFactory objectiveFactory;
    //private List<ToolCard> toolCard;
    private DiceBag diceBag;
    private Draft draft;
    private RoundTrack roundTrack;
    private WindowFactory windowFactory;
    private int nPlayer;

    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Board(int nPlayer) {
        objectiveStrategy = new ObjectiveStrategy();
        objectiveFactory = new ObjectiveFactory(objectiveStrategy);
        publObj = new ArrayList<>();
        this.nPlayer = nPlayer;
        //toolCard = new ArrayList<ToolCard>;
        diceBag = DiceBag.getInstance();
        draft = new Draft(diceBag, (nPlayer*2)+1);
        roundTrack = new RoundTrack();
        windowFactory = new WindowFactory();
     }

    public void setPublObj(PublicObjective obj1, PublicObjective obj2, PublicObjective obj3) {
        this.publObj.add(obj1);
        this.publObj.add(obj2);
        this.publObj.add(obj3);
    }

    public List<PublicObjective> getPublObj() {
        return publObj;
    }

    public ObjectiveStrategy getObjectiveStrategy() {
        return objectiveStrategy;
    }

    public ObjectiveFactory getObjectiveFactory() {
        return objectiveFactory;
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