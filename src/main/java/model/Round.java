package model;

import exception.EmptyException;
import exception.PlayerNotFoundException;
import exception.SamePlayerException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


public class Round {

    private List<Player> playerList;
    private static final Logger logger = Logger.getLogger(Player.class.getName());
    private boolean firstTurn;

    public Round() {
        playerList = new ArrayList<>();
        firstTurn = true;
    }


    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info("contains following players: ");
        for (Player p : playerList)
            p.dump();
    }

    public boolean addPlayer(Player p) throws SamePlayerException {
        if (playerList.contains(p))
            throw new SamePlayerException("Player already in game");
        return playerList.add(p);
    }

    public boolean rmPlayer(Player p) throws EmptyException {
        if (playerList.isEmpty())
            throw new EmptyException("Round List is empty");
        else
            return playerList.remove(p);
    }

    public Player findPlayer(Player p) throws PlayerNotFoundException, EmptyException {
        if (playerList.isEmpty())
            throw new EmptyException("No player in game");
        if (playerList.contains(p))
            return p;
        else
            throw new PlayerNotFoundException("Player not found");
    }

    public Player nextPlayer() {
        if (firstTurn) {
            for (Player p : playerList) {
                if (p.isFirstTurn()) {
                    p.endFirstTurn();
                    return p;
                }
            }
            for (Player p : playerList) {
                p.resetPlayedDice();
                p.resetUsedTool();
            }

            firstTurn = false;
        }

        for (ListIterator<Player> itr = playerList.listIterator(playerList.size()); itr.hasPrevious(); ) {
            Player p = itr.previous();
            if (p.isSecondTurn()) {
                p.endSecondTurn();
                return p;
            }
        }

        return null;
    }

    public void nextRound() {
        List<Player> tmp = new ArrayList<>();
        for(ListIterator<Player> itr = playerList.listIterator(1); itr.hasNext();)
            tmp.add(itr.next());

        tmp.add(playerList.get(0));

        playerList = tmp;

        for(Player p: playerList) {
            p.resetFirstTurn();
            p.resetSecondTurn();
            p.resetPlayedDice();
            p.resetUsedTool();
        }

        firstTurn = true;
    }

}