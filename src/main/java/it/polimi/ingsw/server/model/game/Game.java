package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveFactory;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveStrategy;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class Game {
    private Round round;
    private Board board;
    private List<Player> playerList;
    private int nPlayer;
    private int nRound;                 //counter from 1 to 10
    private static final Random random = new Random();

    private static final Logger logger = Logger.getLogger(Player.class.getName());

    public Game(){
        playerList = new ArrayList<>();
        nPlayer = 0;
        nRound = 0;
    }

    public void startGame() throws IDNotFoundException, FileNotFoundException {
        int id, id2, id3;
        List<Integer> vetID = new ArrayList<>();
        ObjectiveCard obj1, obj2, obj3, objPriv;
        ObjectiveStrategy objStrat = new ObjectiveStrategy();
        ObjectiveFactory obj = new ObjectiveFactory(objStrat);

        round = new Round(playerList);
        board = new Board(nPlayer);

        for (Player p: playerList){
            p.setBoard(board);
        }

        id = random.nextInt(10)+1;
        obj1 = obj.getPublCard(id);

        do {
            id2 = random.nextInt(10) + 1;
        }while(id2 == id);
        obj2 = obj.getPublCard(id);

        do {
            id3 = random.nextInt(10) + 1;
        }while (id3 == id || id3 == id2);
        obj3 = obj.getPublCard(id);

        board.setPublObj(obj1, obj2, obj3);

        for (Player p: playerList){
            do {
                id = random.nextInt(5) + 1;
            }while (vetID.contains(id));
            vetID.add(id);
            objPriv = obj.getPrivCard(id);
            p.setPrivObj(objPriv);
        }

        //create and assign (setToolCard) toolCard
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
