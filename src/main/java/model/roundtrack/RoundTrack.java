package model.roundtrack;

import exception.EmptyException;
import exception.IDNotFoundException;
import exception.SameDiceException;
import model.dicebag.Dice;
import model.dicebag.Draft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class RoundTrack {

    private List<ListDiceRound> trackList = new ArrayList<>();
    private Draft draft;
    private static final Logger logger = Logger.getLogger(RoundTrack.class.getName());

    public RoundTrack(Draft draft){
        this.draft = draft;
        for (int i=0; i<10; i++)
            trackList.add(new ListDiceRound());
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("contains following : ");
        for (ListDiceRound r :trackList )
            r.dump();
    }

    public Dice findDice (int id) throws IDNotFoundException {
        for (ListDiceRound l : trackList) {
            for (Iterator<Dice> itr = l.itr(); itr.hasNext();) {
                Dice d = itr.next();
                if (d.getID() == id)
                    return d.copyDice();
            }
        }

        throw new IDNotFoundException("Id not found");
    }

    public void moveDraft(int round) throws SameDiceException {
        List<Dice> copy = draft.copyDraft();
        draft.freeDraft();
        trackList.get(round).addDice(copy);
    }

    public boolean addDice(Dice d, int round) throws SameDiceException {
        if (round < 0 || round > 9)
            throw new IndexOutOfBoundsException("Round doesn't exists");

        return trackList.get(round).addDice(d);
    }

    public boolean rmDice(Dice d, int round) throws EmptyException, IDNotFoundException {
        if (round < 0 || round > 9)
            throw new IndexOutOfBoundsException("Round doesn't exists");

        return trackList.get(round).rmDice(d);
    }
}