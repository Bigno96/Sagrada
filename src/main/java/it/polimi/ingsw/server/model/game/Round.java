package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.exception.SameDiceException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;


public class Round {

    private static final String DUMP_MSG = "contains following players: ";
    private static final String PLAYER_NOT_FOUND_MSG = "ID of the player not found";

    private List<Player> playerList;
    private Game game;
    private static final Logger logger = Logger.getLogger(Player.class.getName());
    private boolean firstTurn;      //false if all player finished the first round

    /**
     * Constructor
     * @param playerList list of player ordered by turn order
     */
    public Round(List<Player> playerList, Game game) {
        this.game = game;
        this.playerList = playerList;
        firstTurn = true;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info(DUMP_MSG);
        playerList.forEach(Player::dump);
    }

    /**
     * Next Player
     * @return the next player, who will play
     */
    public Player nextPlayer() {
        if (firstTurn) {
            for (Player p : playerList) {
                if (p.isFirstTurn()) {
                    p.setFirstTurn(false);
                    return p;
                }
            }
            playerList.forEach(Player::nextTurn);

            firstTurn = false;      //if all player finished the first turn, set firstTurn = false
        }

        //The second turn of the round, will start from the last player of the previous turn
        for (ListIterator<Player> itr = playerList.listIterator(playerList.size()); itr.hasPrevious();) {
            Player p = itr.previous();
            if (p.isSecondTurn()) {
                p.setSecondTurn(false);
                return p;
            }
        }

        return null;
    }

    /**
     * When the second turn of the round finished, nextRound reset all parameters of Player and Round
     * and move the first Player of the previous turn at the end of the playerList
     * @throws SameDiceException when move Draft finds a dice already in round Track
     * @throws EmptyException when finds an empty dice bag
     * @throws IDNotFoundException when internal error on adding dice occurs
     */
    public void nextRound() throws SameDiceException, EmptyException, IDNotFoundException {
        List<Player> tmp = new ArrayList<>(playerList.subList(1, playerList.size()));

        tmp.add(playerList.get(0));

        playerList = tmp;

        playerList.forEach(p -> {
            p.setFirstTurn(true);
            p.setSecondTurn(true);
            p.nextTurn();
        });

        firstTurn = true;
        game.getBoard().getRoundTrack().moveDraft(game.getNumRound()-1);

        game.getBoard().getDraft().fillDraft();
        game.getBoard().getDraft().rollDraft();
    }

    /**
     * Find Player with thaht ID, on the list of players
     * @param id != null
     * @return Player with this id
     * @throws PlayerNotFoundException when Id of the player not found
     */
    public Player getPlayer(String id) throws PlayerNotFoundException {
        for (Player p: playerList){
            if (p.getId().equals(id))
                return p;
        }

        throw new PlayerNotFoundException(PLAYER_NOT_FOUND_MSG);
    }

}