package it.polimi.ingsw.server;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
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

    private static final String RMI_HOSTNAME = "java.rmi.server.hostname";
    private static final String SERVER_REMOTE_KEYWORD = "SERVER_REMOTE";
    private static final String SERVER_UP_KEYWORD = "SERVER_UP";

    public static void main(String[] args) {
        ServerMain server = new ServerMain();
        server.startServer();
    }

    private ServerMain() { }

    private void startServer() {
        try {
            NetworkInfoParser network = (NetworkInfoParser) ParserManager.getNetworkInfoParser();
            CommunicationParser communication = (CommunicationParser) ParserManager.getCommunicationParser();
            String ip = "";

            // find rmi ip address over the local network
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();

                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    if (i.toString().replace("/", "").equals(network.getLocalIp())) {
                        ip = i.toString().replace("/", "");
                        break;
                    }
                }
            }

            Lobby lobby = new Lobby();
            lobby.startLobby();

            ServerSocketThreadLauncher listener = new ServerSocketThreadLauncher(network.getSocketPort(), lobby);         // create the listener for socket connection
            System.setProperty(RMI_HOSTNAME, ip);

            ServerRemote server = new ServerRemoteImpl(lobby);                                       // export to port 4500 rmi remote server interface
            ServerRemote remote = (ServerRemote) UnicastRemoteObject.exportObject(server, network.getRmiServerPort());
            Registry registry = LocateRegistry.createRegistry(network.getRmiServerPort());
            registry.bind(communication.getMessage(SERVER_REMOTE_KEYWORD), remote);

            out.println(communication.getMessage(SERVER_UP_KEYWORD));

            listener.serverListening();             // start listening for socket connection

        } catch (IOException | AlreadyBoundException e) {
            out.println(e.getMessage());
        }
    }
}