package it.polimi.ingsw.client.network;

public interface ServerSpeaker {
    // interface to hide network difference in comm Client -> Server
    void setIp(String ip);
    boolean connect(String user);
}

