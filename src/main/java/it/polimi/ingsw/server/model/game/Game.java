package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Optional;
import java.util.logging.Logger;

public class Game extends Observable {

    private static final String DUMP_BOARD_MSG = "Board: ";
    private static final String DUMP_N_PLAYER_MSG = " nPlayer: ";
    private static final String DUMP_N_ROUND_MSG = " nRound: ";

    private static final String PLAYER_ALREADY_GAME_MSG = "Giocatore già in partita";
    private static final String ROUND_EMPTY_MSG = "Lista dei round vuota";
    private static final String NO_PLAYER_MSG = "Nessun giocatore in partita";
    private static final String PLAYER_NOT_FOUND_MSG = "Giocatore non trovato";

    private static final String NOTIFY_NEXT_TURN = "nextTurn";

    private Round round;
    private Board board;
    private Player currentPlayer;
    private List<Player> playerList;
    private int nPlayer;
    private int nRound;                 //counter from 1 to 10

    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Game() {
        playerList = new ArrayList<>();
        nPlayer = 0;
        nRound = 0;
    }

    /**
     * Initialization
     */
    public void startGame() {
        round = new Round(playerList, this);
        try {
            board = new Board(nPlayer);
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }
        playerList.forEach(p -> p.setBoard(board));
    }

    /**
     * Add player p to Game
     * @param p != null && nPlayer < 4
     * @return true if login is successful
     * @throws SamePlayerException when try to add the same player
     */
    public boolean addPlayer(Player p) throws SamePlayerException {
        if (playerList.contains(p))
            throw new SamePlayerException(PLAYER_ALREADY_GAME_MSG);

        nPlayer++;
        return playerList.add(p);
    }

    /**
     * Remove player p to Game
     * @param p != null
     * @return true if rmPlayer is successful
     * @throws EmptyException when RoundList is empty
     */
    public boolean rmPlayer(Player p) throws EmptyException {
        if (playerList.isEmpty())
            throw new EmptyException(ROUND_EMPTY_MSG);
        else {
            nPlayer--;
            return playerList.remove(p);
        }
    }

    /**
     * Find Player p to Game
     * @param id != null
     * @return true if Game.contain(p)
     * @throws PlayerNotFoundException when !Game.contain(p)
     * @throws EmptyException when Game have not player
     */
    public Player findPlayer(String id) throws PlayerNotFoundException, EmptyException {
        if (playerList.isEmpty())
            throw new EmptyException(NO_PLAYER_MSG);
        Optional<Player> ret = playerList.stream().filter(player -> player.getId().equals(id)).findFirst();

        if (ret.isPresent())
            return ret.get();

        throw new PlayerNotFoundException(PLAYER_NOT_FOUND_MSG);
    }

    /**
     * @param nRound to be set as current round number
     */
    public void setNumRound(int nRound) {
        this.nRound = nRound;
    }

    /**
     * @return current round number
     */
    public int getNumRound() {
        return nRound;
    }

    /**
     * @return current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return current round
     */
    public Round getRound() {
        return round;
    }

    /**
     * @return current number of players in the game
     */
    public int getNumPlayer() {
        return nPlayer;
    }

    /**
     * Return the Current Player of the Turn
     * @return return the current Player, null if the game is finished
     * @throws SameDiceException when move Draft finds a dice already in round Track
     * @throws EmptyException when finds an empty dice bag
     * @throws IDNotFoundException when internal error on adding dice occurs
     */
    public Player nextPlayer() throws SameDiceException, EmptyException, IDNotFoundException {
        Player p = round.nextPlayer();

        if (p == null) {
            nRound++;

            if (nRound > 9)
                return null;                //if currentPlayer -> null  the game is finished

            round.nextRound();
            p = round.nextPlayer();
        }

        currentPlayer = p;

        setChanged();
        notifyObservers(NOTIFY_NEXT_TURN);

        return p;
    }

    /**
     * @return player whose current turn is
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump() {
        logger.info(DUMP_BOARD_MSG + getBoard() + DUMP_N_ROUND_MSG + getNumRound() + DUMP_N_PLAYER_MSG + getNumPlayer());
    }
}