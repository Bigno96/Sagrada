package it.polimi.ingsw.client.network;

import it.polimi.ingsw.server.network.ServerRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import static java.lang.System.*;

public class RmiClientHandler implements ClientHandler, ClientRemote {

    private String ip;
    private ServerRemote stub;
    private boolean logged;
    private Scanner inKeyboard;

    public RmiClientHandler(String ip) {
        this.logged = false;
        this.ip = ip;
    }

    @Override
    public boolean connect(String user) {
        out.println("Trying to connect");
        out.println(ip);

        try {
            inKeyboard = new Scanner(in);

            ClientRemote remote = (ClientRemote) UnicastRemoteObject.exportObject(this, 4000);
            Registry registry = LocateRegistry.createRegistry(4000);
            registry.bind("Client_Interface", remote);

            registry = LocateRegistry.getRegistry(ip, 4500);
            stub = (ServerRemote) registry.lookup("Server_Interface");

            stub.login(user);

            return true;

        } catch (RemoteException | NotBoundException | AlreadyBoundException e) {
            out.print(e.getMessage());
            return false;
        }
    }

    @Override
    public void listen() {
        do {
            logged = true;
            out.println("\nInsert Command: \n [press 'x' to exit]");

            String inputLine = inKeyboard.nextLine();

            if (inputLine.equals("x")) {
                out.println("Closing connection");
                logged = false;
            }

        } while (logged);

    }

    @Override
    public void disconnect(String user) {
        try {
            stub.logout(user);
        } catch (RemoteException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public boolean isLogged() {
        return true;
    }

    @Override
    public void tooManyPlayersError() {
        out.println("Logged failed, too many players connected");
    }

    @Override
    public void welcome() {
        out.println("Connection established");
        out.println("Welcome!");
    }
}
