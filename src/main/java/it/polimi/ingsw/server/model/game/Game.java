package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.*;

import java.util.*;
import java.util.logging.Logger;

public class Game extends Observable {
    private Round round;
    private Board board;
    private List<Player> playerList;
    private int nPlayer;
    private int nRound;                 //counter from 1 to 10
    private static final Random random = new Random();

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

        round = new Round(playerList);
        try {
            board = new Board(nPlayer);
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }
        for (Player p: playerList){
            p.setBoard(board);
        }
    }

    /**
     * Add player p to Game
     * @param p != null && nPlayer < 4
     * @return true if login is successful
     * @throws SamePlayerException when try to add the same player
     */
    public boolean addPlayer(Player p) throws SamePlayerException {
        if (playerList.contains(p))
            throw new SamePlayerException("Player already in game");

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
            throw new EmptyException("Round List is empty");
        else {
            nPlayer--;
            return playerList.remove(p);
        }
    }

    /**
     * Find Player p to Game
     * @param p != null
     * @return true if Game.contain(p)
     * @throws PlayerNotFoundException when !Game.contain(p)
     * @throws EmptyException when Game have not player
     */
    public boolean findPlayer(Player p) throws PlayerNotFoundException, EmptyException {
        if (playerList.isEmpty())
            throw new EmptyException("No player in game");
        if (playerList.contains(p))
            return true;

        throw new PlayerNotFoundException("Player not found");
    }

    public void setnRound(int nRound) {
        this.nRound = nRound;
    }

    public int getNRound() {
        return nRound;
    }

    public Board getBoard() {
        return board;
    }

    public Round getRound() {
        return round;
    }

    public int getNPlayer() {
        return nPlayer;
    }

    public List<Player> getPlayerList(){
        return playerList;
    }

    /**
     * Return the Current Player of the Turn
     * @return return the current Player, null if the game is finished
     */
    public Player currentPlayer(){
        Player p = round.nextPlayer();
        while( p == null ){
                nRound++;
                if (nRound > 9)
                    return null;                //if currentPlayer -> null  the game is finished
                round.nextRound();
                p = round.nextPlayer();
            }
        return p;
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("Board: " + getBoard() + " Board: " + getBoard() +
                " nRound: " + getNRound() + " nPlayer: " + getNPlayer());
    }
}