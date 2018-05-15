package it.polimi.ingsw.network;

import java.net.*;
import java.io.*;
import static java.lang.System.*;
import java.net.Socket;

public class ClientMain {

    private int port;
    private InetAddress ip;
    private Socket socket;
    private BufferedReader inSocket;
    private PrintWriter outSocket;
    private BufferedReader inKeyboard;
    private PrintWriter outVideo;

    public ClientMain(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() {
        out.println("ClientMain on");

        try {
            exe();
        }catch(Exception e) {
            out.println("Exception: "+e);
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch(IOException e) {
                err.println("Socket not closed");
            }
        }
    }

    private void connect() {
        try {

            out.println("ClientMain try to connect");
            socket = new Socket(ip, port);
            inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            inKeyboard = new BufferedReader(new InputStreamReader(in));
            outVideo = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);

            out.println("ClientMain connected");
        }catch(Exception e) {
            out.println("Exception: "+e);
            e.printStackTrace();

            // Always close it:
            try {
                socket.close();
            } catch(IOException ex) {
                err.println("Socket not closed");
            }
        }
    }

    private void exe() {
        try {
            connect();
            stamp();
            close();
        }catch(Exception e) {
            out.println("Exception: "+e);
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch(IOException e) {
                err.println("Socket not closed");
            }
        }
    }

    private void stamp() {
        while (true) {
            outVideo.println("Hello!");
            outVideo.println("q --> exit");

            try {
                String choose = inKeyboard.readLine();
                if (choose.equals("q")) {
                    outSocket.println("EndSocket");
                    outVideo.println("End");
                    break;
                } else
                    outVideo.println("INPUT Not Found");
            } catch (Exception e) {
                out.println("Exception: " + e);
                e.printStackTrace();
            }

            try {
                socket.close();
            } catch (IOException ex) {
                err.println("Socket not closed");
            }
        }
    }


    private void close() {
        try{
            socket.close();
        }catch(Exception e) {
            out.println("Exception: "+e);
            e.printStackTrace();
        } finally{
            try {
                socket.close();
            }catch(IOException ex) {
                err.println("Socket not closed");
            }
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        InetAddress ipAddress = InetAddress.getLocalHost();
        ClientMain c = new ClientMain(ipAddress, 4912);
        c.startClient();
    }

}
