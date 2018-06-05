package it.polimi.ingsw.client.view.gui;

import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GuiData {

    private StringProperty firstPlayer;
    private StringProperty secondPlayer;
    private StringProperty thirdPlayer;
    private StringProperty fourthPlayer;
    private StringProperty userName;

    private StringProperty firstWindow;
    private StringProperty secondWindow;
    private StringProperty thirdWindow;
    private StringProperty fourthWindow;

    private StringProperty firstPubl;
    private StringProperty secondPubl;
    private StringProperty thirdPubl;

    private StringProperty firstTool;
    private StringProperty secondTool;
    private StringProperty thirdTool;

    private StringProperty myPriv;

    public GuiData( StringProperty userName) {
        this.userName = userName;
    }

    public GuiData( ) {

    }

    public String getUserName() {
        return userName.get();
    }

    public String getFirstPlayer() {
        return firstPlayer.get();
    }

    public StringProperty firstPlayerProperty() {
        return firstPlayer;
    }

    public void setFirstPlayer(String firstPlayer) {
        this.firstPlayer.set(firstPlayer);
    }

    public String getSecondPlayer() {
        return secondPlayer.get();
    }

    public StringProperty secondPlayerProperty() {
        return secondPlayer;
    }

    public void setSecondPlayer(String secondPlayer) {
        this.secondPlayer.set(secondPlayer);
    }

    public String getThirdPlayer() {
        return thirdPlayer.get();
    }

    public StringProperty thirdPlayerProperty() {
        return thirdPlayer;
    }

    public void setThirdPlayer(String thirdPlayer) {
        this.thirdPlayer.set(thirdPlayer);
    }

    public String getFourthPlayer() {
        return fourthPlayer.get();
    }

    public StringProperty fourthPlayerProperty() {
        return fourthPlayer;
    }

    public void setFourthPlayer(String fourthPlayer) {
        this.fourthPlayer.set(fourthPlayer);
    }

    public String getFirstWindow() {
        return firstWindow.get();
    }

    public StringProperty firstWindowProperty() {
        return firstWindow;
    }

    public void setFirstWindow(String firstWindow) {
        this.firstWindow.set(firstWindow);
    }

    public String getSecondWindow() {
        return secondWindow.get();
    }

    public StringProperty secondWindowProperty() {
        return secondWindow;
    }

    public void setSecondWindow(String secondWindow) {
        this.secondWindow.set(secondWindow);
    }

    public String getThirdWindow() {
        return thirdWindow.get();
    }

    public StringProperty thirdWindowProperty() {
        return thirdWindow;
    }

    public void setThirdWindow(String thirdWindow) {
        this.thirdWindow.set(thirdWindow);
    }

    public String getFourthWindow() {
        return fourthWindow.get();
    }

    public StringProperty fourthWindowProperty() {
        return fourthWindow;
    }

    public void setFourthWindow(String fourthWindow) {
        this.fourthWindow.set(fourthWindow);
    }

    public String getMyPriv() {
        return myPriv.get();
    }

    public StringProperty myPrivProperty() {
        return myPriv;
    }

    public void setMyPriv(String myPriv) {
        this.myPriv.set(myPriv);
    }

    public String getThirdTool() {
        return thirdTool.get();
    }

    public StringProperty thirdToolProperty() {
        return thirdTool;
    }

    public void setThirdTool(String thirdTool) {
        this.thirdTool.set(thirdTool);
    }

    public String getSecondTool() {
        return secondTool.get();
    }

    public StringProperty secondToolProperty() {
        return secondTool;
    }

    public void setSecondTool(String secondTool) {
        this.secondTool.set(secondTool);
    }

    public String getFirstTool() {
        return firstTool.get();
    }

    public StringProperty firstToolProperty() {
        return firstTool;
    }

    public void setFirstTool(String firstTool) {
        this.firstTool.set(firstTool);
    }

    public String getThirdPubl() {
        return thirdPubl.get();
    }

    public StringProperty thirdPublProperty() {
        return thirdPubl;
    }

    public void setThirdPubl(String thirdPubl) {
        this.thirdPubl.set(thirdPubl);
    }

    public String getSecondPubl() {
        return secondPubl.get();
    }

    public StringProperty secondPublProperty() {
        return secondPubl;
    }

    public void setSecondPubl(String secondPubl) {
        this.secondPubl.set(secondPubl);
    }

    public String getFirstPubl() {
        return firstPubl.get();
    }

    public StringProperty firstPublProperty() {
        return firstPubl;
    }

    public void setFirstPubl(String firstPubl) {
        this.firstPubl.set(firstPubl);
    }
}
