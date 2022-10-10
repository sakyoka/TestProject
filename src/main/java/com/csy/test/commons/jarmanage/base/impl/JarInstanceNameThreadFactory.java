package com.csy.test.commons.jarmanage.base.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：给线程重新命名-jar实例
 * @author csy
 * @date 2022年1月12日 上午11:30:19
 */
public class JarInstanceNameThreadFactory implements ThreadFactory{

	public static final String JAR_MANAGE_PREFIXX = "JarManage-instance";
	
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    
    private String preffixName;
    
    private boolean daemon;

    public static JarInstanceNameThreadFactory newJarNameThreadFactoryDefault(){
    	return new JarInstanceNameThreadFactory(JAR_MANAGE_PREFIXX);
    }
    
    public static JarInstanceNameThreadFactory newJarNameThreadFactory(String preffixName){
    	return new JarInstanceNameThreadFactory(preffixName);
    }
    
    public JarInstanceNameThreadFactory(String preffixName, boolean daemon) {
        this.preffixName = preffixName;
        this.daemon = daemon;
    }
    
    public JarInstanceNameThreadFactory(String preffixName) {
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
