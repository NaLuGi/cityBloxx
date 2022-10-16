package cityBloxx.test;

import cityBloxx.Thread.CustomThreadPoolExecutor;
import cityBloxx.enums.BuildingType;
import cityBloxx.obj.Case;
import cityBloxx.obj.CityBloxx;
import org.apache.lucene.util.RamUsageEstimator;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;

import static org.apache.lucene.util.RamUsageEstimator.ONE_GB;

/**
 * @author zkk
 */
public class DoAllBuildMethod {

    /**
     * 本次执行要开辟的线程数
     */
    private static final int THREAD_SIZE = 64;

    public static long maxCaseCount;

    static {
        maxCaseCount = new BigDecimal(4).pow(25).longValue();
    }

    private static final Map<String, Date> PART_MAP = new HashMap<>();

    public static void main(String[] args) {

        List<CityBloxx> lawfulHighestWayList = getLawfulHighestWayList();
        System.out.println(lawfulHighestWayList);

        Long lawfulWaySize = getLawfulWaySize();
        System.out.println(lawfulWaySize);
        if (lawfulHighestWayList.size() > 0) {

            long oneSize = RamUsageEstimator.sizeOf(lawfulHighestWayList.get(0));
            if (oneSize * lawfulWaySize < ONE_GB) {
                System.out.println(getLawfulWayList());
            }
        }
    }

    private static List<CityBloxx> getLawfulHighestWayList() {
        List<CityBloxx> lawfulHighestWayList = new ArrayList<>();
        final int[] highestFloorCount = {0};
        CustomThreadPoolExecutor executor = new CustomThreadPoolExecutor();
        executor.init(THREAD_SIZE);
        CyclicBarrier barrier = new CyclicBarrier(THREAD_SIZE);
        ExecutorService poolExecutor = executor.getCustomThreadPoolExecutor();
        PART_MAP.put("主线程开始", new Date());
        for (int t = 0; t < THREAD_SIZE; t++) {
            int finalT = t;
            poolExecutor.execute(() -> {
                long partSize = maxCaseCount / THREAD_SIZE;
                long partStart = finalT * partSize;
                for (long i = 0; i < partSize; i++) {
                    long id = partStart + i;
                    int count = countByCaseId(id);
                    if (count < highestFloorCount[0]) {
                        continue;
                    }
                    CityBloxx cityBloxx = buildByCaseId2(id);
                    if (cityBloxx.checkLegality2()) {
                        if (count >= highestFloorCount[0]) {
                            cityBloxx.count(count);
                            lawfulHighestWayList.add(cityBloxx);
                            synchronized (lawfulHighestWayList) {
                                if (count > highestFloorCount[0]) {
                                    lawfulHighestWayList.clear();
                                }
                                highestFloorCount[0] = count;
                            }
                        }
                    }
                }
                try {
                    barrier.await();
                    String info = "线程" + finalT + "结束";
                    System.out.println(info);
                    PART_MAP.put(info, new Date());
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            barrier.await();
            System.out.println(PART_MAP);
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        return lawfulHighestWayList;
    }

    private static List<CityBloxx> getLawfulWayList() {
        List<CityBloxx> lawfulWayList = new ArrayList<>();
        List<BuildingType> buildingTypes = BuildingType.getBuildingTypeList();
        for (long i = 0; i < maxCaseCount; i++) {
            CityBloxx cityBloxx = buildByCaseId(buildingTypes, i);
            if (cityBloxx.checkLegality()) {
                lawfulWayList.add(cityBloxx);
            }
        }
        return lawfulWayList;
    }

    private static Long getLawfulWaySize() {
        long lawfulWaySize = 0;
        List<BuildingType> buildingTypes = BuildingType.getBuildingTypeList();
        for (long i = 0; i < maxCaseCount; i++) {
            CityBloxx cityBloxx = buildByCaseId(buildingTypes, i);
            if (cityBloxx.checkLegality()) {
                lawfulWaySize++;
            }
        }
        return lawfulWaySize;
    }

    public static int countByCaseId(long i) {
        int count = 0;
        long lastPosNum = 0L;

        for (int r = Long.SIZE - 2; r >= 0; r -= 2) {
            i = i - (lastPosNum << (r + 2));
            long currNum = i >>> r;
            count += currNum + 1;
            lastPosNum = currNum;
        }
        return (count - 7) * 10;
    }

    private static CityBloxx buildByCaseId(List<BuildingType> buildingTypes, long i) {
        Byte[][] caseI = Case.caseIdToCase(i);
        CityBloxx cityBloxx = new CityBloxx();
        for (byte j = 0; j < caseI.length; j++) {
            for (byte k = 0; k < caseI[j].length; k++) {
                cityBloxx.build(j, k, buildingTypes.get(caseI[j][k]).getMaxHigh());
            }
        }
        return cityBloxx;
    }

    public static CityBloxx buildByCaseId2(long i) {
        Byte[][] caseI = Case.caseIdToCase2(i);
        return new CityBloxx(caseI);
    }

}
