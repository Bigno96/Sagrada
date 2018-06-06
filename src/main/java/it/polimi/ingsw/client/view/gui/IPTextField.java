package it.polimi.ingsw.client.view.gui;

import javafx.scene.control.TextField;

public class IPTextField extends TextField {

    public void IPTextField(){
        this.setPromptText("Only valid IP");
    }

    @Override
    public void replaceText(int i, int il, String string){
        if(!validIP(string)){
            super.replaceText(i, il, string);
        }
    }

    @Override
    public void replaceSelection(String string){
        super.replaceSelection(string);
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
}
