package it.polimi.ingsw.parser;

import it.polimi.ingsw.parser.gamedataparser.PrivateObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.PublicObjectiveCardParser;
import it.polimi.ingsw.parser.gamedataparser.ToolCardParser;
import it.polimi.ingsw.parser.gamedataparser.WindowParser;
import it.polimi.ingsw.parser.messageparser.CommunicationParser;
import it.polimi.ingsw.parser.messageparser.GameSettingsParser;
import it.polimi.ingsw.parser.messageparser.NetworkInfoParser;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;

public class ParserManager {

    private static final String SYSTEM_DIR = System.getProperty("user.dir");
    private static final String WINDOW_CARD_PATH = SYSTEM_DIR + "/src/main/resources/Json/WindowCard.json";
    private static final String PRIVATE_CARD_PATH = SYSTEM_DIR + "/src/main/resources/Json/PrivateCard.json";
    private static final String PUBLIC_CARD_PATH = SYSTEM_DIR + "/src/main/resources/Json/PublicCard.json";
    private static final String TOOL_CARD_PATH = SYSTEM_DIR + "/src/main/resources/Json/ToolCard.json";
    private static final String COMMUNICATION_PROTOCOL_PATH = SYSTEM_DIR + "/src/main/resources/Json/CommunicationProtocol.json";
    private static final String GAME_SETTINGS_PATH = SYSTEM_DIR + "/src/main/resources/Json/GameSettings.json";
    private static final String NETWORK_INFO_PATH = SYSTEM_DIR + "/src/main/resources/Json/NetworkInfo.json";
    private static final String VIEW_DICTIONARY_PATH = SYSTEM_DIR + "/src/main/resources/Json/ViewDictionary.json";

    private ParserManager() {
        //create just the instance
    }

    public static Parser getWindowCardParser() {
        return new WindowParser(WINDOW_CARD_PATH);
    }

    public static Parser getPublicCardParser() {
        return new PublicObjectiveCardParser(PUBLIC_CARD_PATH);
    }

    public static Parser getPrivateCardParser() {
        return new PrivateObjectiveCardParser(PRIVATE_CARD_PATH);
    }

    public static Parser getToolCardParser() {
        return new ToolCardParser(TOOL_CARD_PATH);
    }

    public static Parser getCommunicationParser() {
        return new CommunicationParser(COMMUNICATION_PROTOCOL_PATH);
    }

    public static Parser getGameSettingsParser() {
        return new GameSettingsParser(GAME_SETTINGS_PATH);
    }

    public static Parser getNetworkInfoParser() {
        return new NetworkInfoParser(NETWORK_INFO_PATH);
    }

    public static Parser getViewMessageParser() {
        return new ViewMessageParser(VIEW_DICTIONARY_PATH);
    }
}