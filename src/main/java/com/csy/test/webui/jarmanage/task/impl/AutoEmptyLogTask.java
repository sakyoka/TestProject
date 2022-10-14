package com.csy.test.webui.jarmanage.task.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.constants.JarTaskKeyEnum;
import com.csy.test.commons.jarmanage.manage.JarManageNeetInit;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.jarmanage.utils.JarScheduleTaskUtils;
import com.csy.test.commons.utils.Properties;
import com.csy.test.commons.utils.file.FileUtils;
import com.csy.test.webui.systemconfig.component.SpringContextHolder;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

/**
 * 
 * 描述：系统启动-自动清空日志
 * @author csy
 * @date 2022年9月10日 下午11:51:30
 */
public class AutoEmptyLogTask implements InitTaskBase{

	@Override
	public int order() {
		return 2;
	}
	
	@Override
	public void execute() {
		emptyLogTask();
	}
	
	public static final String SYSTEM_REMARK = "system";

	/**
	 * 
	 * 描述：清空日志内容
	 * @author csy
	 * @date 2022年8月24日 下午5:55:08
	 * @param logPath
	 */
	public static void emptyLog(String logPath, String jarId){
		//默认日志最大值200M，可由log.max.size配置
		long maxSize = Long.valueOf(Properties.get("log.max.size", "200"));
		File logFile = new File(logPath);
		if (logFile.exists()){
			long fileSize = logFile.length();
			long fileMbSize = fileSize/(1024 * 1024);
			if (fileMbSize >= maxSize){
				//清空文件内容。当日志文件被占用时，linux把文件清空没问题，但是window会失败（存在文件独占问题）
				FileUtils.writeFile(logFile, "自动清空日志内容。", false);
				//告诉前端控制台，清除了jarId对应日志内容
				SimpMessagingTemplate simpMessagingTemplate = 
						SpringContextHolder.getBean(SimpMessagingTemplate.class);
				simpMessagingTemplate.convertAndSend("/topic/emptylog", jarId);
			}
		}	
	}
	
	/**
	 * 
	 * 描述：清空日志内容
	 * @author csy
	 * @date 2022年8月24日 下午5:55:20
	 */
	public static void emptyLogTask(){
		
		//立刻执行，而且每半个小时执行一次
		JarScheduleTaskUtils.addTask(JarTaskKeyEnum.EMPTY_LOG_TASK.getMark(), () -> {
	    	synchronized (JarManageNeetInit.class) {
	    		Map<String, JarManageBean> jarManageBeanMap = JarEntityUtils.getUnmodifiableMap();
	    		
	    		Map<String, String> jarLogPathMap = new HashMap<String, String>(jarManageBeanMap.size() + 1);
	    		if (!jarManageBeanMap.isEmpty()){
		    		jarManageBeanMap.forEach((k, v) -> {
		    			jarLogPathMap.put(k, JarInitBaseConstants.getJarRootPath() + v.getLogPath());
		    		});
	    		}

	    		String systemLog = JarInitBaseConstants.getSystemLogFilePath();
	    		jarLogPathMap.put(SYSTEM_REMARK, systemLog);
	    		
	    		jarLogPathMap.forEach((k, v) -> {
	    			try {
	    				emptyLog(v, k);
					} catch (Exception e) {/**清空失败不能影响失败*/}	    			
	    		});
			}
		}, 0, 30 * 60 * 1000,TimeUnit.MILLISECONDS);
	}
}
