package it.polimi.ingsw.server.network;

public interface ServerHandler {

    void login(String user);
    void logout(String user);
}
