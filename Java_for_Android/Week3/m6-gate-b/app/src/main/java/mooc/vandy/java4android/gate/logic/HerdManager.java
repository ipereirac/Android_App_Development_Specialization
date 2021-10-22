package mooc.vandy.java4android.gate.logic;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import mooc.vandy.java4android.gate.ui.OutputInterface;

/**
 * This class uses your Gate class to manage a herd of snails.  We
 * have supplied you will the code necessary to execute as an app.
 * You must fill in the missing logic below.
 */
public class HerdManager {
    /**
     * Reference to the output.
     */
    private OutputInterface mOut;

    /**
     * The input Gate object.
     */
    private Gate mWestGate;

    /**
     * The output Gate object.
     */
    private Gate mEastGate;

    /**
     * Maximum number of iterations to run the simulation.
     */
    private static final int MAX_ITERATIONS = 10;

    public static final int HERD = 24;

    /**
     * Constructor initializes the fields.
     */
    public HerdManager(OutputInterface out,
                       Gate westGate,
                       Gate eastGate) {
        mOut = out;

        mWestGate = westGate;
        mWestGate.open(Gate.IN);

        mEastGate = eastGate;
        mEastGate.open(Gate.OUT);
    }

    public void simulateHerd(Random rand) {
        int insideThePen = HERD;
        mOut.println("There are currently "+insideThePen+" snails in the pen and "+(HERD-insideThePen)+" snails in the pasture");
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            if (insideThePen == HERD) {
                int qty = rand.nextInt(HERD)+1;
                insideThePen += mEastGate.thru(qty);
            } else if (insideThePen == 0) {
                int qty = rand.nextInt(HERD)+1;
                insideThePen += mWestGate.thru(qty);
            } else {
                boolean in = rand.nextBoolean();
                if(in){
                    int qtyIn = rand.nextInt(insideThePen)+1;
                    insideThePen += mEastGate.thru(qtyIn);
                }else{
                    int qtyOut = rand.nextInt(HERD-insideThePen)+1;
                    insideThePen += mWestGate.thru(qtyOut);
                }
            }
            mOut.println("There are currently "+insideThePen+" snails in the pen and "+(HERD-insideThePen)+" snails in the pasture");
        }
    }


}
