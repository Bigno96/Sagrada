package it.polimi.ingsw.server.model.roundtrack;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.RoundNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.dicebag.Dice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.logging.Logger;

public class RoundTrack extends Observable implements Serializable {

    private static final String DUMP_MSG = "contains following dices: ";
    private static final String ID_NOT_FOUND_MSG = "Id non trovato";
    private static final String ROUND_NOT_EXISTING_MSG = "Il round non esite";
    private static final String DICE_NOT_FOUND_MSG = "Dado non trovato nel tracciato dei round";

    private List<ListDiceRound> trackList = new ArrayList<>();
    private Draft draft;
    private static final Logger logger = Logger.getLogger(RoundTrack.class.getName());

    /**
     * Constructor
     * @param draft of the current Round
     */
    public RoundTrack(Draft draft) {
        this.draft = draft;
        for (int i=0; i<10; i++)
            trackList.add(new ListDiceRound());
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info(DUMP_MSG);
        trackList.forEach(ListDiceRound::dump);
    }

    /**
     * Find dice into RoundTrack, with that ID
     * @param id != null
     * @return a copy of the Dice
     * @throws IDNotFoundException when dice is not found
     */
    public Dice findDice (int id) throws IDNotFoundException {
        for (ListDiceRound l : trackList) {
            for (Iterator<Dice> itr = l.itr(); itr.hasNext();) {
                Dice d = itr.next();
                if (d.getID() == id)
                    return d.copyDice();
            }
        }

        throw new IDNotFoundException(ID_NOT_FOUND_MSG);
    }

    /**
     * Check if exist a dice, into RoundTrack, with this color
     * @param col != null
     * @return true if exist a Dice with that color, else false
     */
    public boolean findColor (Colors col) {
        for (ListDiceRound l : trackList) {
            for (Iterator<Dice> itr = l.itr(); itr.hasNext();) {
                Dice d = itr.next();
                if (d.getColor() == col)
                    return true;
            }
        }

        return false;
    }

    /**
     * Move Dice from draft of round, to RoundTrack
     * @param round != null
     * @throws SameDiceException when trying to add the same Dice twice in draft
     */
    public void moveDraft(int round) throws SameDiceException {
        List<Dice> copy = draft.getDraftList();
        draft.freeDraft();
        trackList.get(round).addDice(copy);

        setChanged();
        notifyObservers();
    }

    /**
     * Used to add Dice to a specified round
     * @param d dice to add
     * @param round where add a Dice
     * @return true if successful, false else
     * @throws SameDiceException when trying to add the same Dice twice
     * @throws RoundNotFoundException when wrong round is requested
     */
    public boolean addDice(Dice d, int round) throws SameDiceException, RoundNotFoundException {
        if (round < 0 || round > 9)
            throw new RoundNotFoundException(ROUND_NOT_EXISTING_MSG);

        return trackList.get(round).addDice(d);
    }

    /**
     * Used to remove Dice from a specified round
     * @param d dice to remove
     * @param round where to remove Dice
     * @return true if successful, false else
     * @throws EmptyException when removing from an empty round
     * @throws IDNotFoundException when dice is not in the specified round
     * @throws RoundNotFoundException when wrong round is requested
     */
    public boolean rmDice(Dice d, int round) throws EmptyException, IDNotFoundException, RoundNotFoundException {
        if (round < 0 || round > 9)
            throw new RoundNotFoundException(ROUND_NOT_EXISTING_MSG);

        return trackList.get(round).rmDice(d);
    }

    /**
     * Used to get number of round where a Dice is contained
     * @param d dice to find
     * @return round.contains(d)
     * @throws IDNotFoundException when dice is not found
     */
    public int getRound(Dice d) throws IDNotFoundException {
        for (ListDiceRound l : trackList) {
            for (Iterator<Dice> itr = l.itr(); itr.hasNext();) {
                Dice dice = itr.next();
                if (d.getID() == dice.getID()) {
                    return trackList.indexOf(l);
                }
            }
        }

        throw new IDNotFoundException(DICE_NOT_FOUND_MSG);
    }

    /**
     * @return all lists of list<dice> for all rounds
     */
    public List<ListDiceRound> getTrackList() {
        return this.trackList;
    }

    /**
     * Set changed and notify observers
     */
    public void setChangedAndNotify() {
        setChanged();
        notifyObservers();
    }
}