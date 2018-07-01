package it.polimi.ingsw.client.network.rmi;

import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.dicebag.Draft;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.atomic.AtomicReference;

public class ClientRemoteImpl extends UnicastRemoteObject implements ClientRemote {

    private String username;
    private final AtomicReference<ViewInterface> view = new AtomicReference<>();

    public ClientRemoteImpl(String username, ViewInterface view) throws RemoteException {
        super();
        this.view.set(view);
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @param s to be printed
     */
    @Override
    public void tell(String s) {
        view.get().print(s);
    }

    /**
     * @return true
     */
    @Override
    public boolean ping() {
        return true;
    }

    /**
     * @return this username
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * @param cards cards.size() = 4
     */
    @Override
    public void chooseWindowCard(List<WindowCard> cards) {
        view.get().chooseWindowCard(cards);
    }

    /**
     * @param user = Player.getId()
     * @param card = Player.getWindowCard()
     */
    @Override
    public void showCardPlayer(String user, WindowCard card) {
        view.get().showCardPlayer(user, card);
    }

    /**
     * @param user = game.getCurrentPlayer()
     */
    @Override
    public void nextTurn(String user) {
        view.get().isTurn(user);
    }

    /**
     * @param username of player moving the dice
     * @param dest     cell where the dice is being moved
     * @param moved    dice being moved
     */
    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {
        view.get().successfulPlacementDice(username, dest, moved);
    }

    /**
     * @param card to print
     */
    @Override
    public void printWindowCard(WindowCard card) {
        view.get().printWindowCard(card);
    }

    /**
     * @param draft draft.size() > 0 && draft.size() <= game.getNumberPlayer() * 2 +1
     */
    @Override
    public void showDraft(Draft draft) {
        view.get().showDraft(draft.getDraftList());
    }

    /**
     * @param publicObj publicObj.size() = 3
     */
    @Override
    public void printPublicObj(List<ObjectiveCard> publicObj) {
        view.get().printPublicObj(publicObj);
    }

    /**
     * @param privateObj = Player.getPrivateObj()
     */
    @Override
    public void printPrivateObj(ObjectiveCard privateObj) {
        view.get().printPrivateObj(privateObj);
    }

    /**
     * @param ranking sorted map of player username and their points through the game
     */
    @Override
    public void printRanking(SortedMap<Integer, String> ranking) {
        view.get().printRanking(ranking);
    }
}
