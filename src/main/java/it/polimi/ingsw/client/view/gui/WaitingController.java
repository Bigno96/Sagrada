package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.List;

public class WaitingController implements ControlInterface{

    private static final String STARTING_GAME = "GAME_WILL_START";

    @FXML
    public TextArea textArea;

    private GuiSystem guiSystem;

    public void setGuiSystem(GuiSystem guiSystem) {
        this.guiSystem = guiSystem;
        ViewMessageParser dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.textArea.setText(dictionary.getMessage(STARTING_GAME));

    }

    @Override
    public void setList(List<WindowCard> cards) {

    }

    public void print(String s) {

        this.textArea.appendText("\n"+s);

    }

}
