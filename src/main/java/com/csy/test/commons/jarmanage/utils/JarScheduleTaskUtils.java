package com.csy.test.commons.jarmanage.utils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.csy.test.commons.jarmanage.base.impl.JarScheduleNameThreadFactory;
import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：任务调度工具类
 * @author csy
 * @date 2022年2月22日 下午5:21:39
 */
public class JarScheduleTaskUtils {

	private JarScheduleTaskUtils(){}
	
    /**
     * 定时任务线程池
     */
    private static ScheduledExecutorService JAR_SCHEDULE_SERVICE;
    
    static{
    	JAR_SCHEDULE_SERVICE = Executors.newScheduledThreadPool(12, JarScheduleNameThreadFactory.newJarNameThreadFactoryDefault());
    }
    
    /**
     * 描述：定时任务线程池
     * @author csy 
     * @date 2022年2月22日 下午5:03:43
     * @return
     */
    public static ScheduledExecutorService getScheduleExecutorService(){
    	return JAR_SCHEDULE_SERVICE;
    }
	
    @SuppressWarnings("rawtypes")
	private static final Map<String, ScheduledFuture> KEY_SCHEDULE_MAP = new ConcurrentHashMap<String, ScheduledFuture>();
    
    /**
     * 描述：添加定时任务(在hour时候执行任务)
     * @author csy 
     * @date 2022年2月22日 下午5:42:14
     * @param key    任务标识
     * @param hour   小时(0~24)
     * @param runnable 任务
     */
    public static void addTaskForHour(String key, int hour, Runnable runnable){
    	LocalDateTime localDateTime = LocalDateTime.now();
    	int nowHour = localDateTime.getHour();
    	int result = hour - nowHour;
    	long oneDay = 24 * 60 * 60 * 1000;
    	//如果是小于0就是明天才执行，加一天时间;如果大于等于0就是今天执行
    	long initialDelay = result * 60 * 60 * 1000 + (result < 0 ? oneDay : 0);
    	addTask(key, runnable, initialDelay, oneDay, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 描述：添加定时任务
     * @author csy 
     * @date 2022年2月22日 下午5:38:23
     * @param runnable  任务
     * @param initialDelay 延迟时间
     * @param period    两次开始执行最小间隔时间
     */
    public static synchronized void addTask(String key, Runnable runnable, long initialDelay, long period, TimeUnit timeUnit){
    	Objects.notNullAssert(key, "任务标识不能为空");
    	
    	removeTask(key);
    	
    	ScheduledFuture<?> scheduledFuture = JAR_SCHEDULE_SERVICE.scheduleAtFixedRate(runnable, initialDelay, period, timeUnit);
    	KEY_SCHEDULE_MAP.put(key, scheduledFuture);
    }
    
    /**
     * 描述：停止任务
     * @author csy 
     * @date 2022年2月22日 下午5:17:48
     * @param key
     */
    public static void stopTask(String key){
    	Objects.isConditionAssert(KEY_SCHEDULE_MAP.containsKey(key), 
    			RuntimeException.class, "移除失败，没有对应的任务");
    	KEY_SCHEDULE_MAP.get(key).cancel(true);
    }
    
    /**
     * 描述：移除任务（先停止再移除）
     * @author csy 
     * @date 2022年2月22日 下午5:17:54
     * @param key
     */
    public static void removeTask(String key){
    	if (KEY_SCHEDULE_MAP.containsKey(key)){
	    	stopTask(key);
			KEY_SCHEDULE_MAP.remove(key);
    	}
    }
    
    /**
     * 描述：关闭
     * @author csy 
     * @date 2022年2月22日 下午5:18:23
     */
    public static void shutdown(){
    	JAR_SCHEDULE_SERVICE.shutdown();
    }
    
    /**
     * 描述：关闭
     * @author csy 
     * @date 2022年2月23日 上午9:01:38
     */
    public static void shutdownNow(){
    	JAR_SCHEDULE_SERVICE.shutdownNow();
    }
}
