package mooc.vandy.java4android.calculator.logic;

/**
 * Perform the Subtract operation.
 */
public class Subtract extends Operation {

    public Subtract(int argument1, int argument2) {
        super(argument1, argument2);
    }

    @Override
    public String performOperation() {
        return String.valueOf(getArgument1()-getArgument2());
    }
}
