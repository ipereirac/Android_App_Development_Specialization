package mooc.vandy.java4android.calculator.logic;

/**
 * Perform the Divide operation.
 */
public class Divide extends Operation{

    public Divide(int argument1, int argument2) {
        super(argument1, argument2);
    }

    @Override
    public String performOperation() {
        if(getArgument2()==0){
            return "Cannot be divided by 0!";
        }
        int result = getArgument1()/getArgument2();
        int remainder = getArgument1() % getArgument2();
        return ""+result+" R "+remainder;
    }
}
