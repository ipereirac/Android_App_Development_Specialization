package mooc.vandy.java4android.calculator.logic;

/**
 * Perform the Add operation.
 */
public class Add extends Operation{

    public Add(int argumentOne, int argumentTwo) {
        super(argumentOne,argumentTwo);
    }

    @Override
    public String performOperation() {
        return String.valueOf(getArgument1()+getArgument2());
    }

}
