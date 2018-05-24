package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.viewInterface;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import static java.lang.System.*;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;


public class CliSystem implements viewInterface {

    public CliSystem(){}

    @Override
    public void print(String s) {
        out.println(s);
    }

    @Override
    public void printWindowCard(WindowCard window) throws IDNotFoundException {
        Cell c;
        for (int i=0; i<window.getWindow().getRows(); i++) {
            for (int j = 0; j < window.getWindow().getCols(); j++) {
                c = window.getWindow().getCell(i, j);
                if (c.isOccupied())
                    out.print(ansi().eraseScreen().bg(Color.valueOf(c.getDice().getColor().toString())).fg(BLACK).a(c.getDice().getValue()).reset() + "\t");
                else
                    out.print(ansi().eraseScreen().fg(Color.valueOf(c.getColor().toString())).a(c.getValue()).reset() + "\t");
            }
            out.println("\n");
        }
    }
}

