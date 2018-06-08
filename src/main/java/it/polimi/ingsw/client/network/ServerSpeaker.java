package it.polimi.ingsw.client.network;

public interface ServerSpeaker {
    // interface to hide network difference in comm Client -> Server

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

    void setWindowCard(String username, String name);

    void askWindowCard(String usernameWanted, String me);

    void getAllUsername(String currUser);

    void askDraft(String username);

    void askPublObj(String username);

    void askPrivObj(String username);

    void askToolCards(String username);

    void askFavorPoints(String username);

    void endTurn(String username);

    void moveDiceFromDraftToCard(String username, int index, int row, int col);

}

