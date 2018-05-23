package it.polimi.ingsw.server;

import it.polimi.ingsw.server.network.rmi.ServerRemote;
import it.polimi.ingsw.server.network.rmi.ServerRemoteImpl;
import it.polimi.ingsw.server.network.socket.ServerSocketListener;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.System.*;

public class ServerMain {

    public static void main(String[] args) {
        ServerMain server = new ServerMain();
        server.startServer();
    }

    private ServerMain() {

    }

    private void startServer() {
        try {
            ServerSocketListener listener = new ServerSocketListener(5000);
            ServerRemote server = new ServerRemoteImpl();

            ServerRemote remote = (ServerRemote) UnicastRemoteObject.exportObject(server, 4500);
            Registry registry = LocateRegistry.createRegistry(4500);
            registry.bind("Server_Interface", remote);

            out.println("Server is up");

            listener.serverListening();

        } catch (IOException | AlreadyBoundException e) {
            out.println(e.getMessage());
        }
    }
}