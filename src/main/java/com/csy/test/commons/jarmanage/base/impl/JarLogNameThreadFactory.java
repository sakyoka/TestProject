package com.csy.test.commons.jarmanage.base.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：给线程重新命名-日志
 * @author csy
 * @date 2022年1月12日 上午11:30:19
 */
public class JarLogNameThreadFactory implements ThreadFactory{

	public static final String JAR_MANAGE_PREFIXX = "JarManage-log";
	
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    
    private String preffixName;
    
    private boolean daemon;

    public static JarLogNameThreadFactory newLogNameThreadFactoryDefault(){
    	return new JarLogNameThreadFactory(JAR_MANAGE_PREFIXX);
    }
    
    public static JarLogNameThreadFactory newLogNameThreadFactoryDefault(String preffixName){
    	return new JarLogNameThreadFactory(preffixName);
    }
    
    public JarLogNameThreadFactory(String preffixName, boolean daemon) {
        this.preffixName = preffixName;
        this.daemon = daemon;
    }
    
    public JarLogNameThreadFactory(String preffixName) {
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
