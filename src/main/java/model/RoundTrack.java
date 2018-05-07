package model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RoundTrack {

    private List<ListRound> roundTrack = new ArrayList<ListRound>();
    private static final Logger logger = Logger.getLogger(RoundTrack.class.getName());

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("contains following : ");
        for (ListRound r :roundTrack )
        {
            r.dump();
        }
    }

    public RoundTrack(){
        for (int i=0; i<10; i++){
            roundTrack.add(new ListRound());
        }
    }

    public boolean findDice(Dice d){
        for(int i=0; i<10; i++) {
            for (Dice itr : roundTrack.get(i).getListRound()) {
                if (itr.equals(d)) {
                    return true;
                }
            }
        }
        return false;
    }

    //searchDice: metodo che dato valore e colore dado cerca, in tutte le listRound di roundTrack, e restituisce il dado

    //switchDice: metodo che tramite searchDice trova un dado e lo restituisce con quello dato in ingresso (TOOLCARD)

}