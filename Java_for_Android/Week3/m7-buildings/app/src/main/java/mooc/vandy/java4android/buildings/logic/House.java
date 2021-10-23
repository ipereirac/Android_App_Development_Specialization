package mooc.vandy.java4android.buildings.logic;

import java.util.Objects;

/**
 * This is the House class file that extends Building.
 */
public class House extends Building {

    private String mOwner;
    private boolean mPool;
    public House(int length, int width, int lotLength, int lotWidth) {
        super(length, width, lotLength, lotWidth);
    }

    public House(int length, int width, int lotLength, int lotWidth, String owner) {
        super(length, width, lotLength, lotWidth);
        this.mOwner = owner;
    }

    public House(int length, int width, int lotLength, int lotWidth, String owner, boolean pool) {
        super(length, width, lotLength, lotWidth);
        this.mOwner = owner;
        this.mPool = pool;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        this.mOwner = owner;
    }

    public boolean hasPool() {
        return mPool;
    }

    public void setPool(boolean pool) {
        this.mPool = pool;
    }

    @Override
    public String toString() {
        String res= "";
        if(mOwner!=null){
            res+="Owner: "+mOwner;
        }
        if(hasPool()){
            res+="; has a pool";
        }
        if (calcLotArea()>calcBuildingArea()){
            res+="; has a big open space";
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        House house = (House) o;
        return mPool == house.mPool && calcBuildingArea()==house.calcBuildingArea();
    }


}
