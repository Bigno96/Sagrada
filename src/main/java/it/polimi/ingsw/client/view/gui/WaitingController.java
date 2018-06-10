package it.polimi.ingsw.client.view.gui;

import javafx.scene.text.Text;

import java.util.List;

public class WaitingController {

    public Text firstPlayer;
    public Text secondPlayer;
    public Text thirdPlayer;
    public Text fourthPlayer;

    List<String> players;

    //set players

    public WaitingController(GuiSystem guiSystem){
        while(true) {
            /*if(new PlayerLogged){
                players.add(newPlayerLogged.getUsername());
              }
             */
            if(guiSystem.chooseCard()) {
                break;
            }
        }
        guiSystem.chooseCard();
    }

}
