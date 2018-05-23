package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.network.ClientHandler;
import it.polimi.ingsw.server.network.rmi.ServerRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.System.*;

public class RmiClientHandler implements ClientHandler {
    // realize the comm Client -> Server using rmi
    private String ip;
    private ServerRemote server;            // server remote interface
    private ClientRemote client;            // client remote interface passed to server

    public RmiClientHandler(String ip, String username) {
        this.ip = ip;
        try {
            this.client = new ClientRemoteImpl(username);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Tries to connect to the server
     * @param username name chosen by user
     * @return true if connection succeed, else false
     */
    @Override
    public boolean connect(String username) {
        out.println("Trying to connect to " + ip);

        try {
            Registry registry = LocateRegistry.getRegistry(ip, 4500);
            server = (ServerRemote) registry.lookup("Server_Interface");        // find remote interface

            server.login(username, client);                                             // logs to server
            server.tell("User " + username + " successfully logged in");            // tells logged successfully

            return true;

        } catch (RemoteException | NotBoundException e) {
            out.print(e.getMessage());
            return false;
        }
    }
}
