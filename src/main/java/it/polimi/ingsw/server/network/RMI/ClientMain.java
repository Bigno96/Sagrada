package it.polimi.ingsw.server.network.RMI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static java.lang.System.in;

public class ClientMain {

    private String ip;
    private static int PORT;
    private boolean logged = false;
    private RmiInterface stub;
    private BufferedReader inKeyboard;

    public ClientMain(String ip, int PORT){
        this.ip = ip;
        this.PORT = PORT;
    }

    public void startClient(){
        System.out.println("ClientMain on");

        try {
            exe();
        }catch(Exception e) {
            System.out.println("Exception: "+e);
            e.printStackTrace();
        }
    }

    private void exe() {
        try {
            connection();
            stamp();
            logged = stub.logout("Bob");
        }catch(Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    private void connection() {
        try {
            inKeyboard = new BufferedReader(new InputStreamReader(in));

            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(ip.toString(), PORT);

            // Looking up the registry for the remote object
            stub = (RmiInterface) registry.lookup("Rmi_Interface");

            // Calling the remote method using the obtained object
            logged = stub.login("Bob");

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private void stamp() {
        while (logged) {
            System.out.println("Hello!");
            System.out.println("q --> exit");

            try {
                String choose = inKeyboard.readLine();
                if (choose.equals("q")) {
                    System.out.println("Close RMI Connection");
                    break;
                } else
                    System.out.println("INPUT Not Found");
            } catch (Exception e) {
                System.out.println("Exception: " + e);
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //int port = Integer.parseInt(args[0]);
        int port = 1234;
        String ipAddr = "127.0.0.1";
        ClientMain c = new ClientMain(ipAddr, port);
        c.startClient();
    }

}
