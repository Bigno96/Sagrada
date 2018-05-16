package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.exception.SamePlayerException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Game {
    private Round round;
    private Board board;
    private List<Player> playerList;
    private int nPlayer;
    private int nRound;                 //counter from 1 to 10

    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Game(){
        playerList = new ArrayList<>();
        nPlayer = 0;
        nRound = 0;
    }

    public void startGame() throws IDNotFoundException {
        round = new Round(playerList);
        board = new Board(nPlayer);
    }

    public boolean addPlayer(Player p) throws SamePlayerException {
        if (playerList.contains(p))
            throw new SamePlayerException("Player already in game");

        nPlayer++;
        return playerList.add(p);
    }

    public boolean rmPlayer(Player p) throws EmptyException {
        if (playerList.isEmpty())
            throw new EmptyException("Round List is empty");
        else {
            nPlayer--;
            return playerList.remove(p);
        }
    }

    public Player findPlayer(Player p) throws PlayerNotFoundException, EmptyException {
        if (playerList.isEmpty())
            throw new EmptyException("No player in game");
        if (playerList.contains(p))
            return p;
        else
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
