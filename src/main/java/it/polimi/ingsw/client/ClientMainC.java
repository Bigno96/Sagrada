package it.polimi.ingsw.client;

import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.client.view.gui.GuiLogin;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.scene.control.*;
import it.polimi.ingsw.client.view.cli.CliSystem;
import javafx.scene.control.Label;
import javafx.stage.*;
import javafx.scene.*;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.swing.*;
import java.awt.Button;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.scene.layout.AnchorPane;

public class ClientMainC {

    Button buttonCLI;
    Button buttonGUI;
    GuiLogin loginWindow;

    private ViewInterface graphic;
/*
    @FXML private AnchorPane ap;
    Stage stage = (Stage) ap.getScene().getWindow();
*/
    /*public void chooseGUI(javafx.event.ActionEvent event){
     /*   //loginWindow = new GuiLogin(event.getSource().getScene().getWindow());
        Platform.runLater(() -> {
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/LoginPage.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("How do you wanna play?");
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        });*/




    public void chooseGUI(){
    }

    public void chooseCLI(javafx.event.ActionEvent event) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

       // ((Stage)(((Button)event.getSource()).getScene().getWindow())).hide();
        graphic = new CliSystem();
        System.out.println("CLI graphic chosen");
        graphic.startGraphic();

    }

}
