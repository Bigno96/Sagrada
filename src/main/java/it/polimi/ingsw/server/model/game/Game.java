package it.polimi.ingsw.server.model.game;

import it.polimi.ingsw.exception.EmptyException;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PlayerNotFoundException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveCard;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveFactory;
import it.polimi.ingsw.server.model.objectivecard.ObjectiveStrategy;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.toolcard.ToolFactory;
import it.polimi.ingsw.server.model.toolcard.ToolStrategy;

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

    /**
     * Constructor
     */
    public Game(){
        playerList = new ArrayList<>();
        nPlayer = 0;
        nRound = 0;
    }

    /**
     * Initialization
     * @throws IDNotFoundException when getPublCard, getPrivCard and makeToolCard
     * @throws FileNotFoundException when getPublCard, getPrivCard and makeToolCard
     */
    public void startGame() {
        int id;
        int id2;
        int id3;
        List<Integer> vetID = new ArrayList<>();
        ObjectiveCard obj1;
        ObjectiveCard obj2;
        ObjectiveCard obj3;
        ObjectiveCard objPriv;
        ToolCard tool1;
        ToolCard tool2;
        ToolCard tool3;
        ObjectiveStrategy objStrat = new ObjectiveStrategy();
        ObjectiveFactory obj = new ObjectiveFactory(objStrat);

        round = new Round(playerList);
        try {
            board = new Board(nPlayer);
        } catch (IDNotFoundException e) {
            logger.info(e.getMessage());
        }

        ToolStrategy toolStrat = new ToolStrategy(board.getRoundTrack(), board.getDraft(), board.getDiceBag());
        ToolFactory tool = new ToolFactory(toolStrat, this);

        for (Player p: playerList){
            p.setBoard(board);
        }

        try {
            id = random.nextInt(10) + 1;
            obj1 = obj.getPublCard(id);

            do {
                id2 = random.nextInt(10) + 1;
            } while (id2 == id);
            obj2 = obj.getPublCard(id2);

            do {
                id3 = random.nextInt(10) + 1;
            } while (id3 == id || id3 == id2);
            obj3 = obj.getPublCard(id3);

            board.setPublObj(obj1, obj2, obj3);

            for (Player p : playerList) {
                do {
                    id = random.nextInt(5) + 1;
                } while (vetID.contains(id));
                vetID.add(id);
                objPriv = obj.getPrivCard(id);
                p.setPrivObj(objPriv);
            }

            id = random.nextInt(12) + 1;
            tool1 = tool.makeToolCard(id);

            do {
                id2 = random.nextInt(12) + 1;
            } while (id2 == id);
            tool2 = tool.makeToolCard(id2);

            do {
                id3 = random.nextInt(12) + 1;
            } while (id3 == id || id3 == id2);
            tool3 = tool.makeToolCard(id3);

            board.setToolCard(tool1, tool2, tool3);

        } catch (IDNotFoundException | FileNotFoundException e) {
            logger.info(e.getMessage());
        }
    }

    /**
     * Add player p to Game
     * @param p != null && nPlayer < 4
     * @return true if addPlayer is successful
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