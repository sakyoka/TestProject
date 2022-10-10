package com.csy.test.webui.jarmanage.model;

import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import com.csy.test.commons.jarmanage.bean.JmxRmiInfoBean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JarRumtimeMessageBean {
	
	/** ip*/
	private String ip;
	
	/** 端口*/
	private String port;
	
	/** 堆内存信息*/
	private String heapMemoryUsage;
	
	/** 非堆内存信息*/
	private String nonHeapMemoryUsage;
	
	private String runtimeThreads;
	
	/** 活动线程的当前数目，包括守护线程和非守护线程*/
	private int threadCount;
	
	/** 自从 Java 虚拟机启动或峰值重置以来峰值活动线程计数*/
	private int peakThreadCount;

	/** 自从 Java 虚拟机启动以来创建和启动的线程总数目*/
	private long totalStartedThreadCount;

	/** 活动守护线程的当前数目*/
	private int daemonThreadCount;
	
	public void jmxRmiInfoBeanTransterJarRumtimeMessageBean(JmxRmiInfoBean jmxRmiInfoBean) {

		MemoryMXBean memoryMXBean = jmxRmiInfoBean.getMemoryMXBean();
		this.heapMemoryUsage =  memoryMXBean.getHeapMemoryUsage().toString();
		this.nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage().toString();
		
		ThreadMXBean threadMXBean = jmxRmiInfoBean.getThreadMXBean();
		this.threadCount = threadMXBean.getThreadCount();
		this.peakThreadCount = threadMXBean.getPeakThreadCount();
		this.totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount();
		this.daemonThreadCount = threadMXBean.getDaemonThreadCount();
		
		ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds());
		StringBuilder runtimeThreadBuilder = new StringBuilder();
		for (ThreadInfo threadInfo:threadInfos){
			runtimeThreadBuilder.append(threadInfo.toString()).append("<br>");
		}
		this.runtimeThreads = runtimeThreadBuilder.toString();
		
		//尝试获取ip、端口
		try {
			this.ip = jmxRmiInfoBean.getServerIpPortObjectMBean().getIp();
			this.port = jmxRmiInfoBean.getServerIpPortObjectMBean().getPort();
		} catch (Exception e) {}
	}	
}
