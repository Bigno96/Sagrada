package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.List;

import static java.lang.System.out;

public class BoardController implements ControlInterface {

    @FXML
    public ImageView myWind;
    @FXML
    public ImageView wind0;
    @FXML
    public ImageView wind1;
    @FXML
    public ImageView wind2;

    @FXML
    public ImageView publ0;
    @FXML
    public ImageView publ1;
    @FXML
    public ImageView publ2;

    @FXML
    public ImageView tool0;
    @FXML
    public ImageView tool1;
    @FXML
    public ImageView tool2;

    @FXML
    public Pane paneDraft;

    @FXML
    public ImageView priv;

    @FXML
    public GridPane myTabel;
    @FXML
    public GridPane tabel0;
    @FXML
    public GridPane tabel1;
    @FXML
    public GridPane tabel2;
    @FXML
    public TextArea textArea;
    @FXML
    public GridPane draftGrid;
    @FXML
    public Button roundButton;

    private String baseURL = "/img/WindowCard/";
    private String exp = ".png";
    private GuiSystem guiSystem;
    private int indexDiceDraft;
    private int column;
    private int row;

    public void print(String s) {

        this.textArea.appendText("\n" + s);

    }

    @Override
    public void setGuiSystem(GuiSystem guiSystem) {

        this.guiSystem = guiSystem;
        Image myWindowImage = new Image(baseURL + guiSystem.getMyWindowCard().getName() + exp);
        myWind.setImage(myWindowImage);

        //getNodeByRowColumnIndex(4,5,myTabel);

    }
/*
    public void getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }

           node.setOnMousePressed(diceOnMousePressedEventHandler);

        }

    }*/


    @Override
    public void setList(List<WindowCard> cards) {

    }

    @Override
    public void printDraft(List<Dice> draft) {

        String diceURL = "/img/Dices/";

        Platform.runLater(() -> {

            draftGrid.getChildren().retainAll(draftGrid.getChildren().get(0));

            for (int i = 0; draft.size() > i; i++) {

                Image imageDice = new Image(diceURL + draft.get(i).getColor() + "-" + draft.get(i).getValue() + exp);
                Rectangle rectangle = new Rectangle(30, 30);
                rectangle.setFill(new ImagePattern(imageDice));

                row = i%3;
                column = i/3;

                draftGrid.add(rectangle,column,row);

            }

        });
    }

    @Override
    public void printPrivateObj(ObjectiveCard privObj) {

        Image myPrivCard = new Image("/img/Private Objective/" + privObj.getId() + exp);
        priv.setImage(myPrivCard);

        Image windowPlayer2 = new Image(baseURL + guiSystem.getWindowCards().get(0).getName() + exp);
        wind0.setImage(windowPlayer2);

        if(guiSystem.getWindowCards().size() > 1){

            Image windowPlayer3 = new Image(baseURL + guiSystem.getWindowCards().get(1).getName() + exp);
            wind1.setImage(windowPlayer3);

        }

        if(guiSystem.getWindowCards().size() > 2){

            Image windowPlayer4 = new Image(baseURL + guiSystem.getWindowCards().get(2).getName() + exp);
            wind2.setImage(windowPlayer4);

        }

    }

    @Override
    public void printListToolCard(List<ToolCard> toolCards) {

        String baseTool = "/img/ToolCard/";

        Image imageTool0 = new Image(baseTool + toolCards.get(0).getId() + exp);
        tool0.setImage(imageTool0);
        Image imageTool1 = new Image(baseTool + toolCards.get(1).getId() + exp);
        tool1.setImage(imageTool1);
        Image imageTool2 = new Image(baseTool + toolCards.get(2).getId() + exp);
        tool2.setImage(imageTool2);

    }

    @Override
    public void printListPublObj(List<ObjectiveCard> publObj) {

        String basePubl = "/img/Public Objective/";

        Image imagePubl0 = new Image(basePubl + publObj.get(0).getId() + exp);
        publ0.setImage(imagePubl0);
        Image imagePubl1 = new Image(basePubl + publObj.get(1).getId() + exp);
        publ1.setImage(imagePubl1);
        Image imagePubl2 = new Image(basePubl + publObj.get(2).getId() + exp);
        publ2.setImage(imagePubl2);

    }

    @Override
    public void updateCard(List<WindowCard> windowCards, WindowCard window) {

        String diceURL = "/img/Dices/";

        Platform.runLater(() -> {

            if (window.getName().equals(guiSystem.getMyWindowCard().getName())) {

                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 5; j++) {

                        Image imageDice = new Image(diceURL + window.getWindow().getCell(i, j).getColor() + "-" + window.getWindow().getCell(i, j).getValue() + exp);
                        Rectangle rectangle = new Rectangle(30, 30);
                        rectangle.setFill(new ImagePattern(imageDice));

                        myTabel.add(rectangle,j,i);

                    }

                }


            } else {
                int k = 0;
                while (window.getName().equals(windowCards.get(k).getName())) ;
                if (k == 0) {

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++) {

                            Image imageDice = new Image(diceURL + window.getWindow().getCell(i, j).getColor() + "-" + window.getWindow().getCell(i, j).getValue() + exp);
                            Rectangle rectangle = new Rectangle(30, 30);
                            rectangle.setFill(new ImagePattern(imageDice));

                            tabel1.add(rectangle, j, i);

                        }

                    }

                } else if (k == 1) {

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++) {

                            Image imageDice = new Image(diceURL + window.getWindow().getCell(i, j).getColor() + "-" + window.getWindow().getCell(i, j).getValue() + exp);
                            Rectangle rectangle = new Rectangle(30, 30);
                            rectangle.setFill(new ImagePattern(imageDice));

                            tabel1.add(rectangle, j, i);

                        }

                    }

                } else if (k == 2) {

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++) {

                            Image imageDice = new Image(diceURL + window.getWindow().getCell(i, j).getColor() + "-" + window.getWindow().getCell(i, j).getValue() + exp);
                            Rectangle rectangle = new Rectangle(30, 30);
                            rectangle.setFill(new ImagePattern(imageDice));

                            tabel2.add(rectangle, j, i);

                        }
                    }
                }
            }
        });

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
    public void succefulPlacementDice(String username, Cell dest, Dice moved) {

    }

    public void diceOnMousePressedEventHandler(MouseEvent mouseEvent) {

        guiSystem.moveDice(indexDiceDraft,GridPane.getColumnIndex((Pane) mouseEvent.getSource()),GridPane.getRowIndex((Pane) mouseEvent.getSource()));
        out.println("moveDice" + indexDiceDraft + GridPane.getColumnIndex((Pane) mouseEvent.getSource()) + GridPane.getRowIndex((Pane) mouseEvent.getSource()) );

    }

    public void draftSelected(MouseEvent mouseEvent) {

        indexDiceDraft = GridPane.getColumnIndex((Pane)mouseEvent.getSource()) * 3 + GridPane.getRowIndex((Pane)mouseEvent.getSource());

    }


    public void showRoundTrack(MouseEvent mouseEvent) {

        guiSystem.askRoundTrack();
        Platform.runLater(() -> {

            RoundTrackWindow roundTrackWindow = new RoundTrackWindow();
            roundTrackWindow.display(guiSystem.roundTrack, this);

        });

    }

    public void endTurn(MouseEvent mouseEvent){

        guiSystem.endTurn();

    }

}
