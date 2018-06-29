package it.polimi.ingsw.client.network;

/**
 * Interface to hide network difference in comm Client -> Server
 */
public interface ServerSpeaker {

    /**
     * Used locally to change ip after a failed connection toward server
     * @param ip isValidIPv4Address || isValidIPv6Address
     */
    void setIp(String ip);

    /**
     * Used to connect client to server. No control on username yet.
     * @param username != null
     * @return true if connection was successful, false else
     */
    boolean connect(String username);

    /**
     * Used to login client to server. Username and other restrictions are controlled.
     * @param username != null
     * @return true if login was successful, false else
     */
    boolean login(String username);

    /**
     * Used to set the window card chosen by passed player
     * @param username = Player.getId()
     * @param cardName = Player.getWindowCard().getName()
     */
    void setWindowCard(String username, String cardName);

    /**
     * Used to get which window card has the passed player
     * @param usernameWanted = Player.getId()
     * @param me username of player that requested
     */
    void askWindowCard(String usernameWanted, String me);

    /**
     * Used to get username of all Players in the game
     * @param currentUser username of player that requested
     */
    void getAllUsername(String currentUser);

    /**
     * Used to get Draft of current round
     * @param username of player that requested
     */
    void askDraft(String username);

    /**
     * Used to get public objectives of the game
     * @param username of player that requested
     */
    void askPublicObj(String username);

    /**
     * Used to get private objective of the passed player. Only works for owner private objective.
     * @param username = Player.getId() && Player.getPrivateObj()
     */
    void askPrivateObj(String username);

    void askToolCards(String username);

    void askFavorPoints(String username);

    /**
     * Used to pass the current player
     * @param username of Player that wants to end his turn
     */
    void endTurn(String username);

    /**
     * Used to place dice, passing index of dice in the draft and row and column of the destination cell
     * @param username of player moving the dice
     * @param index in the draft of the dice
     * @param row of the destination cell
     * @param col of the destination cell
     */
    void placementDice(String username, int index, int row, int col);

}

