package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
    public TextField usernameText1;

    private GuiSystem guiSystem;
    private List<WindowCard> cards;

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

    }

    @Override
    public void print(String message) {

        usernameText1.setText(message);

    }

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
    }

}
