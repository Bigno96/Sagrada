package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.client.view.gui.GuiSystem;
import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.*;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.stage.Stage;
import java.util.concurrent.*;

import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.System.*;

public class ClientMain extends Application {

    private ViewInterface graphic;

    @Override
    public void start(Stage primaryStage) {

    }

    private ClientMain() {

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

            switch (s) {
                case "d":
                case "g":        // gui graphic chosen
                    Platform.runLater(() -> {
                        graphic = new GuiSystem();
                        Stage window = new Stage();
                        try {
                            ((GuiSystem) graphic).start(window);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            graphic.startGraphic();
                        } catch (FileNotFoundException | ValueException | PositionException | IDNotFoundException e) {
                            e.printStackTrace();
                        }

                    });
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    out.println("GUI graphic chosen");
                    break;
                case "c":                  // cli graphic chosen
                    graphic = new CliSystem();
                    out.println("CLI graphic chosen");
                    break;
                default:                                     // wrong typing
                    out.println("Incorrect entry");
                    break;
            }

        } while (graphic == null);
    }

}
