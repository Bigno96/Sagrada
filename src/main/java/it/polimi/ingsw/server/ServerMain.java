package it.polimi.ingsw.server;

import it.polimi.ingsw.server.network.RmiInterface;
import it.polimi.ingsw.server.network.ClientRmiHandler;
import it.polimi.ingsw.server.network.ClientSocketHandler;

import java.net.*;
import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;

import static java.lang.System.*;

public class ServerMain {
    private int id;
    private int portSocket;
    private int portRmi;
    private ServerSocket serverSocket;
    private ClientRmiHandler rmiSkeleton;

    public static void main(String[] args) {
        ServerMain server = new ServerMain(4912, 4500);
        server.startServer();
    }

    private ServerMain(int portSocket, int portRmi) {
        rmiSkeleton = new ClientRmiHandler();
        this.id = 0;
        this.portSocket = portSocket;
        this.portRmi = portRmi;
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(portSocket);

            RmiInterface skeleton = (RmiInterface) UnicastRemoteObject.exportObject(rmiSkeleton, portRmi);
            Registry registry = LocateRegistry.createRegistry(portRmi);
            registry.bind("Rmi_Interface", skeleton);

        } catch (IOException | AlreadyBoundException e) {
            out.println(e.getMessage());
        }

        out.println("Server ready");

        serverListening();
    }

    private void serverListening() {
        Boolean exit = true;
        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            while (exit) {
                    Socket socket = serverSocket.accept();
                    id++;
                    executor.submit(new ClientSocketHandler(socket, id));

                    out.println("New Users Registered");
            }
        } catch (IOException e) {
            out.println(e.getMessage());
        }
    }

}