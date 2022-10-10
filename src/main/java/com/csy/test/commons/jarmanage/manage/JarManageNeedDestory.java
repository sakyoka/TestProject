package com.csy.test.commons.jarmanage.manage;

import java.util.Map;

import com.csy.test.commons.jarmanage.bean.JarGobalConfigBean;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.jarmanage.utils.JarGobalConfigUtils;
import com.csy.test.commons.jarmanage.utils.JarScheduleTaskUtils;
import com.csy.test.commons.jarmanage.utils.JarTaskUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;

/**
 * 
 * 描述：应用关闭需要执行
 * @author csy
 * @date 2022年1月28日 上午10:38:41
 */
public class JarManageNeedDestory {

	private JarManageNeedDestory(){}
	
	/**
	 * 描述：需要销毁的动作
	 * @author csy 
	 * @date 2022年2月12日 上午9:16:50
	 */
	public static void destory(){
		autoStop();
		
		JarScheduleTaskUtils.shutdownNow();
		JarTaskUtils.getJarExecutorService().shutdownNow();
		JarTaskUtils.getLogExecutorService().shutdownNow();
	}
	
	/**
	 * 描述：自动关闭应用
	 * @author csy 
	 * @date 2022年2月12日 上午9:16:38
	 */
	private static void autoStop(){
		JarGobalConfigBean jarGobalConfigBean = JarGobalConfigUtils.readConfig();
		if (Objects.notNull(jarGobalConfigBean) && JarNumberEnum.AUTO_STOP.equalValue(jarGobalConfigBean.getAutoStop())){
			PrintUtils.println("系统配置了全局关闭，正在关闭相关应用...");
			Map<String, JarManageBean> jarManageBeanMap = JarEntityUtils.getUnmodifiableMap();
			if (jarManageBeanMap.isEmpty()){
				PrintUtils.println("没有需要关闭的应用.");
				return;
			}
			jarManageBeanMap.forEach((k, v) -> {
				if (JarNumberEnum.RUNING.equalValue(v.getIsRuning())){
					try {
						JarTaskUtils.kill(k);
					} catch (Exception e) {
						//失败忽略
					}					
				}
			});
		}		
	}
}
