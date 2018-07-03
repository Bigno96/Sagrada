package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class ChooseWinCardController implements ControlInterface {

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

    private List<WindowCard> cards;

    private GuiSystem guiSystem;

    ChooseWinCardController(){

    }

    public void choose0() {

        guiSystem.inizializeBoard();
        guiSystem.getServerSpeaker().setWindowCard(guiSystem.getUserName(), cards.get(0).getName());

    }

    public void choose1(){

        guiSystem.inizializeBoard();
        guiSystem.getServerSpeaker().setWindowCard(guiSystem.getUserName(), cards.get(1).getName());

    }

    public void choose2(){

        guiSystem.inizializeBoard();
        guiSystem.getServerSpeaker().setWindowCard(guiSystem.getUserName(), cards.get(2).getName());


    }

    public void choose3(){

        guiSystem.inizializeBoard();
        guiSystem.getServerSpeaker().setWindowCard(guiSystem.getUserName(), cards.get(3).getName());
        
    }

    public void print(String s) {

        this.textArea.appendText("\n"+s);

    }

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
    }

    public void setList(List<WindowCard> cards) {

        this.cards = cards;

        String baseURL = "/img/WindowCard/";
        String exp = ".png";
        Image firstWindow = new Image(baseURL + cards.get(0).getName() + exp);
        img0.setImage(firstWindow);

        Image secondWindow = new Image(baseURL + cards.get(1).getName() + exp);
        img1.setImage(secondWindow);

        Image thirdWindow = new Image(baseURL + cards.get(2).getName() + exp);
        img2.setImage(thirdWindow);

        Image fourthWindow = new Image(baseURL + cards.get(3).getName() + exp);
        img3.setImage(fourthWindow);

    }


}
