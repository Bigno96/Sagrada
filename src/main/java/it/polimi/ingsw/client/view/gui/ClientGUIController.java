package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientMain;

public class ClientGUIController {

    private ClientMain clientMain;


    public void setClientMain(ClientMain clientMain) {
        this.clientMain = clientMain;
    }

    public void chooseGUI(){

        clientMain.openGUI();
    }

    public void chooseCLI() {

        clientMain.openCLI();

    }

}
