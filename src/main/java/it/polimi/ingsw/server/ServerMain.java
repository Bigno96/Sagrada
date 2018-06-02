package it.polimi.ingsw.server;

import it.polimi.ingsw.parser.ParserFactory;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.parser.CommunicationParser;
import it.polimi.ingsw.parser.NetworkInfoParser;
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
            NetworkInfoParser network = (NetworkInfoParser) ParserFactory.getNetworkInfoParser();
            CommunicationParser communication = (CommunicationParser) ParserFactory.getCommunicationParser();
            String ip = "";

            // find rmi ip address over the local network
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();

                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.toString().equals(network.getLocalIp())) {
                        ip = i.toString();
                        break;
                    }
                }
            }

            Lobby lobby = new Lobby();
            lobby.startLobby();

            ServerSocketThreadLauncher listener = new ServerSocketThreadLauncher(network.getSocketPort(), lobby);         // create the listener for socket connection
            System.setProperty("java.rmi.server.hostname", ip);

            ServerRemote server = new ServerRemoteImpl(lobby);                                       // export to port 4500 rmi remote server interface
            ServerRemote remote = (ServerRemote) UnicastRemoteObject.exportObject(server, network.getRmiServerPort());
            Registry registry = LocateRegistry.createRegistry(network.getRmiServerPort());
            registry.bind(communication.getMessage("SERVER_REMOTE"), remote);

            out.println("Server is up");

            listener.serverListening();             // start listening for socket connection

        } catch (IOException | AlreadyBoundException e) {
            out.println(e.getMessage());
        }
    }
}