package cityBloxx.test;

import cityBloxx.Thread.CustomThreadPoolExecutor;
import cityBloxx.enums.BuildingType;
import cityBloxx.obj.Case;
import cityBloxx.obj.CityBloxx;
import cityBloxx.obj.ThreadInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import static cityBloxx.test.DoAllBuildMethod.countByCaseId;

/**
 * 穷举法
 * 暂定失败，本机算力要好几年
 * @author zkk
 */
public class DoAll3x3BuildMethod {

    /**
     * 本次执行要开辟的线程数
     */
    private static final int THREAD_SIZE = 1;

    public static long maxCaseCount;

    static {
        maxCaseCount = new BigDecimal(4).pow(9).longValue();
    }

    private static final Map<String, Date> PART_MAP = new HashMap<>();

    public static void main(String[] args) {

        List<CityBloxx> lawfulHighestWayList = getLawfulHighestWayList();
        System.out.println(lawfulHighestWayList);

//        Long lawfulWaySize = getLawfulWaySize();
//        System.out.println(lawfulWaySize);
//        if (lawfulHighestWayList.size() > 0) {
//
//            long oneSize = RamUsageEstimator.sizeOf(lawfulHighestWayList.get(0));
//            if (oneSize * lawfulWaySize < ONE_GB) {
//                System.out.println(getLawfulWayList());
//            }
//        }
    }

    private static List<CityBloxx> getLawfulHighestWayList() {
        List<CityBloxx> lawfulHighestWayList = new ArrayList<>();
        AtomicReference<CityBloxx> lawfulHighestWay = new AtomicReference<>(new CityBloxx());
        final int[] highestFloorCount = {0};
        CustomThreadPoolExecutor executor = new CustomThreadPoolExecutor();
        executor.init(THREAD_SIZE);
        CyclicBarrier barrier = new CyclicBarrier(THREAD_SIZE);
        ExecutorService poolExecutor = executor.getCustomThreadPoolExecutor();
        PART_MAP.put("主线程开始", new Date());
        List<ThreadInfo> threadInfos = new ArrayList<>();
        for (int t = 0; t < THREAD_SIZE; t++) {
            ThreadInfo threadInfo = new ThreadInfo(t);
            threadInfos.add(threadInfo);
            poolExecutor.execute(() -> {
                threadInfo.setName(Thread.currentThread().getName());
                long partSize = maxCaseCount / THREAD_SIZE;
                long partStart = threadInfo.getId() * partSize;
                threadInfo.setStartId(partStart);
                threadInfo.setEndId(partStart + partSize);
                for (long i = 0; i < partSize; i++) {
                    long id = partStart + i;
                    CityBloxx cityBloxx = buildByCaseId2(id);
                    int count = countByCaseId(id);
                    if (cityBloxx.checkLegality2()) {
                        if (count >= highestFloorCount[0] ) {
                            cityBloxx.count(count);
                            lawfulHighestWay.set(cityBloxx);
//                            lawfulHighestWayList.add(cityBloxx);
//                            synchronized (lawfulHighestWayList) {
//                                if (count > highestFloorCount[0]) {
//                                    lawfulHighestWayList.clear();
//                                }
//                                highestFloorCount[0] = count;
//                            }
                            threadInfo.putStepLog(count);
                            highestFloorCount[0] = count;
                        }
                    }
                    if (i > 1024 * 1024 * 1024){
                        threadInfo.putStepLog(count);
                        break;
                    }
                }
                try {
                    String info = "线程" + threadInfo.getName() + "结束";
                    System.out.println(info);
                    PART_MAP.put(info, new Date());
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            barrier.await();
            System.out.println(PART_MAP);
//            String fileName = "D:/!ZKK/cityBloxx.txt";
            String fileName = "/home/cityBloxx.txt";

            Path path = Paths.get(fileName);
            // 使用newBufferedWriter创建文件并写文件
            // 这里使用了try-with-resources方法来关闭流，不用手动关闭
            try (BufferedWriter writer =
                         Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                writer.write(lawfulHighestWay.get().toString());
                writer.newLine();
                writer.write(PART_MAP.toString());
                writer.newLine();
                writer.write(threadInfos.toString());
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            //追加写模式
//            try (BufferedWriter writer =
//                         Files.newBufferedWriter(path,
//                                 StandardCharsets.UTF_8,
//                                 StandardOpenOption.APPEND)){
//                writer.write("Hello World -字母哥!!");
//            } catch (IOException exception) {
//                exception.printStackTrace();
//            }
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
