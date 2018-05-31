package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.network.rmi.ServerRemote;
import it.polimi.ingsw.server.network.rmi.ServerRemoteImpl;
import it.polimi.ingsw.server.network.socket.ServerSocketThreadLauncher;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

import static java.lang.System.*;

public class ServerMain {

    public static void main(String[] args) {
        ServerMain server = new ServerMain();
        server.startServer();
    }

    private ServerMain() { }

    private void startServer() {
        try {
            String ip = "";

            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();

                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.toString().equals("192.168.x.x")) {
                        ip = i.toString();
                        break;
                    }
                }
            }

            Lobby lobby = new Lobby();
            lobby.startLobby();

            ServerSocketThreadLauncher listener = new ServerSocketThreadLauncher(5000, lobby);         // create the listener for socket connection
            System.setProperty("java.rmi.server.hostname", ip);

            ServerRemote server = new ServerRemoteImpl(lobby);                                       // export to port 4500 rmi remote server interface
            ServerRemote remote = (ServerRemote) UnicastRemoteObject.exportObject(server, 4500);
            Registry registry = LocateRegistry.createRegistry(4500);
            registry.bind("Server_Interface", remote);

            out.println("Server is up");

            listener.serverListening();             // start listening for socket connection

        } catch (IOException | AlreadyBoundException e) {
            out.println(e.getMessage());
        }
    }
}