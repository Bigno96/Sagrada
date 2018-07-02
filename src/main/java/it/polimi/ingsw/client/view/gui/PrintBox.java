package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.parser.ParserManager;
import it.polimi.ingsw.parser.messageparser.ViewMessageParser;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrintBox implements Runnable{

    private static final String SERVER_MESSAGE = "PRINT_BOX_TITLE";

    private static ViewMessageParser dictionary;
    private String message;

    public PrintBox(String message){

        dictionary = (ViewMessageParser) ParserManager.getViewMessageParser();
        this.message = message;
        run();

    }

    @Override
    public void run() {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(SERVER_MESSAGE);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
