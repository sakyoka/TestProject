package com.csy.test.webui.jarmanage.task.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

import com.csy.test.commons.jarmanage.constants.JarAgentConstants;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.file.FileUtils;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：系统启动-自动处理代理包
 * @author csy
 * @date 2022年9月10日 下午11:49:15
 */
@Log4j2
public class AutoDisposeAgentJarTask implements InitTaskBase{
	
	@Override
	public int order() {
		return 1;
	}
	
	@Override
	public void execute() {
		String premainCacheFoler = JarAgentConstants.getPremainRootDir() 
				+ File.separator + JarAgentConstants.CACHE_FOLDER;
		
		//监听支撑包
		String listenerSupportPath = premainCacheFoler 
				+ File.separator + JarAgentConstants.LISTENER_SUPPORT_JAR_FILE;
		File listenerSupportFile = new File(listenerSupportPath);
		//存在就不执行了
		if (listenerSupportFile.exists()) {
			log.debug("监听支撑jar包已存在，不需要生成");
			return ;
		}else {
			FileUtils.ifNotExistsCreate(premainCacheFoler);
		}
		
		//代理包
		String agentPath = JarAgentConstants.getPremainRootDir() 
				+ File.separator + JarAgentConstants.AGENT_PREMAIN_JAR_FILE;
		File agentFile = new File(agentPath);
		//如果不存在就不处理了
		if (!agentFile.exists()) {
			log.debug("代理包不存在，提取监听支撑jar包失败");
			return ;
		}
		
		try (JarFile jarFile = new JarFile(agentFile);){
			jarFile.stream().forEach((e) -> {
				if (Objects.notNull(e.getName()) 
						&& e.getName().endsWith(JarAgentConstants.LISTENER_SUPPORT_JAR_FILE)) {
					try (InputStream inputStream = jarFile.getInputStream(e);){
						FileUtils.writeFile(listenerSupportFile, inputStream);
					} catch (IOException e1) {
						log.error("生成代理支撑包失败-写入", e1);
					}
				}
			});
		} catch (IOException e) {
			log.error("生成代理支撑包失败", e);
		}		
	}

	@Override
	public String taskName() {
		return null;
	}
}
