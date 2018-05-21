package it.polimi.ingsw.server.network;

import static java.lang.System.*;

public class ClientRmiHandler implements Runnable, RmiInterface {

    public ClientRmiHandler() {

    }

    @Override
    public void run() {

    }

    @Override
    public void login(String usr) {
        out.println(usr + " is logging in.");
    }

    @Override
    public void logout(String usr) {
        out.println(usr + " is logging out.");
    }

}
