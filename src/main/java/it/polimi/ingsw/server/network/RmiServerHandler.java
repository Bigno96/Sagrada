package it.polimi.ingsw.server.network;

import it.polimi.ingsw.client.network.ClientRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static java.lang.System.*;

public class RmiServerHandler implements ServerRemote, ServerHandler {

    private ClientRemote skeleton;

    public RmiServerHandler() {
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
            Registry registry = LocateRegistry.getRegistry(4000);
            skeleton = (ClientRemote) registry.lookup("Client_Interface");

            out.println(user + " is logging in");
            if (skeleton.isLogged()) {
                out.println(user + " successfully logged");
                skeleton.welcome();
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
