package it.polimi.ingsw.client.network;

public interface Handler {

    void connect(String user);
    void listen();
    void disconnect(String user);
}

