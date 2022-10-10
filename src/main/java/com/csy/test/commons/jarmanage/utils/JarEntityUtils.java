package com.csy.test.commons.jarmanage.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.csy.test.commons.entity.utils.EntityUtils;
import com.csy.test.commons.jarmanage.base.JarReadWriteBase;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.manage.JarBeanHolder;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：内存储存实例
 * @author csy
 * @date 2022年1月7日 下午4:27:07
 */
public class JarEntityUtils {
	
	private JarEntityUtils(){}
	
	/**
	 * 操作锁
	 */
	private static final Lock LOCK = new ReentrantLock();
	
	/**
	 * 内存缓存
	 */
	private static Map<String, JarManageBean> JARUUID_REF_MAP;
		
	/**
	 * 存储与读取实现类
	 */
	private static JarReadWriteBase<Map<String, JarManageBean>> jarEntityReadWriteBase;
	
	static{
		
		JARUUID_REF_MAP = new ConcurrentHashMap<String, JarManageBean>();
		
		jarEntityReadWriteBase = JarBeanHolder.getBean("jarEntityReadWrite");
		
		//初始化加载到内存
		jarEntityReadWriteBase.read(JARUUID_REF_MAP);
	}
	
	/**
	 * 描述：获取jar配置
	 * @author csy 
	 * @date 2022年1月13日 下午12:36:48
	 * @param jarId jarId
	 * @return JAR配置对象
	 */
	public static JarManageBean get(String jarId){
		JarManageBean jarManageBean = JARUUID_REF_MAP.get(jarId);
		Objects.notNullAssert(jarManageBean, "jarId找不到对应的配置内容,jarId:" + jarId);
		return EntityUtils.deepCopy(JARUUID_REF_MAP.get(jarId));

	}
	
	/**
	 * 描述：存储JAR配置对象
	 * @author csy 
	 * @date 2022年1月13日 下午12:37:25
	 * @param jarManageBean JAR配置对象
	 */
	public static void storage(JarManageBean jarManageBean){
		LOCK.lock();
		try {
			JarManageBean storageBean = JARUUID_REF_MAP.get(jarManageBean.getJarId());
			jarManageBean.setDeleteFlag(JarNumberEnum.UN_DELETE.getValue());
			if (Objects.notNull(storageBean)){
				BeanUtils.copyProperties(jarManageBean, storageBean);
				JARUUID_REF_MAP.put(jarManageBean.getJarId(), storageBean);
			}else{
				JARUUID_REF_MAP.put(jarManageBean.getJarId(), jarManageBean);
			}
			//总
			jarEntityReadWriteBase.write(JARUUID_REF_MAP);
			
			storageSingleEntityCache(jarManageBean, JarNumberEnum.UN_DELETE.getValue());
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 描述：存储单个配置实例
	 * @author csy 
	 * @date 2022年3月4日 上午8:52:01
	 * @param jarManageBean
	 * @param deleteFlag 删除标识
	 */
	public static void storageSingleEntityCache(JarManageBean jarManageBean, Integer deleteFlag){
		//单个
		jarManageBean.setDeleteFlag(deleteFlag);
		String cacheEntityFilePath = JarInitBaseConstants.getJarRootPath() 
				+ jarManageBean.getJarDir() + JarInitBaseConstants.DETAULT_SINGLE_ENTITY_NAME;
		String contents = JSON.toJSONString(jarManageBean, SerializerFeature.PrettyFormat, 
				SerializerFeature.WriteDateUseDateFormat);
		FileUtils.ifNotExistsCreate(cacheEntityFilePath);
		FileUtils.writeFile(cacheEntityFilePath, contents);		
	}
	
	/**
	 * 描述：移除JAR配置对象
	 * @author csy 
	 * @date 2022年1月13日 下午12:37:22
	 * @param jarId jarId
	 */
	public static void remove(String jarId){
		LOCK.lock();
		try {
			JarManageBean jarManageBean = get(jarId);
			storageSingleEntityCache(jarManageBean, JarNumberEnum.DELETED.getValue());
			JARUUID_REF_MAP.remove(jarId);
			jarEntityReadWriteBase.write(JARUUID_REF_MAP);
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 描述：移除全部JAR配置对象
	 * @author csy 
	 * @date 2022年1月13日 下午12:37:18
	 */
	public static void removeAll(){
		LOCK.lock();
		try {
			JARUUID_REF_MAP.forEach((k, v) -> {
				storageSingleEntityCache(v, JarNumberEnum.DELETED.getValue());
			});
			JARUUID_REF_MAP.clear();
			jarEntityReadWriteBase.write(JARUUID_REF_MAP);
		} finally {
			LOCK.unlock();
		}
	}
	
	/**
	 * 
	 * 描述：不给外部直接操作JARUUID_REF_MAP里面的内容
	 * @author csy
	 * @date 2022年8月17日 上午11:10:02
	 * @return Map<String, JarManageBean>
	 */
	public static Map<String, JarManageBean> getUnmodifiableMap(){
		Map<String, JarManageBean> temMap = 
				new HashMap<String, JarManageBean>(JARUUID_REF_MAP.size());
		JARUUID_REF_MAP.forEach((k, v) -> {
			temMap.put(k, EntityUtils.deepCopy(v));
		});
		return temMap;
	}
}
