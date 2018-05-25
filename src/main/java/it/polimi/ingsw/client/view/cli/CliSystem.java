package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.viewInterface;

import java.util.List;

import static java.lang.System.*;


public class CliSystem implements viewInterface {

    public CliSystem(){}

    @Override
    public void print(String s) {
        out.println(s);
    }

    @Override
    public void chooseWindowCard(int[] ids) {

    }

    @Override
    public void sendCardPlayers(int[] ids) {

    }

    @Override
    public void printPrivObj(int id) {

    }

    @Override
    public void printPublObj(int[] ids) {

    }

    @Override
    public void setRound() {

    }

    /*public void printWindowCard(Player player, WindowCard window) throws IDNotFoundException { //param id to choose windowCard in json
        out.println(player.getId());
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
    }*/

    @Override
    public void isTurn (String username){

    }

    @Override
    public void printDraft(List<Integer> draftValue, List<String> draftColor) {

    }

    @Override
    public void placementDice(String username, int row, int col, String color, int value) {

    }

    public void askParameter(){

    }

    public void listenCli(){

    }
}

