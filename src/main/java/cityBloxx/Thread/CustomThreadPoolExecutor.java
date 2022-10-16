package cityBloxx.Thread;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author zkk
 */
public class CustomThreadPoolExecutor {
    private ThreadPoolExecutor pool = null;
    private static final Logger log = Logger.getLogger("多线程测试");


    /**
     * 线程池初始化方法
     *
     * corePoolSize 核心线程池大小----1
     * maximumPoolSize 最大线程池大小----3
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间----30+单位TimeUnit
     * TimeUnit keepAliveTime时间单位----TimeUnit.MINUTES
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(5)====5容量的阻塞队列
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时,
     *                          即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)),
     *                                任务会交给RejectedExecutionHandler来处理
     */
    public void init() {
        pool = new ThreadPoolExecutor(
                1,
                3,
                30,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(5),
                new CustomThreadFactory(),
                new CustomRejectedExecutionHandler());
    }
    public void init(int corePoolSize) {
        pool = new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize * 3,
                30,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(5),
                new CustomThreadFactory(),
                new CustomRejectedExecutionHandler());
    }


    public void destory() {
        if(pool != null) {
            pool.shutdownNow();
        }
    }


    public ExecutorService getCustomThreadPoolExecutor() {
        return this.pool;
    }

    public int getActiveCount(){
        return this.pool.getActiveCount();
    }





    // 测试构造的线程池
    public static void main(String[] args) {
        CustomThreadPoolExecutor exec = new CustomThreadPoolExecutor();
        // 1.初始化
        exec.init();
        ExecutorService pool = exec.getCustomThreadPoolExecutor();
        System.out.println("项目：" + 1 + "的定时任务开启! 30分钟后查询是否");
        AtomicInteger time= new AtomicInteger();
        pool.execute(() -> {
            try {
                boolean isAllCheck = false;
                while (!isAllCheck) {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("等待"+time+"s完成\n");
                    time.addAndGet(10);
                    if (time.intValue()==20){
                        isAllCheck = true;
                    }
                }
                System.out.println("销毁了\n");
                exec.destory();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("做完了\n");
        // 2.销毁----此处不能销毁,因为任务没有提交执行完,如果销毁线程池,任务也就无法执行了
    }

}
