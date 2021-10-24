package mooc.vandy.java4android.calculator.logic;

import mooc.vandy.java4android.calculator.ui.ActivityInterface;

/**
 * Performs an operation selected by the user.
 */
public class Logic implements LogicInterface {
    private static final int ADD = 1;
    private static final int SUBTRACT = 2;
    private static final int MULTIPLY = 3;
    private static final int DIVIDE = 4;
    /**
     * Reference to the Activity output.
     */
    protected ActivityInterface mOut;


    /**
     * Constructor initializes the field.
     */
    public Logic(ActivityInterface out) {
        mOut = out;

    }

    /**
     * Perform the operation on argumentOne and argumentTwo.
     */
    public void process(int argumentOne, int argumentTwo, int operation) {
        Operation operationToPerform = null;
        if (operation == ADD) {
            operationToPerform = new Add(argumentOne, argumentTwo);
        }
        if (operation == SUBTRACT) {
            operationToPerform = new Subtract(argumentOne, argumentTwo);
        }
        if (operation == MULTIPLY) {
            operationToPerform = new Multiply(argumentOne, argumentTwo);
        }
        if (operation == DIVIDE) {
            operationToPerform = new Divide(argumentOne, argumentTwo);
        }
        if (operationToPerform != null) {
            mOut.print(operationToPerform.performOperation());
        } else {
            mOut.print("Unknown operation");
        }

    }
}
