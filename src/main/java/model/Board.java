package model;

import java.util.logging.Logger;

public class Board {

    //    private List<PublicObjective> publObj;
//    private List<ToolCard> toolCard;
    private DiceBag diceBag;
    private RoundTrack roundTrack;

    private static final Logger logger = Logger.getLogger(Player.class.getName());


    public Board() {
        //publObj = new ArrayList PublicObjective;
        //toolCard = new ArrayList<ToolCard>;
        //diceBag = new DiceBag();
        roundTrack = new RoundTrack();
    }

    //givePublicObj

    /*public void setPublicObj(PublicObjective publObj) {
        this.publObj = publObj;
    }*/

    /*public PublicObjective getPublObj() {
        return publObj;
    }*/

     /*public void setToolCard(ToolCard toolCard, ToolCard toolCard, ToolCard toolCard) {
        this.toolCard = toolCard(0);
        this.toolCard = toolCard(1);
        this.toolCard = toolCard(2);
    }*/

    /*public PublicObjective getToolCard(int n) {
        return toolCard(n);
    }*/

    //getDiceBag?
}