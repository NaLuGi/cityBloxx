package cityBloxx.obj;


import cityBloxx.enums.BuildingType;

import java.util.*;

/**
 * @author zkk
 */
public class CityBloxx {

    private Byte[][] Bloxxes = {{0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}};

    public int allFloorCount = 0;

    public CityBloxx() {
    }

    public CityBloxx(Byte[][] bloxxes) {
        Bloxxes = bloxxes;
        for (int i = 0; i < Bloxxes.length; i++) {
            for (int j = 0; j < Bloxxes[i].length; j++) {
                byte high = (byte) ((Bloxxes[i][j] + 1) * 10);
                Bloxxes[i][j] = high;
            }
        }
    }

    public void build(byte x, byte y, byte numberOfFloors) {
        if (x < 0) {
            throw new RuntimeException("向北超出地图外");
        }
        if (x >= this.Bloxxes.length) {
            throw new RuntimeException("向南超出地图外");
        }
        if (y < 0) {
            throw new RuntimeException("向西超出地图外");
        }
        if (y >= this.Bloxxes[0].length) {
            throw new RuntimeException("向东超出地图外");
        }
        this.Bloxxes[x][y] = numberOfFloors;
    }

    public byte getHigh(byte x, byte y) {
        if (x < 0) {
            return 0;
        }
        if (x >= this.Bloxxes.length) {
            return 0;
        }
        if (y < 0) {
            return 0;
        }
        if (y >= this.Bloxxes[0].length) {
            return 0;
        }
        return this.Bloxxes[x][y];
    }

    public List<BuildingType> getAround(byte x, byte y) {
        List<BuildingType> aroundBuildingTypes = new ArrayList<>();
        aroundBuildingTypes.add(BuildingType.getTypeByHigh(getHigh((byte) (x - 1), y)));
        aroundBuildingTypes.add(BuildingType.getTypeByHigh(getHigh((byte) (x + 1), y)));
        aroundBuildingTypes.add(BuildingType.getTypeByHigh(getHigh(x, (byte) (y - 1))));
        aroundBuildingTypes.add(BuildingType.getTypeByHigh(getHigh(x, (byte) (y + 1))));
        return aroundBuildingTypes;
    }

    public Set<Byte> getAround2(byte x, byte y) {
        Set<Byte> aroundBuildingTypes = new HashSet<>();
        aroundBuildingTypes.add((getHigh((byte) (x - 1), y)));
        aroundBuildingTypes.add((getHigh((byte) (x + 1), y)));
        aroundBuildingTypes.add((getHigh(x, (byte) (y - 1))));
        aroundBuildingTypes.add((getHigh(x, (byte) (y + 1))));
        return aroundBuildingTypes;
    }

    public boolean checkAround(byte x, byte y) {
        Set<BuildingType> buildingTypeSet = new HashSet<>((getAround(x, y)));
        BuildingType thisType = BuildingType.getTypeByHigh(this.Bloxxes[x][y]);
        if (BuildingType.red.equals(thisType)) {
            return buildingTypeSet.contains(BuildingType.blue);
        } else if (BuildingType.green.equals(thisType)) {
            return buildingTypeSet.contains(BuildingType.blue)
                    && buildingTypeSet.contains(BuildingType.red);
        } else if (BuildingType.gold.equals(thisType)) {
            return buildingTypeSet.contains(BuildingType.blue)
                    && buildingTypeSet.contains(BuildingType.red)
                    && buildingTypeSet.contains(BuildingType.green);
        }
        return true;
    }

    public boolean checkAround2(byte x, byte y) {
        Set<Byte> buildingTypeSet = (getAround2(x, y));
        Byte high = this.Bloxxes[x][y];
        if (BuildingType.RED_HIGH == (high)) {
            return buildingTypeSet.containsAll(BuildingType.red.aroundSet);
        } else if (BuildingType.GREEN_HIGH == (high)) {
            return buildingTypeSet.containsAll(BuildingType.green.aroundSet);
        } else if (BuildingType.GOLD_HIGH == (high)) {
            return buildingTypeSet.containsAll(BuildingType.gold.aroundSet);
        }
        return true;
    }

    public void count() {
        for (byte x = 0; x < this.Bloxxes.length; x++) {
            for (byte y = 0; y < this.Bloxxes[x].length; y++) {
                allFloorCount += this.getHigh(x, y);
            }
        }
    }
    public void count(int count) {
        this.allFloorCount = count;
    }

    public boolean checkLegality() {
        for (byte x = 0; x < this.Bloxxes.length; x++) {
            for (byte y = 0; y < this.Bloxxes[x].length; y++) {
                byte high = this.getHigh(x, y);
                if (high != BuildingType.blue.getMaxHigh()) {
                    if (!checkAround(x, y)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean checkLegality2() {
        for (byte x = 0; x < this.Bloxxes.length; x++) {
            for (byte y = 0; y < this.Bloxxes[x].length; y++) {
                byte high = this.Bloxxes[x][y];
                if (high != (byte) 10) {
                    if (!checkAround2(x, y)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String bloxxToString() {
        return "Bloxxes={\n" +
                "\t{" + this.Bloxxes[0][0] + ", " + this.Bloxxes[0][1] + ", " +
                this.Bloxxes[0][2] + ", " + this.Bloxxes[0][3] + ", " +
                this.Bloxxes[0][4] + "},\n" +
                "\t{" + this.Bloxxes[1][0] + ", " + this.Bloxxes[1][1] + ", " +
                this.Bloxxes[1][2] + ", " + this.Bloxxes[1][3] + ", " +
                this.Bloxxes[1][4] + "},\n" +
                "\t{" + this.Bloxxes[2][0] + ", " + this.Bloxxes[2][1] + ", " +
                this.Bloxxes[2][2] + ", " + this.Bloxxes[2][3] + ", " +
                this.Bloxxes[2][4] + "},\n" +
                "\t{" + this.Bloxxes[3][0] + ", " + this.Bloxxes[3][1] + ", " +
                this.Bloxxes[3][2] + ", " + this.Bloxxes[3][3] + ", " +
                this.Bloxxes[3][4] + "},\n" +
                "\t{" + this.Bloxxes[4][0] + ", " + this.Bloxxes[4][1] + ", " +
                this.Bloxxes[4][2] + ", " + this.Bloxxes[4][3] + ", " +
                this.Bloxxes[4][4] + "},\n}\n";
    }

    @Override
    public String toString() {
        return "CityBloxx{" +
                "allFloorCount=" + allFloorCount +
                ", " + bloxxToString();
    }
}
