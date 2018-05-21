package it.polimi.ingsw.client.network;

import it.polimi.ingsw.server.network.RmiInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import static java.lang.System.*;

public class RmiHandler implements Handler  {

    private int port;
    private String ip;
    private RmiInterface stub;
    private boolean logged;
    private Scanner inKeyboard;

    public RmiHandler(int port, String ip) {
        this.logged = false;
        this.port = port;
        this.ip = ip;
    }

    @Override
    public void connect(String user) {
        try {
            inKeyboard = new Scanner(in);

            Registry registry = LocateRegistry.getRegistry(ip, port);
            stub = (RmiInterface) registry.lookup("Rmi_Interface");

            stub.login(user);
            out.println("Connection established");

        } catch (RemoteException | NotBoundException e) {
            out.print(e.getMessage());
        }

    }

    @Override
    public void listen() {
        out.println("Welcome! \n [press 'x' to exit]");

        do {
            //logged = stub.login("Bob");     // diventerà is Logged

            out.println("Insert Command: ");

            String inputLine = inKeyboard.nextLine();

            if (inputLine.equals("x")) {
                out.println("Closing connection");
                logged = false;
            }
            else {
                out.println("Calling remote method");
            }

        } while (logged);

    }

    @Override
    public void disconnect(String user) {
        stub.logout(user);
    }
}
