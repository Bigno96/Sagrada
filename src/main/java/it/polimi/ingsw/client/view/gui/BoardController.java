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
    @FXML
    public ImageView wind0;
    @FXML
    public ImageView wind1;
    @FXML
    public ImageView wind2;
    @FXML
    public ImageView imageTool0;
    @FXML
    public ImageView imageTool1;
    @FXML
    public ImageView imageTool2;
    @FXML
    public ImageView imagePubl0;
    @FXML
    public ImageView imagePubl1;
    @FXML
    public ImageView imagePubl2;
    @FXML
    public ImageView imagePriv0;
    @FXML
    public ImageView imagePriv1;
    @FXML
    public ImageView imagePriv2;


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

        URL urlWindow = null;
        try {
            urlWindow = new URL(String.format("%s%s", baseURL, "/WindowCard/", listOtherWindowCards.get(0).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image window0 = new Image(String.valueOf(urlWindow));

        wind0.setImage(window0);

        if(listOtherWindowCards.size() > 2){
            URL urlWindow2 = null;
            try {
                urlWindow2 = new URL(String.format("%s%s", baseURL, "/WindowCard/", listOtherWindowCards.get(1).getName()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Image window1 = new Image(String.valueOf(urlWindow2));

            wind1.setImage(window1);

            if(listOtherWindowCards.size() > 3){
                URL urlWindow3 = null;
                try {
                    urlWindow3 = new URL(String.format("%s%s", baseURL, "/WindowCard/", listOtherWindowCards.get(2).getName()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Image window2 = new Image(String.valueOf(urlWindow3));

                wind2.setImage(window2);

            }

        }

        URL urlTool0 = null;
        try {
            urlTool0 = new URL(String.format("%s%s", baseURL, "/ToolCard/", listToolCards.get(0).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image tool0 = new Image(String.valueOf(urlTool0));

        imageTool0.setImage(tool0);

        URL urlTool1 = null;
        try {
            urlTool1 = new URL(String.format("%s%s", baseURL, "/ToolCard/", listToolCards.get(1).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image tool1 = new Image(String.valueOf(urlTool1));

        imageTool1.setImage(tool1);

        URL urlTool2 = null;
        try {
            urlTool2 = new URL(String.format("%s%s", baseURL, "/ToolCard/", listToolCards.get(2).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image tool2 = new Image(String.valueOf(urlTool2));

        imageTool2.setImage(tool2);

        URL urlPublCard0 = null;
        try {
            urlPublCard0 = new URL(String.format("%s%s", baseURL, "/Public Objective/", publList.get(0).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image publ0 = new Image(String.valueOf(urlPublCard0));

        imagePubl0.setImage(publ0);

        URL urlPublCard1 = null;
        try {
            urlPublCard1 = new URL(String.format("%s%s", baseURL, "/Public Objective/", publList.get(1).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image publ1 = new Image(String.valueOf(urlPublCard1));

        imagePubl1.setImage(publ1);

        URL urlPublCard2 = null;
        try {
            urlPublCard2 = new URL(String.format("%s%s", baseURL, "/Public Objective/", publList.get(2).getName()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Image publ2 = new Image(String.valueOf(urlPublCard2));

        imagePubl2.setImage(publ2);

    }
}
