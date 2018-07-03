package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import org.fusesource.jansi.Ansi;

import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class CliSystem implements ViewInterface {

    private static final String INSERT_NUMBER_KEYWORD = "INSERT_NUMBER";
    private static final String WINDOW_CARD_SELECTED_KEYWORD = "WINDOW_CARD_SELECTED";
    private static final String WINDOW_CARD_CHOSEN_KEYWORD = "WINDOW_CARD_CHOSEN";
    private static final String PLAYER_CARD_CHOSEN_KEYWORD = "PLAYER_CARD_CHOSEN";
    private static final String FAVOR_POINT_KEYWORD = "FAVOR_POINT";

    private static final String PRIVATE_OBJECTIVE_KEYWORD = "PRIVATE_OBJECTIVE_CHOSEN";
    private static final String PUBLIC_OBJECTIVE_KEYWORD = "PUBLIC_OBJECTIVE";
    private static final String OBJECTIVE_POINT_KEYWORD = "OBJECTIVE_POINT";

    private static final String TOOL_CARD_KEYWORD = "TOOL_CARD";

    private static final String OTHER_PLAYER_TURN_KEYWORD = "OTHER_PLAYER_TURN";
    private static final String YOUR_TURN_KEYWORD = "YOUR_TURN";

    private static final String SHOW_DRAFT_KEYWORD = "SHOW_DRAFT";
    private static final String SHOW_YOUR_WINDOW_CARD_TO_CHOOSE_KEYWORD = "SHOW_YOUR_WINDOW_CARD_TO_CHOOSE";
    private static final String ASK_ROW_KEYWORD = "ASK_ROW";
    private static final String ASK_COLUMN_KEYWORD = "ASK_COLUMN";

    private static final String POSITION = "Posizione: ";
    private static final String POINT = " - punteggio: ";
    private static final String USER = "\nUtente ";
    private static final String YOU_PLACED_DICE = "Hai piazzato il dado: ";
    private static final String OTHER_PLACED_DICE = " ha piazzato il dado: ";
    private static final String IN_CELL = " nella cella: ";

    private static final String QUIT_ENTRY_KEYWORD = "QUIT_ENTRY";
    private static final String QUIT_KEYWORD = "QUIT";

    private final CliAskConnection connection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private final Scanner inKeyboard;
    private String userName;

    private MenuTask taskMenu;
    private Thread menuThread;

    private Boolean quit = false;

    private final Semaphore semaphore;

    private final ViewMessageParser dictionary;

    public CliSystem() {
        this.connection = new CliAskConnection();
        this.inKeyboard = new Scanner(System.in);
        this.semaphore = new Semaphore(0);
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();

        this.taskMenu = new MenuTask(this);
        this.menuThread = new Thread(taskMenu);
    }

    /**
     * Starts the graphic setting up connection parameters
     */
    @Override
    public void startGraphic() {
        connection.startConnection(this);
    }

    /**
     * @param s to be printed
     */
    @Override
    public void print(String s) {
        out.println(s);
    }

    /**
     * @return user name of the player
     */
    String getUserName() {
        return this.userName;
    }

    /**
     * @param userName to be set
     */
    void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return server Speaker of the player
     */
    ServerSpeaker getServerSpeaker() {
        return this.serverSpeaker;
    }

    /**
     * @param serverSpeaker to be set
     */
    void setServerSpeaker(ServerSpeaker serverSpeaker) {
        this.serverSpeaker = serverSpeaker;
    }

    /**
     * drain all permits of semaphore
     */
    void drainPermits() {
        semaphore.drainPermits();
    }

    /**
     * Used to release permits on semaphore
     */
    void releaseSemaphore() {
        semaphore.release();
    }

    /**
     * Used to acquire permits on semaphore
     */
    void acquireSemaphore() {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * @param cards cards.size() = 4
     */
    @Override
    public void chooseWindowCard(List<WindowCard> cards) {
        print(dictionary.getMessage(WINDOW_CARD_SELECTED_KEYWORD));

        cards.forEach(card -> {
            printWindowCard(card);
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        ChooseWindowCardTask task = new ChooseWindowCardTask(this, cards);
        new Thread(task).start();
    }

    /**
     * @param user = Player.getId()
     * @param card = Player.getWindowCard().getName()
     */
    @Override
    public void showCardPlayer(String user, WindowCard card) {
        if (user.equals(this.userName))
            print(dictionary.getMessage(WINDOW_CARD_CHOSEN_KEYWORD));
        else
            print("\n" + user + dictionary.getMessage(PLAYER_CARD_CHOSEN_KEYWORD));

        printWindowCard(card);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * @param window window card to be printed
     */
    @Override
    public void printWindowCard(WindowCard window) {
        Cell c;

        out.println(window.getName());
        out.println(dictionary.getMessage(FAVOR_POINT_KEYWORD) + window.getNumFavPoint());

        for (int i = 0; i<window.getWindow().getMaxCol(); i++)
            out.print("\t" + i);
        print("");

        for (int i = 0; i<window.getWindow().getMaxRow(); i++) {
            out.print(i + "\t");

            for (int j = 0; j < window.getWindow().getMaxCol(); j++) {
                c = window.getWindow().getCell(i, j);

                if (c.isOccupied()) {
                    out.print(ansi().eraseScreen().bg(Color.valueOf(c.getDice().getColor().toString())).fg(BLACK).a(c.getDice().getValue()).reset() + "\t");
                }
                else
                    out.print(ansi().eraseScreen().fg(Color.valueOf(c.getColor().toString())).a(c.getValue()).reset() + "\t");

            }
            print("");
        }
        print("");

        semaphore.release();
    }

    /**
     * @param privateObj = Player.getPrivateObjective()
     */
    @Override
    public void printPrivateObj(ObjectiveCard privateObj) {
        print(dictionary.getMessage(PRIVATE_OBJECTIVE_KEYWORD));
        print(privateObj.getDescription());
    }

    /**
     * @param publicObj publicObj.size() = 3
     */
    @Override
    public void printListPublicObj(List<ObjectiveCard> publicObj) {
        print(dictionary.getMessage(PUBLIC_OBJECTIVE_KEYWORD));
        publicObj.forEach(p -> print("- " + p.getDescription() + dictionary.getMessage(OBJECTIVE_POINT_KEYWORD) + p.getPoint()));
    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {
        print(dictionary.getMessage(TOOL_CARD_KEYWORD));
        toolCards.forEach(tool -> print("- " + tool.getName()));
    }

    /**
     * @param username = game.getCurrentPlayer().getId()
     */
    @Override
    public void isTurn (String username) {
        if (userName.equals(username)) {
            print(dictionary.getMessage(YOUR_TURN_KEYWORD));
            taskMenu.setPlaying(true);

        } else {
            print(dictionary.getMessage(OTHER_PLAYER_TURN_KEYWORD) + username);
            taskMenu.setPlaying(false);
        }

        if (menuThread.getState().equals(Thread.State.NEW))
            menuThread.start();
    }

    /**
     * @param draft = game.getBoard().getDraft()
     */
    @Override
    public void showDraft(List<Dice> draft) {
        draft.forEach(dice ->
                out.print(ansi().eraseScreen().bg(Ansi.Color.valueOf(dice.getColor().toString())).fg(BLACK).a(dice.getValue()).reset() + "  "));

        semaphore.release();
    }

    /**
     * @param username of player moving the dice
     * @param dest     cell where the dice is being moved
     * @param moved    dice being moved
     */
    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {
        if (username.equals(userName))
            print(YOU_PLACED_DICE + ansi().eraseScreen().bg(Ansi.Color.valueOf(moved.getColor().toString())).fg(BLACK).a(moved.getValue()).reset()
                    + IN_CELL + "(" + dest.getRow() + "," + dest.getCol() + ") ");
        else
            print(USER + username + OTHER_PLACED_DICE + ansi().eraseScreen().bg(Ansi.Color.valueOf(moved.getColor().toString())).fg(BLACK).a(moved.getValue()).reset()
                    + IN_CELL + "(" + dest.getRow() + "," + dest.getCol() + ") ");

        taskMenu.setMoved();
        semaphore.release();
    }


    /**
     * Used when wrong placement is tried
     */
    @Override
    public void wrongPlacementDice(String errorMsg) {
        synchronized (this) {
            print(errorMsg);
        }
        semaphore.release();
    }

    /**
     * @param ranking sorted map of player username and their points through the game
     */
    @Override
    public void printRanking(SortedMap<Integer, String> ranking) {
        int sizeRanking = ranking.size();

        IntStream.range(1, sizeRanking+1).forEach(integer -> {
            int firstPointTmp = ranking.lastKey();
            String firstUserTmp = ranking.get(firstPointTmp);
            print(POSITION + integer + ": " + firstUserTmp + POINT + firstPointTmp);
            ranking.remove(firstPointTmp, firstUserTmp);
        });
    }

    /**
     * Used to get what dice to move and where it wants to move
     */
    void moveDice() {
        quit = false;
        int index;
        int row;
        int col;

        //place a dice (show personal window card and draft to choose dice)

        print(dictionary.getMessage(SHOW_YOUR_WINDOW_CARD_TO_CHOOSE_KEYWORD));
        serverSpeaker.askWindowCard(userName, userName);

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        print(dictionary.getMessage(SHOW_DRAFT_KEYWORD));
        serverSpeaker.askDraft(userName);

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        index = getIndex();

        row = getRow();

        col = getCol();

        if (!quit)
            serverSpeaker.placementDice(userName, index, row, col);
        else
            semaphore.release(2);
    }

    /**
     * Used internally to get index of dice in the draft
     * @return index of dice in the draft
     */
    private int getIndex() {
        int index = -1;

        while (index < 0 && !quit) {
            print(dictionary.getMessage(INSERT_NUMBER_KEYWORD) + " - "
                    + dictionary.getMessage(QUIT_ENTRY_KEYWORD) + dictionary.getMessage(QUIT_KEYWORD));
            try {
                String line = inKeyboard.nextLine();
                if (line.equals(dictionary.getMessage(QUIT_ENTRY_KEYWORD)))
                    quit = true;
                else {
                    index = Integer.parseInt(line);
                    index--;
                }
            } catch (NumberFormatException e) {
                print(e.getMessage());
                index = -1;
            }
        }

        return index;
    }

    /**
     * Used internally to get the row of destination cell
     * @return row of the destination cell
     */
    private int getRow() {
        int row = -1;

        while (row < 0 && !quit) {
            print(dictionary.getMessage(ASK_ROW_KEYWORD));
            try {
                String line = inKeyboard.nextLine();
                if (line.equals(dictionary.getMessage(QUIT_ENTRY_KEYWORD)))
                    quit = true;
                else
                    row = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                print(e.getMessage());
                row = -1;
            }
        }

        return row;
    }

    /**
     * Used internally to get the col of destination cell
     * @return col of the destination cell
     */
    private int getCol() {
        int col = -1;

        while (col < 0 && !quit) {
            print(dictionary.getMessage(ASK_COLUMN_KEYWORD));
            try {
                String line = inKeyboard.nextLine();
                if (line.equals(dictionary.getMessage(QUIT_ENTRY_KEYWORD)))
                    quit = true;
                else
                    col = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                print(e.getMessage());
                col = -1;
            }
        }

        return col;
    }

    void useToolCard(){

    } //use tool card (show tool cards and choose which one use)

}