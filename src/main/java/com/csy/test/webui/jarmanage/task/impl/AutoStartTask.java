package com.csy.test.webui.jarmanage.task.impl;

import java.util.Map;

import com.csy.test.commons.jarmanage.bean.JarGobalConfigBean;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.manage.JarManageNeetInit;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.jarmanage.utils.JarGobalConfigUtils;
import com.csy.test.commons.jarmanage.utils.JarTaskUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：启动系统-自动重启
 * @author csy
 * @date 2022年9月10日 下午11:45:44
 */
@Log4j2
public class AutoStartTask implements InitTaskBase{

	@Override
	public int order() {
		return 2;
	}
	
	@Override
	public void execute() {
		JarGobalConfigBean jarGobalConfigBean = JarGobalConfigUtils.readConfig();
		if (Objects.notNull(jarGobalConfigBean) && JarNumberEnum.AUTO_RUN.equalValue(jarGobalConfigBean.getAutoRestart())){
			log.debug(PrintUtils.getFormatString("系统配置了全局启动，正在启动相关应用..."));
			Map<String, JarManageBean> jarManageBeanMap = JarEntityUtils.getUnmodifiableMap();
			if (jarManageBeanMap.isEmpty()){
				log.debug(PrintUtils.getFormatString("没有需要启动的应用."));
				return;
			}
			jarManageBeanMap.forEach((k, v) -> {
				if ( JarNumberEnum.UN_RUNING.equalValue(v.getIsRuning())){
					try {
						JarTaskUtils.start(k);
					} catch (Exception e) {
						//失败忽略，不能影响启动
					}					
				}
			});
			log.debug(PrintUtils.getFormatString("正在重新确认应用状态..."));
			JarManageNeetInit.checkJarState(false);
		}else{
			log.debug(PrintUtils.getFormatString("系统没有配置全局启动."));
		}
	}

	@Override
	public String taskName() {
		return null;
	}
}
