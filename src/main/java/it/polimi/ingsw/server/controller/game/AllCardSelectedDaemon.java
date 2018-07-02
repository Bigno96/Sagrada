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

    private final String notSelectedYet;
    private final String exceededTime;

    private Lobby lobby;
    private GameController gameController;

    private int count;
    private static final Random random = new Random();

    private final int timeUntilRandomCard;
    private final int notifyInterval;

    AllCardSelectedDaemon(Lobby lobby, GameController gameController) {
        this.lobby = lobby;
        this.gameController = gameController;
        this.count = 0;
        ViewMessageParser dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        GameSettingsParser settings = (GameSettingsParser) ParserManager.getGameSettingsParser();

        this.notSelectedYet = dictionary.getMessage(NOT_YET_SELECTED_CARD_KEYWORD);
        this.exceededTime = dictionary.getMessage(EXCEEDED_TIME_KEYWORD);
        this.timeUntilRandomCard = settings.getTimeUntilRandomCard();
        this.notifyInterval = settings.getNotifyInterval();
    }

    @Override
    public void run() {
        if (count < timeUntilRandomCard/notifyInterval
                && !gameController.allCardsAreSelected()) {

            lobby.getPlayers().values().forEach(player -> {

                if (player.getWindowCard() == null) {

                    lobby.notifyAllPlayers(USER + player.getId() + HAS_YET
                            + ((timeUntilRandomCard-count*notifyInterval)/1000) + notSelectedYet);
                }
            });

            count++;
        }
        else if (!gameController.allCardsAreSelected()) {

            lobby.getPlayers().values().forEach(player -> {

                if (player.getWindowCard() == null) {

                    lobby.notifyAllPlayers(USER + player.getId() + exceededTime);
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
