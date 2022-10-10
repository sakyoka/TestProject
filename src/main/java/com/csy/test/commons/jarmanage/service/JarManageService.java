package com.csy.test.commons.jarmanage.service;

import java.util.List;

import com.csy.test.commons.jarmanage.bean.JarJdkManageBean;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.bean.JarManageDto;

/**
 * 
 * 描述：jar管理
 * @author csy
 * @date 2022年1月7日 下午3:11:02
 */
public interface JarManageService {
	
	/**
	 * 描述：保存jar包信息
	 * @author csy 
	 * @date 2022年1月7日 下午3:00:19
	 * @param jarManageDto
	 * @return jarId
	 */
	String storageProject(JarManageDto jarManageDto);
	
	/**
	 * 描述：移除项目
	 * <br>移除项目并且停止运行的项目
	 * @author csy 
	 * @date 2022年1月7日 下午3:09:38
	 * @param jarIds jar uuid 集合
	 */
	void removeProjectByJarIds(List<String> jarIds);
	
	/**
	 * 描述：启动jar
	 * @author csy 
	 * @date 2022年1月7日 下午3:49:39
	 * @param jarId jar uuid
	 */
	void jarStart(String jarId);
	
	/**
	 * 描述：停止jar
	 * @author csy 
	 * @date 2022年1月7日 下午3:49:51
	 * @param jarId jar uuid
	 */
	void jarStop(String jarId);
	
	/**
	 * 描述：重启应用
	 * @author csy 
	 * @date 2022年1月26日 下午4:58:05
	 * @param jarId
	 */
	void jarRestart(String jarId);
	
	/**
	 * 描述：停止全部jar
	 * @author csy 
	 * @date 2022年1月7日 下午3:50:11
	 */
	void jarAllStop();
	
	/**
	 * 描述：启动所有的jar
	 * @author csy 
	 * @date 2022年1月7日 下午3:50:25
	 */
	void jarAllStart();
	
	/**
	 * 描述：获取jar集合
	 * @author csy 
	 * @date 2022年1月7日 下午5:38:00
	 * @return List<JarManageBean>
	 */
	List<JarManageBean> getJarManageList();
	
	/**
	 * 描述：给jar追加JDK配置对象
	 * @author csy 
	 * @date 2022年1月12日 下午2:58:44
	 * @param jarId
	 * @param jarJdkManageBean JDK配置对象
	 */
	void addJDKConfigForJar(String jarId, JarJdkManageBean jarJdkManageBean);
	
	/**
	 * 描述：根据jarId获取配置对象
	 * @author csy 
	 * @date 2022年1月24日 上午9:07:57
	 * @param jarId
	 * @return  JarManageBean
	 */
	JarManageBean get(String jarId);

	/**
	 * 描述：jar配置修复
	 * @author csy 
	 * @date 2022年3月4日 下午2:18:53
	 */
	void repeat();
}
