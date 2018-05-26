package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.client.view.viewInterface;
import it.polimi.ingsw.exception.SamePlayerException;

import java.util.Scanner;

import static java.lang.System.*;

public class ClientMain {

    private String userName;
    private static Scanner inKeyboard;
    private viewInterface graphic;


    private ClientMain(String userName) {
        this.userName = userName;
    }

    public static void main(String[] args) {
        inKeyboard = new Scanner(in);
        out.println("Insert your user Name");           // ask name of the user

        ClientMain c = new ClientMain(inKeyboard.nextLine());
        c.startClient();
    }

    private void startClient() {
        out.println("Client is working");
        askGraphic();
        graphic.startGraphic();
    }

    /**
     * Ask for type of Graphic to use
     */
    private void askGraphic() {
        do {
            out.println("Choose your type of graphic \n 'c' for CLI \n 'g' for GUI \n 'd' for Default");

            Scanner input = new Scanner(System.in);
            String s = input.nextLine();

            if (s.equals("g") || s.equals("d")) {       // gui graphic chosen
                //graphic = new GuiSystem();
                out.println("GUI graphic chosen");
            } else if (s.equals("c")) {                 // cli graphic chosen
               graphic = new CliSystem(userName);
               out.println("CLI graphic chosen");
            } else {                                    // wrong typing
                out.println("Incorrect entry");
            }

        } while (graphic == null);
    }

}
