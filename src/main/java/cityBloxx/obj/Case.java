package cityBloxx.obj;


import static cityBloxx.test.DoAllBuildMethod.buildByCaseId2;

/**
 * @author zkk
 */
public class Case {

    public static Byte[][] caseIdToCase(long caseId){
        Byte[][] CASE = {{0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}};
        for (int i = 0; i < CASE.length; i++) {
            for (int j = 0; j < CASE[i].length; j++) {
                CASE[i][j] = (byte)(caseId % 4);
                caseId = caseId / 4;
            }
        }
        return CASE;
    }
    public static Byte[][] caseIdToCase2(long caseId){
        Byte[][] CASE = {{0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}};
        for (int i = 0; i < CASE.length; i++) {
            for (int j = 0; j < CASE[i].length; j++) {
                byte currHigh = (byte) (caseId - ((caseId >>> 2) << 2));
                CASE[i][j] = currHigh;
                caseId = caseId>>>2;
            }
        }
        return CASE;
    }

    private static int countByCaseId(long i){
        System.out.println(toFullSetBinary(i) + "counting...:" + i);
        int count = 0;
        long lastPosNum = 0L;
        for (int r = 62; r >= 0; r-=2) {
            i = i - (lastPosNum << (r + 2));
            long currNum = i >>> r;
            count+= currNum;
            System.out.print("0" + currNum + "_");
            lastPosNum = currNum;

        }
        System.out.println("\ncount:" + count);
        return count;
    }
    public static String toFullSetBinary(long num){
        StringBuilder binary = new StringBuilder(Long.toBinaryString(num));
        while (binary.length()<64){
            binary.insert(0, "0");
        }
        for (int i = 64; i > 2; i-=2) {
            binary.insert(i-2,"_");
        }
        return binary.toString();
    }
    public static void main(String[] args) {
        long caseId = 1108307771129855L;
        int count = countByCaseId(caseId);
        caseIdToCase2(caseId);
        CityBloxx cityBloxx = buildByCaseId2(caseId);
        System.out.println(caseId+"count:"+count + "," + cityBloxx.bloxxToString());
    }
}
