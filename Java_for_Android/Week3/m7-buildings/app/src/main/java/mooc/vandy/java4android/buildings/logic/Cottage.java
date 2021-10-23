package mooc.vandy.java4android.buildings.logic;

/**
 * This is the cottage class file.  It is a subclass of House.
 */
public class Cottage extends House {

    private boolean mSecondFloor;

    public Cottage(int dimension, int lotLength, int lotWidth, String owner) {
        super(dimension,dimension,lotLength,lotWidth,owner);
    }

    public Cottage(int dimension, int lotLength, int lotWidth, String owner, boolean secondFloor) {
        super(dimension,dimension,lotLength,lotWidth,owner);
        this.mSecondFloor=secondFloor;
    }

    public boolean hasSecondFloor() {
        return mSecondFloor;
    }

    @Override
    public String toString() {
        String res = super.toString();
        if(hasSecondFloor()){
            res += "; has second floor";
        }
        return res;
    }
}

