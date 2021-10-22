package mooc.vandy.java4android.gate.logic;

import java.util.Arrays;
import java.util.Random;

import mooc.vandy.java4android.gate.ui.OutputInterface;

/**
 * This class uses your Gate class to fill the corral with snails.  We
 * have supplied you will the code necessary to execute as an app.
 * You must fill in the missing logic below.
 */
public class FillTheCorral {
    /**
     * Reference to the OutputInterface.
     */
    private OutputInterface mOut;

    /**
     * Constructor initializes the field.
     */
    FillTheCorral(OutputInterface out) {
        mOut = out;
    }

    public void setCorralGates(Gate[] gates, Random rand) {

        for (Gate gate : gates) {
            int direction = rand.nextInt(3) - 1;
            gate.setSwing(direction);
            mOut.println(gate.toString());
        }
    }

    public boolean anyCorralAvailable(Gate[] corral) {
        for (Gate gate : corral) {
            if (gate.getSwingDirection() == Gate.IN) {
                return true;
            }
        }
        return false;
    }

    public int corralSnails(Gate[] corral, Random rand) {
        int snailsOut = 5;
        int attempts = 0;
        do {
            int randomGate = rand.nextInt(corral.length);
            int randomNumberOfSnails = rand.nextInt(snailsOut) + 1;
            mOut.println(randomNumberOfSnails + " are trying to move through corral " + randomGate);
            snailsOut -= corral[randomGate].thru(randomNumberOfSnails);
            attempts++;
        } while (snailsOut > 0);
        mOut.println("It took " + attempts + " attempts to corral all of the snails.");
        return attempts;
    }

}
