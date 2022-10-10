package com.csy.test.commons.jarmanage.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.jarmanage.base.JarReadWriteBase;
import com.csy.test.commons.jarmanage.manage.JarBeanHolder;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;

/**
 * 
 * 描述：jarId ref pId
 * @author csy
 * @date 2022年1月7日 下午5:08:55
 */
public class JarProcessUtils {
	
	private JarProcessUtils(){}
	
	/**
	 * 操作锁
	 */
	private static final Lock LOCK = new ReentrantLock();
	
	/**
	 * 内存缓存
	 */
	private static Map<String, String> JARUUID_REF_PROCESS;	
	/**
	 * pid存储实现
	 */
	private static JarReadWriteBase<Map<String, String>> jarPidReadWriteBase;
	
	static{
		/**
		 * JARUUID_REF_PROCESS实例化ConcurrentHashMap
		 */
		JARUUID_REF_PROCESS = new ConcurrentHashMap<String, String>();
		
		/**
		 * jarPidReadWriteBase实例化
		 */
		jarPidReadWriteBase = JarBeanHolder.getBean("jarPidReadWrite");
		
		/**
		 * 初始化加载到内存
		 */
		jarPidReadWriteBase.read(JARUUID_REF_PROCESS);
	}
	
	/**
	 * 描述：获取pid
	 * @author csy 
	 * @date 2022年1月13日 下午12:36:28
	 * @param jarId jarId
	 * @return PID
	 */
	public static String get(String jarId){
		String pid = JARUUID_REF_PROCESS.get(jarId);
		Objects.isConditionAssert(StringUtils.isNotBlank(pid), RuntimeException.class, 
				PrintUtils.getFormatString("获取pid失败，jarId没有对应存储的pid, jarId:%s, pid:%s", jarId, pid));
		return pid;
	}
	
	/**
	 * 描述：存储jarId/pid
	 * @author csy 
	 * @date 2022年1月13日 下午12:34:00
	 * @param jarId jarId
	 * @param pId   jar启动的pid
	 */
	public static void storage(String jarId, String pId){
		LOCK.lock();
		try {
			JARUUID_REF_PROCESS.put(jarId, pId);
			jarPidReadWriteBase.write(JARUUID_REF_PROCESS);
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 描述：移除pid
	 * @author csy 
	 * @date 2022年1月13日 下午12:34:28
	 * @param jarId jarId
	 */
	public static void remove(String jarId){
		LOCK.lock();
		try {
			JARUUID_REF_PROCESS.remove(jarId);
			jarPidReadWriteBase.write(JARUUID_REF_PROCESS);
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 描述：移除全部pid
	 * @author csy 
	 * @date 2022年1月13日 下午12:35:14
	 */
	public static void removeAll(){
		LOCK.lock();
		try {
			JARUUID_REF_PROCESS.clear();
			jarPidReadWriteBase.write(JARUUID_REF_PROCESS);
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 
	 * 描述：不给直接操作JARUUID_REF_PROCESS内容
	 * @author csy
	 * @date 2022年8月17日 上午11:14:30
	 * @return Map<String, String>
	 */
	public static Map<String, String> getUnmodifiableMap(){
		Map<String, String> temMap = 
				new HashMap<String, String>(JARUUID_REF_PROCESS.size());
		JARUUID_REF_PROCESS.forEach((k, v) -> {
			temMap.put(k, v);
		});
		return temMap;
	}
}
