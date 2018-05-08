package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class RoundTrack {

    private List<ListDiceRound> roundTrack = new ArrayList<>();
    private Draft draft;
    private static final Logger logger = Logger.getLogger(RoundTrack.class.getName());



    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("contains following : ");
        for (ListDiceRound r :roundTrack )
        {
            r.dump();
        }
    }

    public RoundTrack(Draft draft){
        this.draft = draft;
        for (int i=0; i<10; i++){
            roundTrack.add(new ListDiceRound());
        }
    }

    public boolean findDice(Dice d){
        for(int i=0; i<10; i++) {
            for (Iterator<Dice> itr = roundTrack.get(i).itrListRound(); itr.hasNext();) {
                if (itr.next().equals(d)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void moveDraft(int turn) {
        List<Dice> copy = draft.copyDraft();
        draft.freeDraft();
        roundTrack.get(turn).addDice(copy);
    }

    //searchDice: metodo che dato valore e colore dado cerca, in tutte le listRound di roundTrack, e restituisce il dado

    //switchDice: metodo che tramite searchDice trova un dado e lo restituisce con quello dato in ingresso (TOOLCARD)

}