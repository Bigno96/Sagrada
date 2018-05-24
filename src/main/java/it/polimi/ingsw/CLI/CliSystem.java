package it.polimi.ingsw.CLI;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;


public class CliSystem {

    public static void main(String[] args) {

        System.out.println(ansi().eraseScreen().fg(RED).a("Hello").fg(GREEN).a(" World").reset());

    }

}

