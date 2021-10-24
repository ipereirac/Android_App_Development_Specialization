package mooc.vandy.java4android.calculator.logic;

public abstract class Operation {

    private int mArgument1;
    private int mArgument2;

    public Operation(int argument1, int argument2) {
        this.mArgument1 = argument1;
        this.mArgument2 = argument2;
    }

    public int getArgument1() {
        return mArgument1;
    }

    public int getArgument2() {
        return mArgument2;
    }

    public abstract String performOperation();

}
