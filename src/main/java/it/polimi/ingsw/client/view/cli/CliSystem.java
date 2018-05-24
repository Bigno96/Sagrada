package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.viewInterface;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;


public class CliSystem implements viewInterface {

    public static void main(String[] args) {

        System.out.println(ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset());

    }

    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public void printWindowCard(WindowCard window) {

    }
}

