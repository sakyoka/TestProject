package com.csy.test.webui.taskmanage.utils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;

import com.csy.test.commons.utils.Objects;
import com.csy.test.webui.systemconfig.component.SpringContextHolder;

/**
 * 
 * 描述：任务注册
 * @author csy
 * @date 2022年9月29日 下午1:51:44
 */
public class TaskRegisterUtils {

	private static final Map<String, ScheduledFuture<?>> TASK_MAP = 
			new ConcurrentHashMap<String, ScheduledFuture<?>>();
	
	private static final Lock LOCK = new ReentrantLock();
	
	private static TaskScheduler taskScheduler;
	
	static {
		taskScheduler = SpringContextHolder.getBean(TaskScheduler.class);
	}
	
	/**
	 * 
	 * 描述：添加任务，以Trigger形式执行任务
	 * @author csy
	 * @date 2022年9月29日 下午2:04:31
	 * @param taskId
	 * @param task
	 * @param trigger
	 */
	public static void register(String taskId, Runnable task, Trigger trigger){
		LOCK.lock();
		try {
			Objects.isConditionAssert(!TASK_MAP.containsKey(taskId), 
					RuntimeException.class, "不能重复添加任务，taskId:" + taskId);
			ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, trigger);
			TASK_MAP.put(taskId, scheduledFuture);	
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 
	 * 描述：添加任务，以Trigger形式执行任务
	 * @author csy
	 * @date 2022年9月29日 下午2:13:13
	 * @param taskId
	 * @param task
	 * @param corn
	 */
	public static void register(String taskId, Runnable task, String corn){
		register(taskId, task, new CronTrigger(corn));
	}
	
	/**
	 * 
	 * 描述：添加任务，以指定时间执行任务
	 * @author csy
	 * @date 2022年9月29日 下午2:04:59
	 * @param taskId
	 * @param task
	 * @param startTime
	 */
	public static void register(String taskId, Runnable task, Date startTime){
		LOCK.lock();
		try {
			Objects.isConditionAssert(!TASK_MAP.containsKey(taskId), 
					RuntimeException.class, "不能重复添加任务，taskId:" + taskId);
			ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, startTime);
			TASK_MAP.put(taskId, scheduledFuture);
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 
	 * 描述：停止任务
	 * @author csy
	 * @date 2022年9月29日 下午2:05:21
	 * @param taskId
	 */
	public static void stop(String taskId){
		if (TASK_MAP.containsKey(taskId)){
			TASK_MAP.get(taskId).cancel(true);
		}
	}
	
	/**
	 * 
	 * 描述：移除任务
	 * @author csy
	 * @date 2022年9月29日 下午2:10:01
	 * @param taskId
	 */
	public static void remove(String taskId){
		LOCK.lock();
		try {
			if (TASK_MAP.containsKey(taskId)){
				stop(taskId);
				TASK_MAP.remove(taskId);
			}			
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 
	 * 描述：移除所有的任务
	 * @author csy
	 * @date 2022年9月29日 下午2:13:23
	 */
	public static void removeAll(){
		TASK_MAP.forEach((taskId, futrue) -> {
			remove(taskId);
		});
	}
}
