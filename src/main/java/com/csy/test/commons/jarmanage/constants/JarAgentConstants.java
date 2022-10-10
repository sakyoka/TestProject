package com.csy.test.commons.jarmanage.constants;

import java.io.File;

/**
 * 
 * 描述：代理相关常量
 * @author csy
 * @date 2022年6月9日 下午12:58:31
 */
public class JarAgentConstants {

	/** 代理文件夹名字*/
	public static final String DEFAULT_AGENT_NAME = "agent";
	
	/** premain代理文件夹名字*/
	public static final String DEFAULT_PREMMAIN_NAME = "premain";
	
	/** agentmain代理文件夹名字*/
	public static final String DEFAULT_AGENTMAIN_NAME = "agentmain";
	
	public static final String AGENT_PREMAIN_JAR_FILE = "springboot-agent-premain.jar";
	
	public static final String AGENT_AGENTMAIN_JAR_FILE = "springboot-agent-agentmain.jar";
	
	public static final String LISTENER_SUPPORT_JAR_FILE = "springboot-listener-support.jar";
	
	public static final String LISTENER_ENCODING_JAR_FILE = "FileEncodingApplicationListener.class.cls";
	
	public static final String CACHE_FOLDER = "cache";
	
	/**
	 * 代理文件存放根目录
	 */
	public static final String DEFAULT_AGENT_STORAGE_ROOT_PATH = JarInitBaseConstants.getJarRootPath() 
			+ File.separator + DEFAULT_AGENT_NAME;
	
	/**
	 * 描述：（目录）premain根目录
	 * @author csy 
	 * @date 2022年6月9日 下午1:48:07
	 * @return premain根目录
	 */
	public static String getPremainRootDir(){
		return DEFAULT_AGENT_STORAGE_ROOT_PATH + File.separator + DEFAULT_PREMMAIN_NAME;
	}
	
	/**
	 * 描述：（目录）agentmain根目录
	 * @author csy 
	 * @date 2022年6月9日 下午1:48:12
	 * @return agentmain根目录
	 */
	public static String getAgentmainRootDir(){
		return DEFAULT_AGENT_STORAGE_ROOT_PATH + File.separator + DEFAULT_AGENTMAIN_NAME;
	}
}
