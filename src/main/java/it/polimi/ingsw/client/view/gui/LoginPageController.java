package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.network.ServerSpeaker;
import it.polimi.ingsw.client.network.rmi.RmiServerSpeaker;
import it.polimi.ingsw.client.network.socket.SocketServerSpeaker;
import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.List;

import static org.fusesource.jansi.AnsiConsole.out;

public class LoginPageController implements ControlInterface {

    private static final String SAME_USERNAME = "INSERT_NAME_AGAIN";
    private static final String WRONG_IP_KEY = "WRONG_IP";
    private static final String NO_SERVER = "SERVER_NOT_RESPONDING";

    @FXML
    public TextField usernameText;
    @FXML
    public TextField ipText;
    @FXML
    public RadioButton socket;
    @FXML
    public RadioButton rmi;
    @FXML
    public Button submit;
    @FXML
    public TextArea textArea;

    private GuiSystem guiSystem;
    private ViewMessageParser dictionary;

    private ServerSpeaker serverSpeaker;
    private final HashMap<String, ServerSpeaker> connParam;

    public LoginPageController(){
        boolean socketConnection = false;
        boolean rmiConnection = false;
        this.connParam = new HashMap<>();
        dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
    }

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
    }


    public void setList(List<WindowCard> cards) {

    }


    @Override
    public void printDraft(List<Dice> draft) {

    }

    @Override
    public void printPrivateObj(ObjectiveCard privObj) {

    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {

    }

    @Override
    public void printListPublObj(List<ObjectiveCard> publObj) {

    }

    @Override
    public void updateCard(List<WindowCard> windowCards, WindowCard window) {

    }

    @Override
    public void updateRoundTrack(RoundTrack roundTrack) {

    }

    @Override
    public void favorPoints(int point) {

    }

    @Override
    public void setDiceFromDraft(Integer columnIndex, Integer rowIndex) {

    }

    @Override
    public void successfulPlacementDice(String username, Cell dest, Dice moved) {

    }

    @Override
    public void isMyTurn(Boolean turnBoolean) {

    }

    private boolean validIP(String ip) {
        if (ip.isEmpty())
            return false;

        String[] parts = ip.split("\\.");

        if (parts.length != 4 && parts.length != 6)
            return false;

        for (String s : parts) {
            int i = Integer.parseInt(s);
            if (i < 0 || i > 255)
                return false;
        }

        return !ip.endsWith(".");
    }

    /**
     * If connection has been successful change Stage
     * LoginPage -> WaitingPage
     */
    public void submitAction() {
        startConnection();

        guiSystem.setUsername(usernameText.getText());
        guiSystem.setServerSpeaker(serverSpeaker);

    }

    /**
     * Choice of connection, constructor serverSpeaker
     */
    private void startConnection() {

        String username = usernameText.getText();
        if (socket.isSelected()) {
            //if rmi RadioButton is selected return HashMap(username, ServerSpeaker)
            serverSpeaker = new SocketServerSpeaker(ipText.getText(), guiSystem);
        } else {
            serverSpeaker = new RmiServerSpeaker(ipText.getText(), usernameText.getText(), guiSystem);
        }

            if (validIP(ipText.getText()) && usernameText.getText().isEmpty()) {

                if (!serverSpeaker.connect(username)) {

                    textArea.setText(dictionary.getMessage(NO_SERVER));
                    ipText.setText("");

                    return;
                }

                if (!serverSpeaker.login(username)) {

                    usernameText.setText("");
                    textArea.setText(dictionary.getMessage(SAME_USERNAME));

                    return;

                }

            }else{
                    if(usernameText.getText().isEmpty()){

                        textArea.setText("Inserire un nome utente valido");

                    }else {
                        textArea.setText(dictionary.getMessage(WRONG_IP_KEY));

                        ipText.setText("");
                    }
                    return;
            }

            guiSystem.setUsername(username);
            connParam.put(username, serverSpeaker);
            guiSystem.setConnParam(connParam);
            guiSystem.waitingPage();

    }

    public void print(String s) {

        this.textArea.appendText("\n"+s);

    }
}