package it.polimi.ingsw.parser;

public class ParserFactory {

    private static final String SYSTEM_DIR = System.getProperty("user.dir");

    private ParserFactory() {
        //create just the instance
    }

    public static Parser getCommunicationParser() {
        final String PATH = SYSTEM_DIR + "/src/main/resources/Json/CommunicationProtocol.json";
        return new CommunicationParser(PATH);
    }

    public static Parser getGameSettingsParser() {
        final String PATH = SYSTEM_DIR + "/src/main/resources/Json/GameSettings.json";
        return new GameSettingsParser(PATH);
    }

    public static Parser getNetworkInfoParser() {
        final String PATH = SYSTEM_DIR + "/src/main/resources/Json/NetworkInfo.json";
        return new NetworkInfoParser(PATH);
    }

    public static Parser getViewMessageParser() {
        final String PATH = SYSTEM_DIR + "/src/main/resources/Json/ViewDictionary.json";
        return new ViewMessageParser(PATH);
    }
}
