package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.game.Board;
import it.polimi.ingsw.server.model.objectivecard.card.PrivateObjective;
import it.polimi.ingsw.server.model.objectivecard.card.PublicObjective;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BoardController {

    String baseURL = "/img/";

    @FXML
    public ImageView myImage;

    private WindowCard myCard;
    private List<WindowCard> listOtherWindowCards;
    private List<ToolCard> listToolCards;
    private List<PublicObjective> publList;
    private List<PrivateObjective> privList;
    List<Dice> Draft;
    GuiSystem guiSystem;

    BoardController(GuiSystem guiSystem){
        this.guiSystem = guiSystem;

    }

    void setMyWindowCard(WindowCard myCard){
        this.myCard = myCard;

    }

    void setCardOtherPlayerList(List<WindowCard> listOtherWindowCards){
        this.listOtherWindowCards = listOtherWindowCards;

    }

    void setPrivCard(List<PrivateObjective> privList){
        this.privList = privList;

    }

    void setPublCard(List<PublicObjective> publList){
        this.publList = publList;

    }

    void setToolCard(List<ToolCard> listToolCards){
        this.listToolCards = listToolCards;

    }

    void init(){

        URL urlMyWindow = null;
        try {
            urlMyWindow = new URL(String.format("%s%s", baseURL, "/WindowCard/", myCard.getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image myWindow = new Image(String.valueOf(urlMyWindow));

        myImage.setImage(myWindow);
        
    }
}
