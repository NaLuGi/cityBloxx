package cityBloxx.enums;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zkk
 */
public enum BuildingType {
    /**
     * 灰色 无建筑
     */
    ash((byte)0),
    /**
     * 蓝色
     */
    blue(BuildingType.BLUE_HIGH),
    /**
     * 红色
     */
    red(BuildingType.RED_HIGH),
    /**
     * 绿色
     */
    green(BuildingType.GREEN_HIGH),
    /**
     * 金色
     */
    gold(BuildingType.GOLD_HIGH);

    /**
     * 红楼最高层数
     */
    public static final byte BLUE_HIGH = (byte) 10;
    /**
     * 红楼最高层数
     */
    public static final byte RED_HIGH = (byte) 20;
    /**
     * 绿楼最高层数
     */
    public static final byte GREEN_HIGH = (byte) 30;
    /**
     * 金楼最高层数
     */
    public static final byte GOLD_HIGH = (byte) 40;

    BuildingType(byte maxHigh) {
        this.maxHigh = maxHigh;
        while (maxHigh > (byte)10){
            maxHigh -= 10;
            this.aroundSet.add(maxHigh);
        }
    }

    public static BuildingType getTypeByHigh(byte high){
        for (BuildingType buildingType : values()) {
            if (buildingType.maxHigh == high){
                return buildingType;
            }
        }
        throw new RuntimeException("未找到高度为" + high + "对应的建筑颜色");
    }

    public byte getMaxHigh() {
        return maxHigh;
    }

    public static List<BuildingType> getBuildingTypeList(){
        List<BuildingType> buildingTypeList = new ArrayList<>();
        for (BuildingType buildingType : values()) {
            if (buildingType.maxHigh>0){
                buildingTypeList.add(buildingType);
            }
        }
        return buildingTypeList;
    }
    byte maxHigh;

    public Set<Byte> aroundSet = new HashSet<>();
}
