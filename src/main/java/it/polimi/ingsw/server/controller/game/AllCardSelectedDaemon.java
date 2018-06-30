package it.polimi.ingsw.server.controller.game;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.controller.lobby.Lobby;
import it.polimi.ingsw.server.model.windowcard.WindowCard;

import java.util.Random;
import java.util.TimerTask;

/**
 * Wait until all players select their window card, then release permits to game controller to end game setup.
 * If a player after a predefined time does not choose a window card, one is selected for him randomly.
 */
public class AllCardSelectedDaemon extends TimerTask {

    private static final String USER = "Il giocatore ";
    private static final String HAS_YET = " ha ancora ";
    private static final String NOT_YET_SELECTED_CARD_KEYWORD = "NOT_YET_SELECTED_CARD";
    private static final String EXCEEDED_TIME_KEYWORD = "EXCEEDED_TIME_SELECTING_CARD";

    private Lobby lobby;
    private GameController gameController;

    private final ViewMessageParser dictionary;
    private final GameSettingsParser settings;
    private int count;
    private static final Random random = new Random();

    AllCardSelectedDaemon(Lobby lobby, GameController gameController) {
        this.lobby = lobby;
        this.gameController = gameController;
        this.count = 0;
        this.dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.settings = (GameSettingsParser) ParserManager.getGameSettingsParser();
    }

    @Override
    public void run() {
        if (count < settings.getTimeUntilRandomCard()/settings.getRandomCardNotifyInterval()
                && !gameController.allCardsAreSelected()) {

            lobby.getPlayers().values().forEach(player -> {

                if (player.getWindowCard() == null) {

                    lobby.notifyAllPlayers(USER + player.getId() + HAS_YET
                            + ((settings.getTimeUntilRandomCard()-count*settings.getRandomCardNotifyInterval())/1000)
                            + dictionary.getMessage(NOT_YET_SELECTED_CARD_KEYWORD));
                }
            });

            count++;
        }
        else if (!gameController.allCardsAreSelected()) {

            lobby.getPlayers().values().forEach(player -> {

                if (player.getWindowCard() == null) {

                    lobby.notifyAllPlayers(USER + player.getId() + dictionary.getMessage(EXCEEDED_TIME_KEYWORD));
                    WindowCard chosen = gameController.getWindowAlternatives().get(player.getId()).get(random.nextInt(4));
                    gameController.setWindowCard(player.getId(), chosen.getName());
                }
            });

            synchronized (this) {
                notifyAll();
            }

            this.cancel();
        }
    }
}
