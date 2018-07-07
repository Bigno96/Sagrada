package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.dicebag.Dice;
import it.polimi.ingsw.server.model.objectivecard.card.ObjectiveCard;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import it.polimi.ingsw.server.model.toolcard.ToolCard;
import it.polimi.ingsw.server.model.windowcard.Cell;
import it.polimi.ingsw.server.model.windowcard.WindowCard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

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
    @FXML
    public TextField favorPoint;
    @FXML
    public Text user0;
    @FXML
    public Text user1;
    @FXML
    public Text user2;

    private String baseURL = "/img/WindowCard/";
    private String exp = ".png";
    private GuiSystem guiSystem;
    private int indexDiceDraft;
    private int column;
    private int row;
    private int nDices = 0;
    private int nullCheck;
    private int nCells = 0;


    private EnumMap<ToolCard.Actor, Consumer<String>> actorMap;
    private EnumMap<ToolCard.Parameter, Consumer<String>> parameterMap;

    private List<ToolCard.Actor> actor;
    private List<ToolCard.Parameter> parameter;
    private boolean diceValue;

    public void print(String s) {

        this.textArea.appendText("\n" + s);

    }

    @Override
    public void setGuiSystem(GuiSystem guiSystem) {

        this.guiSystem = guiSystem;
        Image myWindowImage = new Image(baseURL + guiSystem.getMyWindowCard().getName() + exp);
        favorPoint.setText(Integer.toString(guiSystem.getMyWindowCard().getNumFavPoint()));
        myWind.setImage(myWindowImage);

    }

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

        String basePubl = "/img/Public Objective/";

        Image imageTool0 = new Image(baseTool + toolCards.get(0).getId() + exp);
        tool0.setImage(imageTool0);
        Image imageTool1 = new Image(baseTool + toolCards.get(1).getId() + exp);
        tool1.setImage(imageTool1);
        Image imageTool2 = new Image(baseTool + toolCards.get(2).getId() + exp);
        tool2.setImage(imageTool2);

        Image imagePubl0 = new Image(basePubl + guiSystem.getPulicCards().get(0).getId() + exp);
        publ0.setImage(imagePubl0);
        Image imagePubl1 = new Image(basePubl + guiSystem.getPulicCards().get(1).getId() + exp);
        publ1.setImage(imagePubl1);
        Image imagePubl2 = new Image(basePubl + guiSystem.getPulicCards().get(2).getId() + exp);
        publ2.setImage(imagePubl2);

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

                    user0.setText(guiSystem.getOtherUsername().get(0));
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++) {

                            Image imageDice = new Image(diceURL + window.getWindow().getCell(i, j).getColor() + "-" + window.getWindow().getCell(i, j).getValue() + exp);
                            Rectangle rectangle = new Rectangle(30, 30);
                            rectangle.setFill(new ImagePattern(imageDice));

                            tabel0.add(rectangle, j, i);

                        }

                    }

                } else if (k == 1) {

                    user1.setText(guiSystem.getOtherUsername().get(1));
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 5; j++) {

                            Image imageDice = new Image(diceURL + window.getWindow().getCell(i, j).getColor() + "-" + window.getWindow().getCell(i, j).getValue() + exp);
                            Rectangle rectangle = new Rectangle(30, 30);
                            rectangle.setFill(new ImagePattern(imageDice));

                            tabel1.add(rectangle, j, i);

                        }

                    }

                } else if (k == 2) {

                    user1.setText(guiSystem.getOtherUsername().get(2));
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

        favorPoint.setText(Integer.toString(point));

    }

    @Override
    public void setDiceFromDraft(Integer columnIndex, Integer rowIndex) {




    }

    @Override
    public void succefulPlacementDice(String username, Cell dest, Dice moved) {

        String diceURL = "/img/Dices/";

        Platform.runLater(() -> {

            if (username.equals(guiSystem.getUserName())) {

                        Image imageDice = new Image(diceURL + moved.getColor() + "-" + moved.getValue() + exp);
                        Rectangle rectangle = new Rectangle(30, 30);
                        rectangle.setFill(new ImagePattern(imageDice));

                        myTabel.add(rectangle,dest.getCol(),dest.getRow());

                } else {
                int i = guiSystem.getOtherUsername().indexOf(username);
                Image imageDice = new Image(diceURL + moved.getColor() + "-" + moved.getValue() + exp);
                Rectangle rectangle = new Rectangle(30, 30);
                rectangle.setFill(new ImagePattern(imageDice));
                if (i == 0)
                    tabel0.add(rectangle, dest.getCol(),dest.getRow());

                if (i == 1)
                    tabel1.add(rectangle, dest.getCol(),dest.getRow());

                if (i == 2)
                    tabel2.add(rectangle, dest.getCol(),dest.getRow());
            }
        });

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

    public void clickTool0(MouseEvent mouseEvent) {

        useTool(0);

    }

    public void clickTool1(MouseEvent mouseEvent) {

        useTool(1);

    }

    public void clickTool2(MouseEvent mouseEvent) {

        useTool(2);

    }

    private void useTool(int i){

        Boolean wrong = false;
        //quit = false;
        //dices = new ArrayList<>();
        //cells = new ArrayList<>();
        diceValue = 0;
        //diceColor = null;
        //up = null;

        if(guiSystem.getServerSpeaker().checkPreCondition(i,guiSystem.getUserName())) {

            guiSystem.getServerSpeaker().askFavorPoints(guiSystem.getUserName());

            while (wrong) { //!quit ||

                Boolean validity = guiSystem.getServerSpeaker().checkPreCondition(i, guiSystem.getUserName());

                if (!validity)
                    wrong = true;
                else {
                    actor = guiSystem.getServerSpeaker().getActor(i, guiSystem.getUserName());
                    //showActor();

                    parameter = guiSystem.getServerSpeaker().getParameter(i, guiSystem.getUserName());
                    //getParameter();

                    if (checkNull())
                        wrong = checkAndUseTool(wrong, i);
                    else
                        wrong = true;
                }
            }
        }
    }

    /**
     * Checks if some parameter are null when they should not be null
     * @return true if everything is okay, false else
     */
        private Boolean checkNull() {
            nullCheck = true;

        parameter.forEach(param -> {
            if (param.equals(ToolCard.Parameter.DICE))
                nDices++;
            else if (param.equals(ToolCard.Parameter.CELL))
                nCells++;
            else if (param.equals(ToolCard.Parameter.BOOLEAN))
                nullCheck = up != null && nullCheck;
            else if (param.equals(ToolCard.Parameter.COLOR))
                nullCheck = diceColor != null && nullCheck;
            else if (param.equals(ToolCard.Parameter.INTEGER))
                nullCheck = diceValue != 0 && nullCheck;
        });

        IntStream.range(0, nDices).forEach(integer ->
                nullCheck = dices.get(integer) != null && nullCheck);

        IntStream.range(0, nCells).forEach(integer ->
                nullCheck = cells.get(integer) != null && nullCheck);

        return nullCheck;
    }

    /**
     * Check if tool is correct and try to applies it
     * @param wrong to remain in the loop of the caller
     * @return true if all correct, false else
     */
   private Boolean checkAndUseTool(Boolean wrong, int pick) {
        Boolean success;
        Boolean validity;

        if (!quit) {
            validity = serverSpeaker.checkTool(pick, dices, cells, diceValue, diceColor);

            if (!validity)
                wrong = true;
            else {
                success = serverSpeaker.useTool(pick, dices, up, cells, userName);

                if (!success)
                    wrong = true;
            }
        }

        return wrong;
    }

    private void getParameter() {
        parameter.forEach(param -> {
            parameterMap.get(param).accept(userName);
            acquireSemaphore();
        });
    }
}
