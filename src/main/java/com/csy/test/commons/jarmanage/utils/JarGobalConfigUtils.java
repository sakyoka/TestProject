package com.csy.test.commons.jarmanage.utils;

import com.csy.test.commons.jarmanage.base.JarReadWriteBase;
import com.csy.test.commons.jarmanage.bean.JarGobalConfigBean;
import com.csy.test.commons.jarmanage.manage.JarBeanHolder;

/**
 * 
 * 描述：全局配置
 * @author csy
 * @date 2022年1月7日 下午3:38:37
 */
public class JarGobalConfigUtils {
	
	private JarGobalConfigUtils(){}
	
	/**
	 * 全局参数读写实现类
	 */
	private static JarReadWriteBase<JarGobalConfigBean> JAR_READ_WRITE_BASE;
	
	static{
		JAR_READ_WRITE_BASE = JarBeanHolder.getBean("jarGobalConfigReadWrite");
	}
		
	/**
	 * 描述：存储配置内容
	 * @author csy 
	 * @date 2022年1月7日 下午3:38:20
	 * @param config
	 */
	public synchronized static void stoage(JarGobalConfigBean jarGobalConfigBean){
		JAR_READ_WRITE_BASE.write(jarGobalConfigBean);
	}
	
	/**
	 * 描述：读取配置内容
	 * @author csy 
	 * @date 2022年1月7日 下午3:38:37
	 * @return 配置内容
	 */
	public static JarGobalConfigBean readConfig(){
		return JAR_READ_WRITE_BASE.read(null);
	}
}
