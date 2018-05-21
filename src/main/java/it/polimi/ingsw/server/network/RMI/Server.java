package it.polimi.ingsw.server.network.RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements RmiInterface{

    static int PORT = 1234;

    public Server() {}

    public static void main (String[] args) {
        //port = Integer.parseInt(args[0]);

        try{
            Server centralServer = new Server();
            RmiInterface skeleton = (RmiInterface) UnicastRemoteObject.exportObject(centralServer, PORT);
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind("Rmi_Interface", skeleton);
            System.out.println("Server ready");
        }catch (Exception e){
            System.err.println("Server exception: " + e.toString());
        }
    }

    @Override
    public boolean login(String usr) {
        System.out.println(usr + " is logging in.");
        return true;
    }

    @Override
    public boolean logout(String usr) {
        System.out.println(usr + " is logging out.");
        return false;
    }
}
