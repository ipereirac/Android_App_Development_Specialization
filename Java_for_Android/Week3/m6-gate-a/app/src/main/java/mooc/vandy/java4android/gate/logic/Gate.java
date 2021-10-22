package mooc.vandy.java4android.gate.logic;

/**
 * This file defines the Gate class.
 */
public class Gate {
    public static int IN = 1;
    public static int OUT = -1;
    public static int CLOSED = 0;
    private int mSwing;

    public Gate() {

    }

    public boolean setSwing(int direction) {
        if (direction == IN || direction == OUT) {
            this.mSwing = direction;
            return true;
        }
        return false;
    }

    public boolean open(int direction) {
        return setSwing(direction);
    }

    public void close() {
        this.mSwing = 0;
    }

    public int getSwingDirection() {
        return mSwing;
    }

    public int thru(int count) {
        if(mSwing==CLOSED) {
            return 0;
        }else if(mSwing==IN){
            return count;
        }else{
            return count*-1;
        }
    }

    @Override
    public String toString() {
        if(mSwing==CLOSED){
            return "This gate is closed";
        }else if(mSwing==IN){
            return "This gate is open and swings to enter the pen only";
        }else if(mSwing==OUT){
            return "This gate is open and swings to exit the pen only";
        }else{
            return "This gate has an invalid swing direction";
        }

    }
}
