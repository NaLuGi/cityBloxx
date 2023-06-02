package cityBloxx.obj;



import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程信息
 * @author zkk
 */
@Getter
@Setter
public class ThreadInfo {
    public ThreadInfo() {
        this.stepLog = new HashMap<>();
        this.stepLog.put(0L,new Date());
    }

    public ThreadInfo(int id) {
        this();
        this.id = id;
    }

    /**
     * 线程id
     */
    private int id;
    /**
     * 线程名称
     */
    private String name;
    /**
     * 线程读取开始id
     */
    private Long startId;
    /**
     * 线程读取最后id
     */
    private Long endId;
    /**
     * 线程检查到的合法的最大值
     */
    private Long maxId;
    /**
     * 线程获取到新最大值时的时间
     */
    private Map<Long, Date> stepLog;

    public void putStepLog(long value){
        this.stepLog.put(value,new Date());
    }
    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startId=" + startId +
                ", endId=" + endId +
                ", maxId=" + maxId +
                ", stepLog=" + stepLog +
                '}';
    }
}
