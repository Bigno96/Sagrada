import it.polimi.ingsw.client.view.ViewInterface;
import it.polimi.ingsw.exception.IDNotFoundException;
import it.polimi.ingsw.exception.PositionException;
import it.polimi.ingsw.exception.ValueException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.scene.control.*;
import it.polimi.ingsw.client.view.cli.CliSystem;
import javafx.stage.*;
import javafx.scene.*;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClientMainC {

    boolean Close = true;

    Button buttonCLI;
    Button buttonGUI;

    private ViewInterface graphic;

    public void chooseGUI(){
    }

    public void chooseCLI(javafx.event.ActionEvent event) throws FileNotFoundException, IDNotFoundException, PositionException, ValueException {

       // ((Stage)(((Button)event.getSource()).getScene().getWindow())).hide();
        graphic = new CliSystem();
        System.out.println("CLI graphic chosen");
        graphic.startGraphic();
        //notify();

    }

    public boolean GUI(){
        return Close;
    }
}
