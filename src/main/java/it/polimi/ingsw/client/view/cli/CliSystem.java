package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.Colors;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import org.fusesource.jansi.Ansi;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;
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

    private static final String SHOW_ROUND_TRACK_KEYWORD = "SHOW_ROUND_TRACK";
    private static final String SHOW_TOOL_CARD_KEYWORD = "SHOW_TOOL_CARD";
    private static final String SHOW_FAVOR_POINT_LEFT_KEYWORD = "SHOW_FAVOR_POINT_LEFT";
    private static final String CHOOSE_TOOL_CARD_KEYWORD = "CHOOSE_TOOL_CARD";
    private static final String INSERT_ROUND_KEYWORD = "INSERT_ROUND";
    private static final String DICE_VALUE_KEYWORD = "DICE_VALUE";
    private static final String DICE_COLOR_KEYWORD = "DICE_COLOR";
    private static final String BOOLEAN_ASK_KEYWORD = "BOOLEAN_ASK";
    private static final String UP_ENTRY_KEYWORD = "UP_ENTRY";
    private static final String DOWN_ENTRY_KEYWORD = "DOWN_ENTRY";
    private static final String UP_MESSAGE_KEYWORD = "UP_MESSAGE";
    private static final String DOWN_MESSAGE_KEYWORD = "DOWN_MESSAGE";

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
    private static final String ROUND = "Round ";
    private static final String NUMBER_FAVOR_POINTS = "Numero di punti favore = ";

    private static final String QUIT_ENTRY_KEYWORD = "QUIT_ENTRY";
    private static final String QUIT_KEYWORD = "QUIT";

    private final CliAskConnection connection;
    private ServerSpeaker serverSpeaker;        // handles communication Client -> Server
    private final Scanner inKeyboard;
    private String userName;

    private MenuTask taskMenu;
    private Thread menuThread;

    private EnumMap<ToolCard.Actor, Consumer<String>> actorMap;
    private EnumMap<ToolCard.Parameter, Consumer<String>> parameterMap;

    private List<ToolCard.Actor> actor;
    private List<ToolCard.Parameter> parameter;

    private List<Dice> dices;
    private List<Cell> cells;
    private int diceValue;
    private Colors diceColor;
    private Boolean up;

    private Boolean quit = false;
    private int draftLength;

    private Boolean nullCheck;
    private int nDices = 0;
    private int nCells = 0;

    private final Semaphore semaphore;

    private final ViewMessageParser dictionary;

    public CliSystem() {
        this.connection = new CliAskConnection();
        this.inKeyboard = new Scanner(System.in);
        this.semaphore = new Semaphore(0);
        this.actorMap = new EnumMap<>(ToolCard.Actor.class);
        this.nullCheck = true;
        this.parameterMap = new EnumMap<>(ToolCard.Parameter.class);
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();

        mapActor();
        mapParameter();
    }

    /**
     * Starts the graphic setting up connection parameters
     */
    @Override
    public void startGraphic() {
        connection.startConnection(this);
        this.taskMenu = new MenuTask(this);
        this.menuThread = new Thread(taskMenu);
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

        semaphore.release();            // releasing for menuTask action.accept()
    }

    /**
     * @param privateObj = Player.getPrivateObjective()
     */
    @Override
    public void printPrivateObj(ObjectiveCard privateObj) {
        print(dictionary.getMessage(PRIVATE_OBJECTIVE_KEYWORD));
        print(privateObj.getDescription());

        semaphore.release();            // releasing for menuTask action.accept()
    }

    /**
     * @param publicObj publicObj.size() = 3
     */
    @Override
    public void printListPublicObj(List<ObjectiveCard> publicObj) {
        print(dictionary.getMessage(PUBLIC_OBJECTIVE_KEYWORD));
        publicObj.forEach(p -> print("- " + p.getDescription() + dictionary.getMessage(OBJECTIVE_POINT_KEYWORD) + p.getPoint()));

        semaphore.release();            // releasing for menuTask action.accept()
    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {
        print(dictionary.getMessage(SHOW_TOOL_CARD_KEYWORD));
        toolCards.forEach(tool -> {
            print("- " + tool.getName());
            print("     " + NUMBER_FAVOR_POINTS + tool.getFavorPoint());
        });

        semaphore.release();            // releasing for menuTask action.accept()
    }

    /**
     * @param username = game.getCurrentPlayer().getId()
     */
    @Override
    public void isTurn (String username) {

        taskMenu.clearCurrentState();

        if (userName.equals(username)) {
            print(dictionary.getMessage(YOUR_TURN_KEYWORD));
            taskMenu.setPlaying(true);

        } else {
            print(dictionary.getMessage(OTHER_PLAYER_TURN_KEYWORD) + username);
            taskMenu.setPlaying(false);
        }

        semaphore.release();            // releasing for menuTask action.accept()

        if (menuThread.getState().equals(Thread.State.NEW)) {
            semaphore.drainPermits();
            menuThread.start();
        }
    }

    /**
     * @param draft = game.getBoard().getDraft()
     */
    @Override
    public void showDraft(List<Dice> draft) {
        draftLength = draft.size();
        print(dictionary.getMessage(SHOW_DRAFT_KEYWORD));

        draft.forEach(dice ->
                out.print(ansi().eraseScreen().bg(Ansi.Color.valueOf(dice.getColor().toString())).fg(BLACK).a(dice.getValue()).reset() + "  "));

        out.print("\n");
        semaphore.release();            // releasing for menuTask action.accept()
    }

    /**
     * @param roundTrack = game.getBoard().getRoundTrack()
     */
    @Override
    public void showRoundTrack(RoundTrack roundTrack) {
        print(dictionary.getMessage(SHOW_ROUND_TRACK_KEYWORD));

        roundTrack.getTrackList().forEach(listDiceRound -> {
            out.print(ROUND + roundTrack.getTrackList().indexOf(listDiceRound) + ": ");

            listDiceRound.itr().forEachRemaining(dice ->
                    out.print(ansi().eraseScreen().bg(Ansi.Color.valueOf(dice.getColor().toString())).fg(BLACK).a(dice.getValue()).reset() + "  "));

            out.print("\n");
        });

        semaphore.release();            // releasing for menuTask action.accept()
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
        // releasing for menuTask action.accept() is made by print window card called after this
    }


    /**
     * Used when wrong placement is tried
     */
    @Override
    public void wrongPlacementDice(String errorMsg) {
        synchronized (this) {
            print(errorMsg);
        }
        semaphore.release();            // releasing for menuTask action.accept()
    }

    @Override
    public void printFavorPoints(int point) {
        print(dictionary.getMessage(SHOW_FAVOR_POINT_LEFT_KEYWORD) + point);
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
        else {
            semaphore.drainPermits();
            semaphore.release();            // releasing for menuTask action.accept()
        }
    }

    /**
     * Used internally to get index of dice in the draft
     * @return index of dice in the draft
     */
    private int getIndex() {
        int index = -1;

        while ((index < 0 || index >= draftLength) && !quit) {
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


    /**
     * Using tool card
     */
    void useToolCard() {
        quit = false;
        dices = new ArrayList<>();
        cells = new ArrayList<>();
        diceValue = 0;
        diceColor = null;
        up = null;

        serverSpeaker.askToolCards(userName);

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        serverSpeaker.askFavorPoints(userName);

        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        doingTool();

        semaphore.drainPermits();
        semaphore.release();                // releasing for menuTask action.accept()
    }

    /**
     * Checking precondition, requesting parameter and calling checkAndUseTool
     */
    private void doingTool() {
        Boolean wrong = false;

        while (!quit || wrong) {
            int pick = pickTool();

            Boolean validity = serverSpeaker.checkPreCondition(pick, userName);

            if (!validity)
                wrong = true;
            else {
                actor = serverSpeaker.getActor(pick, userName);
                showActor();

                parameter = serverSpeaker.getParameter(pick, userName);
                getParameter();

                if (checkNull())
                    wrong = checkAndUseTool(wrong, pick);
                else
                    wrong = true;
            }
        }
    }

    /**
     * Checks if some parameter are null when they should not be null
     * @return true if everything is okay, false else
     */
    private Boolean checkNull() {
        nullCheck = true;

        parameter.forEach(param -> {
            if (param.equals(ToolCard.Parameter.DICE))
                nDices++;
            else if (param.equals(ToolCard.Parameter.CELL))
                nCells++;
            else if (param.equals(ToolCard.Parameter.BOOLEAN))
                nullCheck = up != null && nullCheck;
            else if (param.equals(ToolCard.Parameter.COLOR))
                nullCheck = diceColor != null && nullCheck;
            else if (param.equals(ToolCard.Parameter.INTEGER))
                nullCheck = diceValue != 0 && nullCheck;
        });

        IntStream.range(0, nDices).forEach(integer ->
            nullCheck = dices.get(integer) != null && nullCheck);

        IntStream.range(0, nCells).forEach(integer ->
            nullCheck = cells.get(integer) != null && nullCheck);

        return nullCheck;
    }

    /**
     * Check if tool is correct and try to applies it
     * @param wrong to remain in the loop of the caller
     * @return true if all correct, false else
     */
    private Boolean checkAndUseTool(Boolean wrong, int pick) {
        Boolean success;
        Boolean validity;

        if (!quit) {
            validity = serverSpeaker.checkTool(pick, dices, cells, diceValue, diceColor);

            if (!validity)
                wrong = true;
            else {
                success = serverSpeaker.useTool(pick, dices, up, cells);

                if (!success)
                    wrong = true;
            }
        }

        return wrong;
    }

    /**
     * Used internally to get index of what tool card player wants
     * @return pick pick > 0 && pick < 4, number of tool card player wants to use
     */
    private int pickTool() {
        int pick = -1;

        while ((pick < 0 || pick > 2) && !quit) {
            print(dictionary.getMessage(CHOOSE_TOOL_CARD_KEYWORD) + " - "
                    + dictionary.getMessage(QUIT_ENTRY_KEYWORD) + dictionary.getMessage(QUIT_KEYWORD));

            try {
                String line = inKeyboard.nextLine();
                if (line.equals(dictionary.getMessage(QUIT_ENTRY_KEYWORD)))
                    quit = true;
                else {
                    pick = Integer.parseInt(line);
                    pick--;
                }
            } catch (NumberFormatException e) {
                print(e.getMessage());
                pick = -1;
            }
        }

        return pick;
    }

    /**
     * Used to map ActorMap that contains actions for each possible actor
     */
    private void mapActor() {
        Consumer<String> windowCard = username -> serverSpeaker.askWindowCard(username, username);
        Consumer<String> roundTrack = username -> serverSpeaker.askRoundTrack(username);
        Consumer<String> draft = username -> serverSpeaker.askDraft(username);

        actorMap.put(ToolCard.Actor.WINDOW_CARD, windowCard);
        actorMap.put(ToolCard.Actor.ROUND_TRACK, roundTrack);
        actorMap.put(ToolCard.Actor.DRAFT, draft);
    }

    /** Used to read which actor does the tool need and to print it consequently
     */
    private void showActor() {
        actor.forEach(act -> {
            actorMap.get(act).accept(userName);
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Used to obtain from player the parameter needed for using tool card
     */
    private void getParameter() {
        parameter.forEach(param -> {
            parameterMap.get(param).accept(userName);
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * Used to map ParameterMap that contains actions for each possible parameter
     */
    private void mapParameter() {
        Consumer<String> dice = string -> {
            if (actor.contains(ToolCard.Actor.DRAFT))
                dices.add(getDiceFromDraft());
            else if (actor.contains(ToolCard.Actor.ROUND_TRACK))
                dices.add(getDiceFromRoundTrack());
            else if (actor.contains(ToolCard.Actor.WINDOW_CARD))
                dices.add(getDiceFromWindow());

            semaphore.release();
        };
        Consumer<String> cell = string -> {
            cells.add(getCellFromWindow());
            semaphore.release();
        };
        Consumer<String> integer = string -> {
            diceValue = getDiceValue();
            semaphore.release();
        };
        Consumer<String> color = string -> {
            diceColor = getColorOnTrack();
            semaphore.release();
        };
        Consumer<String> bool = string -> {
            up = getBooleanDirection();
            semaphore.release();
        };

        parameterMap.put(ToolCard.Parameter.DICE, dice);
        parameterMap.put(ToolCard.Parameter.CELL, cell);
        parameterMap.put(ToolCard.Parameter.INTEGER, integer);
        parameterMap.put(ToolCard.Parameter.COLOR, color);
        parameterMap.put(ToolCard.Parameter.BOOLEAN, bool);
    }

    /**
     * Used to get if the player wants to add or remove 1 to the dice
     * @return true if user wants to add 1 to the dice, false if user wants minus 1 to the dice
     */
    private Boolean getBooleanDirection() {
        Boolean wrong = true;
        Boolean ret = null;

        while (wrong && !quit) {
            print(dictionary.getMessage(BOOLEAN_ASK_KEYWORD) + " - "
                    + dictionary.getMessage(QUIT_ENTRY_KEYWORD) + dictionary.getMessage(QUIT_KEYWORD));
            print("'" + dictionary.getMessage(UP_ENTRY_KEYWORD) + "'" + dictionary.getMessage(UP_MESSAGE_KEYWORD));
            print("'" + dictionary.getMessage(DOWN_ENTRY_KEYWORD) + "'" + dictionary.getMessage(DOWN_MESSAGE_KEYWORD));

            String line = inKeyboard.nextLine();
            if (line.equals(dictionary.getMessage(QUIT_ENTRY_KEYWORD)))
                quit = true;
            else {
                if (line.equals(dictionary.getMessage(UP_ENTRY_KEYWORD))) {
                    wrong = false;
                    ret = true;
                }
                else if (line.equals(dictionary.getMessage(DOWN_ENTRY_KEYWORD))) {
                    wrong = false;
                    ret = false;
                }
                else
                    wrong = true;
            }
        }

        return ret;
    }

    /**
     * Used to ask player what Colors he wants to get out from the round track
     * @return Colors for the dice wanted by user
     */
    private Colors getColorOnTrack() {
        List<Integer> coordinates = new ArrayList<>();
        int index = -1;

        if (!quit) {
            coordinates.add(getRoundNumber());

            while ((index < 0 || index >= draftLength) && !quit) {
                print(dictionary.getMessage(DICE_COLOR_KEYWORD));
                print(dictionary.getMessage(INSERT_NUMBER_KEYWORD));

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

            if (!quit) {
                coordinates.add(index);

                return serverSpeaker.getColorFromRoundTrack(userName, coordinates);
            }
        }

        return null;
    }

    /**
     * Used to ask player what value he wants to set on the dice
     * @return value for the dice wanted by user
     */
    private int getDiceValue() {
        int value = 0;

        while ((value < 1 || value > 6) && !quit) {
            print(dictionary.getMessage(DICE_VALUE_KEYWORD) + " - "
                    + dictionary.getMessage(QUIT_ENTRY_KEYWORD) + dictionary.getMessage(QUIT_KEYWORD));

            String line = inKeyboard.nextLine();
            if (line.equals(dictionary.getMessage(QUIT_ENTRY_KEYWORD))) {
                quit = true;
                value = 0;
            }
            else
                value = Integer.parseInt(line);

        }

        return value;
    }

    /**
     * Used to ask player what cell he wants out of the window card for using it in the tool card
     * @return cell that user requested from his window card
     */
    private Cell getCellFromWindow() {
        List<Integer> coordinates = new ArrayList<>();

        coordinates.add(getRow());
        coordinates.add(getCol());

        if (!quit)
            return serverSpeaker.getCellFromWindow(userName, coordinates);
        else
            return null;
    }

    /**
     * Used to ask player what dice he wants out of the window card for using it in the tool card
     * @return dice that user requested from his window card
     */
    private Dice getDiceFromWindow() {
        List<Integer> coordinates = new ArrayList<>();

        coordinates.add(getRow());
        coordinates.add(getCol());

        if (!quit)
            return serverSpeaker.getDiceFromActor(ToolCard.Actor.WINDOW_CARD, userName, coordinates);
        else
            return null;
    }

    /**
     * Used to ask player what dice he wants out of round track for using it in the tool card
     * @return dice that user requested from round track
     */
    private Dice getDiceFromRoundTrack() {
        List<Integer> coordinates = new ArrayList<>();

        if (!quit) {
            coordinates.add(getRoundNumber());
            coordinates.add(getIndex());

            if (!quit)
                return serverSpeaker.getDiceFromActor(ToolCard.Actor.ROUND_TRACK, userName, coordinates);
            else
                return null;
        }
        else
            return null;
    }

    /**
     * @return number of round wanted by the user
     */
    private int getRoundNumber() {
        int round = -1;

        while ((round < 0 || round > 10) && !quit) {
            print(dictionary.getMessage(INSERT_ROUND_KEYWORD) + " - "
                    + dictionary.getMessage(QUIT_ENTRY_KEYWORD) + dictionary.getMessage(QUIT_KEYWORD));
            try {
                String line = inKeyboard.nextLine();
                if (line.equals(dictionary.getMessage(QUIT_ENTRY_KEYWORD)))
                    quit = true;
                else {
                    round = Integer.parseInt(line);
                    round--;
                }
            } catch (NumberFormatException e) {
                print(e.getMessage());
                round = -1;
            }
        }

        return round;
    }

    /**
     * Used to ask player what dice he wants out of draft for using it in the tool card
     * @return dice that user requested from draft
     */
    private Dice getDiceFromDraft() {
        List<Integer> coordinates = new ArrayList<>();

        coordinates.add(getIndex());

        if (!quit)
            return serverSpeaker.getDiceFromActor(ToolCard.Actor.DRAFT, userName, coordinates);
        else
            return null;
    }

}