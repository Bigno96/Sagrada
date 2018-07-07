package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import it.polimi.ingsw.server.model.roundtrack.RoundTrack;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.FileSystem;


import java.io.IOException;

public class AskWindow {

        Stage window;

        public void display(String message) {
            window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Round Track");
            window.setMinWidth(200);

       /* GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.setVgap(8);
        gridPane.setHgap(10);

        String diceURL = "/img/Dices";
        String exp = ".png";

        Platform.runLater(() -> {

        roundTrack.getTrackList().forEach(listDiceRound -> {
            int index = roundTrack.getTrackList().indexOf(listDiceRound);

            int i = 0;
            listDiceRound.itr().forEachRemaining(dice ->{

                int indexList = listDiceRound.itr().
                         Image imageDice = new Image(diceURL + dice.getColor() + "-" + dice.getValue() + exp);
                         Rectangle rectangle = new Rectangle(30, 30);
                         rectangle.setFill(new ImagePattern(imageDice));

                         gridPane.add(rectangle,index,i);
                         i++;

                    }

            );

            for(int j = 0; j < roundTrack.getTrackList().get(index).getListOfDice().size(); j++){

                //roundTrack.getTrackList().get(index).getListOfDice();

                Image imageDice = new Image(diceURL + roundTrack.getTrackList().get(index).getListOfDice().get(j).getColor() + "-" + roundTrack.getTrackList().get(index).getListOfDice().get(j).getValue() + exp);
                Rectangle rectangle = new Rectangle(30, 30);
                rectangle.setFill(new ImagePattern(imageDice));
                int id = index * 10 + j;
                rectangle.setId(Integer.toString(id));
                rectangle.setOnMouseClicked(draftClick);

                gridPane.add(rectangle,index,j);

                }


            });


        });

    }


    EventHandler<MouseEvent> draftClick =
            new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {



                }

            };*/

            Platform.runLater(() -> {
                Parent root = null;
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AskPage.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                assert root != null;
                window.setScene(new Scene(root));

                AskController askController = loader.getController();
                //askController.setMessage(message);


                window.show();
            });

        }

        public void closeWindow(){

            window.close();

        }

}
