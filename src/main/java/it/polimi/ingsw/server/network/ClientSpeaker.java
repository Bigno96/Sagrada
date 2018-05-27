package it.polimi.ingsw.server.network;

public interface ClientSpeaker {
    // interface to hide network difference in comm Server -> Client

    /**
     * Used to print on Client
     * @param s to be printed
     */
    void tell(String s);

    /**
     * Check if the client is connected
     * @return true if pong is true in return, else false
     */
    boolean ping();
}
