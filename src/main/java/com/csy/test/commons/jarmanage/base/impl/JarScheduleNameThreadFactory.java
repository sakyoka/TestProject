package com.csy.test.commons.jarmanage.base.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：给线程重新命名-定时任务线程池
 * @author csy
 * @date 2022年2月22日 下午5:01:27
 */
public class JarScheduleNameThreadFactory implements ThreadFactory{

	public static final String JAR_MANAGE_PREFIXX = "JarManage-schedule";
	
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    
    private String preffixName;
    
    private boolean daemon;

    public static JarScheduleNameThreadFactory newJarNameThreadFactoryDefault(){
    	return new JarScheduleNameThreadFactory(JAR_MANAGE_PREFIXX);
    }
    
    public static JarScheduleNameThreadFactory newJarNameThreadFactory(String preffixName){
    	return new JarScheduleNameThreadFactory(preffixName);
    }
    
    public JarScheduleNameThreadFactory(String preffixName, boolean daemon) {
        this.preffixName = preffixName;
        this.daemon = daemon;
    }
    
    public JarScheduleNameThreadFactory(String preffixName) {
        this(preffixName, false);
    }

    @Override
    public Thread newThread(Runnable r) {
    	String suffixName = r.toString();
        Thread thread = new Thread(r);
        int i = poolNumber.getAndIncrement();
        String threadName = preffixName + "-" + i + (Objects.notNull(suffixName) ? "-" + suffixName : ""); 
        thread.setName(threadName);
        thread.setDaemon(daemon);
        return thread;
    }
}
