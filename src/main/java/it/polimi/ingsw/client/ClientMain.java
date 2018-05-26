package it.polimi.ingsw.client;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.client.view.viewInterface;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.SamePlayerException;
import it.polimi.ingsw.exception.ValueException;

import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.*;

public class ClientMain {


    private static Scanner inKeyboard;
    private viewInterface graphic;


    private ClientMain() {
        inKeyboard = new Scanner(in);
    }

    public static void main(String[] args) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        ClientMain c = new ClientMain();
        c.startClient();
    }

    private void startClient() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
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
               graphic = new CliSystem();
               out.println("CLI graphic chosen");
            } else {                                    // wrong typing
                out.println("Incorrect entry");
            }

        } while (graphic == null);
    }

}
