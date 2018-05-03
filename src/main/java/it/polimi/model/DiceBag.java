package it.polimi.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class DiceBag {

    private static DiceBag diceBag = null;
    private List<Dice> dices;
    private static final Logger logger = Logger.getLogger(Dice.class.getName());

    public static DiceBag getInstance() {
        if (diceBag == null) {
            diceBag = new DiceBag();
        }
        return diceBag;
    }

    private DiceBag() {     // dice enumerated from 0 to 89
        dices = new ArrayList<Dice>();
        int n = 0;
        for (final Dice.color c : Dice.color.values())     // loop on Dice's enum color
        {
            for (int i = n * 18; i < (n + 1) * 18; i++)        // 18 dices assigned per color
            {
                Dice dice = new Dice(i, c);
                dices.add(dice);
            }
            n++;
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "@ " + this.hashCode();
    }

    public void dump()
    {
        logger.info("contains following dices: ");
        for (Dice d : dices)
        {
            d.dump();
        }
    }

    public Dice findDice(int id) {         // find and return Dice with passed id
        for (final Dice d : dices)
        {
            if (d.getID() == id) {
                return d;
            }
        }
        return null;
    }

    public Dice randDice() {
        Dice d;
        do
        {
            Random rand = new Random();
            d = findDice(rand.nextInt(dices.size()));
        } while (d == null || d.getValue() != 0);

        return d;
    }

    public int diceRemaining() { return this.dices.size(); }

    public boolean rmDice(Dice d) { return dices.remove(d); }

    public boolean addDice(Dice d) { return dices.add(d); }

}