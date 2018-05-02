package it.polimi.model;

import java.util.ArrayList;
import java.util.List;

public class DiceBag {

    private static DiceBag diceBag = null;
    private List<Dice> dices = new ArrayList<Dice>();

    public static DiceBag getInstance()
    {
        if (diceBag == null)
        {
            diceBag = new DiceBag();
        }
        return diceBag;
    }

    private DiceBag()
    {
        int i,n = 0;
        for (Dice.color c: Dice.color.values())     // loop sull'enum color di Dice
        {
            for(i = n*18; i < (n+1)*18; i++)        //assegno 18 dadi per ogni colore
            {
                Dice dice = new Dice(i,c);
                dices.add(dice);
            }
            n++;
        }
    }


}
