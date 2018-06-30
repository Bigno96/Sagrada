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


    public void setFirstPlayer(Text firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setSecondPlayer(Text secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public Text getThirdPlayer() {
        return thirdPlayer;
    }

    public Text getFourthPlayer() {
        return fourthPlayer;
    }

    public WaitingController(GuiSystem guiSystem){
        while(true) {
            /*if(new PlayerLogged){
                players.add(newPlayerLogged.getUsername());
              }
             */
           // if(guiSystem.chooseCard()) {
                break;
           // }
        }
 //       guiSystem.chooseCard();
    }

    //Wait Start game
}
