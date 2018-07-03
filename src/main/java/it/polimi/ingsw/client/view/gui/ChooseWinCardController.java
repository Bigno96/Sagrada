package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.lang.System.out;

public class ChooseWinCardController implements ControlInterface {

    String baseURL = "/img/WindowCard/";
    @FXML
    public ImageView img0;
    @FXML
    public ImageView img1;
    @FXML
    public ImageView img2;
    @FXML
    public ImageView img3;
    @FXML
    public TextArea textArea;

    private GuiSystem guiSystem;
  /*  private List<WindowCard> cards;

    public void setList(List<WindowCard> list){
        cards = list;

        URL url0 = null;
        try {
            url0 = new URL(String.format("%s%s", baseURL, list.get(0).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        
        Image window0 = new Image(String.valueOf(url0));

        img0.setImage(window0);

        URL url1 = null;
        try {
            url1 = new URL(String.format("%s%s", baseURL, list.get(1).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image window1 = new Image(String.valueOf(url1));

        img1.setImage(window1);

        URL url2 = null;
        try {
            url2 = new URL(String.format("%s%s", baseURL, list.get(2).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image window2 = new Image(String.valueOf(url2));

        img2.setImage(window2);

        URL url3 = null;
        try {
            url3 = new URL(String.format("%s%s", baseURL, list.get(3).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image window3 = new Image(String.valueOf(url3));

        img3.setImage(window3);
    }

    ChooseWinCardController(){

    }

    public void wind0(){

        guiSystem.setWindowCard(cards.get(0));
        guiSystem.inizializeBoard();
    }

    public void wind1(){

        guiSystem.setWindowCard(cards.get(1));
        guiSystem.inizializeBoard();
    }

    public void wind2(){

        guiSystem.setWindowCard(cards.get(2));
        guiSystem.inizializeBoard();
    }

    public void wind3(){

        guiSystem.setWindowCard(cards.get(3));
        guiSystem.inizializeBoard();

    }*/

    @Override
    public void print(String s) {

        this.textArea.appendText("\n"+s);

    }

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
    }

    @Override
    public void setList(List<WindowCard> cards) {

        //Image firstWindow = new Image( "@Aurora Sagratis.png" );
/*
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResource("@../img/Sagrada-Logo-RGB.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        //Image firstWindow = new Image( "/img/WindowCard/Aurora Sagradis.png" );

        //Image firstWindow = new Image("@../img/Sagrada-Logo-RGB.jpg");

        out.println("/img/WindowCard/" + cards.get(0).getName().replace("X","") + ".png");

        String url = "/img/WindowCard/" + cards.get(0).getName() + ".png";
        String url2 = cards.get(0).getName();

        out.println(url);
        out.println("\n");
        out.println(url2);

        Image firstWindow = new Image("/img/WindowCard/" + cards.get(0).getName() + ".png");

        img0.setImage(firstWindow);

    }

}
