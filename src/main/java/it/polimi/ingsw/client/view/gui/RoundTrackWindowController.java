package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class RoundTrackWindowController {

    @FXML
    public GridPane gridPane;
    private String diceURL = "/img/Dices/";
    private String exp = ".png";
    private ControlInterface ctrl;
    private RoundTrackWindow roundTrackWindow;
    private BoardController boardController;

    void setRoundTrack(BoardController boardController, RoundTrack roundTrack, ControlInterface ctrl, RoundTrackWindow roundTrackWindow) {

        this.boardController = boardController;
        this.roundTrackWindow = roundTrackWindow;
        this.ctrl = ctrl;

        Platform.runLater(() -> {

            roundTrack.getTrackList().forEach(listDiceRound -> {
                int index = roundTrack.getTrackList().indexOf(listDiceRound);

                for(int j = 0; j < roundTrack.getTrackList().get(index).getListOfDice().size(); j++){

                    Image imageDice = new Image(diceURL + roundTrack.getTrackList().get(index).getListOfDice().get(j).getColor() + "-" + roundTrack.getTrackList().get(index).getListOfDice().get(j).getValue() + exp);
                    Rectangle rectangle = new Rectangle(30, 30);
                    rectangle.setFill(new ImagePattern(imageDice));

                    rectangle.setId(Integer.toString(index*10 + j));
                    rectangle.setOnMouseClicked((MouseEvent)->{
                        ctrl.setDiceFromDraft((Integer.parseInt(rectangle.getId()))/10, (Integer.parseInt(rectangle.getId()))%10);
                        roundTrackWindow.closeWindow();

                    });

                    gridPane.add(rectangle,index,j);

                }

            });

        });

    }

    public void id(MouseEvent mouseEvent) {

        ctrl.setDiceFromDraft(GridPane.getColumnIndex((Pane)mouseEvent.getSource()), GridPane.getRowIndex((Pane)mouseEvent.getSource()));
        boardController.setCoordinatesRoundTrackDice(GridPane.getColumnIndex((Pane)mouseEvent.getSource()), GridPane.getRowIndex((Pane)mouseEvent.getSource()));
        roundTrackWindow.closeWindow();

    }
}
