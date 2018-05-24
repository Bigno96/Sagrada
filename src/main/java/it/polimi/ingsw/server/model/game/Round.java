package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.PlayerNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


public class Round {

    private List<Player> playerList;
    private static final Logger logger = Logger.getLogger(Player.class.getName());
    private boolean firstTurn;      //false if all player finished the first round

    /**
     * Constructor
     * @param playerList list of player ordered by turn order
     */
    public Round(List<Player> playerList) {
        this.playerList = playerList;
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

    /**
     * Next Player
     * @return the next player, who will play
     */
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

            firstTurn = false;      //if all player finished the first turn, set firstTurn = false
        }

        //The second turn of the round, will start from the last player of the previous turn
        for (ListIterator<Player> itr = playerList.listIterator(playerList.size()); itr.hasPrevious(); ) {
            Player p = itr.previous();
            if (p.isSecondTurn()) {
                p.endSecondTurn();
                return p;
            }
        }

        return null;
    }

    /**
     * When the second turn of the round finished, nextRound reset all parameters of Player and Round
     * and move the first Player of the previous turn at the end of the playerList
     */
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

    /**
     * Find Player with thaht ID, on the list of players
     * @param id != null
     * @return Player with this id
     * @throws PlayerNotFoundException when Id of the player not found
     */
    public Player getPlayer(int id) throws PlayerNotFoundException {
        for (Player p: playerList){
            if (p.getId() == id)
                return p;
        }

        throw new PlayerNotFoundException("ID of the player not found");
    }

}