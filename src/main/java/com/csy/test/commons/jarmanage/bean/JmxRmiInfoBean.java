package com.csy.test.commons.jarmanage.bean;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.csy.test.commons.utils.Objects;
import com.sun.management.OperatingSystemMXBean;

import lombok.extern.log4j.Log4j2;


/**
 * 
 * 描述：JMX远程操作信息对象
 * @author csy
 * @date 2022年6月1日 下午4:44:52
 */
@Log4j2
public class JmxRmiInfoBean {

	private String ip;
	
	private Integer port;
	
	private MemoryMXBean memoryMXBean;
	
	private RuntimeMXBean runtimeMXBean;
	
	private ThreadMXBean threadMXBean;
	
	private OperatingSystemMXBean operatingSystemMXBean;
	
	private ServerIpPortObjectMBean serverIpPortObjectMBean;
	
	private JMXConnector connector;
	
	public static JmxRmiInfoBean newIntance(String ip, Integer port){
		return new JmxRmiInfoBean(ip, port);
	}
	
	private JmxRmiInfoBean(){}
	
	private JmxRmiInfoBean(String ip, Integer port){
		this.ip = ip;
		this.port = port;
		initParam();
	}
	
	/**
	 * 描述：初始化获取MXBean
	 * @author csy 
	 * @date 2022年6月1日 下午4:44:24
	 */
	public void initParam(){
		try {
	        connector = this.getConnector();
	        MBeanServerConnection mbsc = connector.getMBeanServerConnection();
	        memoryMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
	        runtimeMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
	        threadMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
	        operatingSystemMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		} catch (Exception e) {
			throw new RuntimeException("获取启动信息失败，请确认对应jar是否已经启动了", e);
		}
		
		try {
			 serverIpPortObjectMBean = this.getBean("com.csy.test.agent:type=ServerIpPortObject", ServerIpPortObjectMBean.class);
		} catch (Exception e) {
			log.error("获取目标对象ip port失败", e);
		}
	}
	
	/**
	 * 描述：远程获取bean实例
	 * @author csy 
	 * @date 2022年6月1日 下午4:42:37
	 * @param objectName
	 * @param clz
	 * @return T
	 */
	public <T> T getBean(String objectName, Class<T> clz){
		try {
			this.getConnector();
	        MBeanServerConnection mbsc = connector.getMBeanServerConnection();
	        return (T)JMX.newMBeanProxy(mbsc, new ObjectName(objectName), clz);	
		} catch (Exception e) {
			throw new RuntimeException("远程获取实例失败", e);
		}
	}
	
	/**
	 * 描述：获取JMX连接
	 * @author csy 
	 * @date 2022年6月1日 下午4:42:07
	 * @return JMXConnector
	 */
	public JMXConnector getConnector(){
		try {
	        if (connector != null){
	        	return connector;
	        }
	        String jmxURL = "service:jmx:rmi:///jndi/rmi://" + ip + ":" + port + "/jmxrmi";
	        JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);

	        Map<String, Object> map = new HashMap<String, Object>();
	        String[] credentials = new String[] { "monitorRole", "QED" };
	        map.put("jmx.remote.credentials", credentials);
	        connector = JMXConnectorFactory.connect(serviceURL, map);
	        return connector;
		} catch (Exception e) {
			throw new RuntimeException("获取连接失败", e);
		}	
	}
	
	/**
	 * 描述：获取pid
	 * @author csy 
	 * @date 2022年1月13日 上午10:52:44
	 * @return pid
	 */
	public String getPid(){
		if (Objects.isNull(runtimeMXBean)){
			initParam();
			Objects.notNullAssert(runtimeMXBean, "获取runtimeMXBean失败, runtimeMXBean is null");
		}
		String jvmName = runtimeMXBean.getName();
		return jvmName.split("@")[0];
	}
	
	/**
	 * 描述：关闭连接
	 * @author csy 
	 * @date 2022年1月26日 下午4:33:57
	 */
	public void closeConnetor(){
		if (Objects.notNull(connector)){
			try {
				connector.close();
			} catch (IOException e) {

			}
		}
	}

	public MemoryMXBean getMemoryMXBean() {
		return memoryMXBean;
	}

	public RuntimeMXBean getRuntimeMXBean() {
		return runtimeMXBean;
	}

	public ThreadMXBean getThreadMXBean() {
		return threadMXBean;
	}

	public OperatingSystemMXBean getOperatingSystemMXBean() {
		return operatingSystemMXBean;
	}

	public ServerIpPortObjectMBean getServerIpPortObjectMBean() {
		return serverIpPortObjectMBean;
	}
}
