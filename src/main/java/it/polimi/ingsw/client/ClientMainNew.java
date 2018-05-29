package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.client.view.cli.CliSystem;
import it.polimi.ingsw.client.view.gui.GuiAskConnection;
import it.polimi.ingsw.client.view.gui.GuiSystem;
import it.polimi.ingsw.client.view.gui.IncorrectIP;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class ClientMainNew extends Application {

    private ViewInterface graphic;
    private ChooseGuiCli choose;

    @Override
    public void start(Stage primaryStage) {

    }

    public static void main(String[] args) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        ClientMainNew c = new ClientMainNew();
        c.askGuiCli();
        c.startClient();
    }

    private void startClient() throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {
        out.println("Client is working");
        //askGraphic();
        graphic.startGraphic();
    }

    private void startGrafic(){
        if (choose.isChooseCli()){
            graphic = new CliSystem();
            out.println("CLI graphic chosen");
        }else{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                out.println(e.getMessage());
            }
            Platform.runLater(() -> {
                graphic = new GuiSystem();
                Stage window = new Stage();
                try {
                    ((GuiSystem) graphic).start(window);
                } catch (Exception e) {
                    out.println(e.getMessage());
                }
                try {
                    graphic.startGraphic();
                } catch (FileNotFoundException | ValueException | PositionException | IDNotFoundException e) {
                    out.println(e.getMessage());
                }

            });

            out.println("GUI graphic chosen");
        }
    }

    private void askGuiCli() {
        System.out.println("Mi Blocco qui");
        Platform.runLater(() -> {
            ChooseGuiCli chooseWindow = new ChooseGuiCli();
            Stage window = new Stage();
            try {
                chooseWindow.display(window);
            } catch (Exception e) {
                out.println(e.getMessage());
            }

        });
    }

}


