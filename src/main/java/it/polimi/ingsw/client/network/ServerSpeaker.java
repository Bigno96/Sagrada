package it.polimi.ingsw.client.network;

import it.polimi.ingsw.exception.SamePlayerException;

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
     * @throws SamePlayerException when trying to login same player twice
     */
    boolean connect(String username) throws SamePlayerException;

    /**
     * Used to login client to server. Username and other restrictions are controlled.
     * @param username != null
     * @return true if login was successful, false else
     */
    boolean login(String username);

    void setWindowCard(String name);

    void endTurn(String username);

    void moveDiceFromDraftToCard(int index, int row, int col);

}

