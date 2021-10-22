package mooc.vandy.java4android.diamonds.logic;

import java.util.ArrayList;

import mooc.vandy.java4android.diamonds.ui.OutputInterface;

/**
 * This is where the logic of this App is centralized for this assignment.
 * <p>
 * The assignments are designed this way to simplify your early
 * Android interactions.  Designing the assignments this way allows
 * you to first learn key 'Java' features without having to beforehand
 * learn the complexities of Android.
 */
public class Logic
        implements LogicInterface {
    /**
     * This is a String to be used in Logging (if/when you decide you
     * need it for debugging).
     */
    public static final String TAG = Logic.class.getName();

    /**
     * This is the variable that stores our OutputInterface instance.
     * <p>
     * This is how we will interact with the User Interface [MainActivity.java].
     * <p>
     * It is called 'out' because it is where we 'out-put' our
     * results. (It is also the 'in-put' from where we get values
     * from, but it only needs 1 name, and 'out' is good enough).
     */
    private OutputInterface mOut;

    /**
     * This is the constructor of this class.
     * <p>
     * It assigns the passed in [MainActivity] instance (which
     * implements [OutputInterface]) to 'out'.
     */
    public Logic(OutputInterface out) {
        mOut = out;
    }

    /**
     * This is the method that will (eventually) get called when the
     * on-screen button labeled 'Process...' is pressed.
     */
    public void process(int size) {
        printTopBorder(size);
        for (int i = 1; i < size; i++) {
            printUpRow(i, size);
        }
        printCenter(size, (size-1)%2==0);
        for (int i = size; i >1; i--) {
            printLowRow(i-1,size);
        }
        printBottomBorder(size);
    }

    private void printUpRow(int i, int size) {
        mOut.print("|");
        int spaces = size - i;
        int fillers = size - spaces -1;
        printXSpaces(spaces);
        mOut.print("/");
        if (fillers > 0) {
            fillDash(fillers, (i-1)%2==0);
        }
        mOut.print("\\");
        printXSpaces(spaces);
        mOut.println("|");
    }

    private void fillDash(int count, boolean isEven) {
        for (int i = 0; i < count*2; i++) {
            if(isEven){
                mOut.print("=");
            }else{
                mOut.print("-");
            }
        }
    }


    private void printLowRow(int i, int size) {
        mOut.print("|");
        int spaces = size -i ;
        int fillers = size - spaces -1;
        printXSpaces(spaces);
        mOut.print("\\");
        if (fillers > 0) {
            fillDash(fillers, (i-1)%2==0);
        }
        mOut.print("/");
        printXSpaces(spaces);
        mOut.println("|");
    }

    private void printXSpaces(int cant) {
        for (int i = 0; i < cant; i++) {
            mOut.print(" ");
        }
    }

    private void printTopBorder(int size) {
        mOut.print("+");
        for (int i = 0; i < size * 2; i++) {
            mOut.print("-");
        }
        mOut.println("+");
    }

    private void printCenter(int size, boolean isEven) {
       mOut.print("|");
        mOut.print("<");
        for (int j = 1; j < size; j++) {
            if(isEven){
                mOut.print("==");
            }else{
                mOut.print("--");
            }
        }
        mOut.print(">");
        mOut.println("|");
    }

    private void printBottomBorder(int size) {
        mOut.print("+");
        for (int i = 0; i < size * 2; i++) {
            mOut.print("-");
        }
        mOut.println("+");
    }

}
