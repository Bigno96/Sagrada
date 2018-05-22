package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.network.ClientRemote;
import it.polimi.ingsw.server.ServerMain;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.System.*;

public class RmiServerHandler implements ServerRemote, ServerHandler {

    private ClientRemote skeleton;
    private ServerMain server;

    public RmiServerHandler(ServerMain server) {
        this.server = server;
        try {
            ServerRemote remote = (ServerRemote) UnicastRemoteObject.exportObject(this, 4500);
            Registry registry = LocateRegistry.createRegistry(4500);
            registry.bind("Server_Interface", remote);

        } catch (RemoteException | AlreadyBoundException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void login(String user) {
        try {
            if (!server.legalConnect()) {
                skeleton.tooManyPlayersError();
                return;
            }

            Registry registry = LocateRegistry.getRegistry(4000);
            skeleton = (ClientRemote) registry.lookup("Client_Interface");

            out.println(user + " is logging in with RMI");
            if (skeleton.isLogged()) {
                out.println(user + " successfully logged");
                skeleton.welcome();
                server.countId();
            }
        } catch (RemoteException | NotBoundException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void logout(String user) {
        out.println(user + " logged out");
    }

}
