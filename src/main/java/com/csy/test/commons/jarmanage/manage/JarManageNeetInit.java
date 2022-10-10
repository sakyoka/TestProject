package com.csy.test.commons.jarmanage.manage;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csy.test.commons.jarmanage.bean.JarApplicatioinAboutMessageBean;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.JarAgentConstants;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.jarmanage.utils.JarJMXPortUtils;
import com.csy.test.commons.utils.Command;
import com.csy.test.commons.utils.OsUtils;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.commons.utils.file.FileUtils;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述:jarManage全部初始化，放在这里被调用
 * <br>需要主动调用初始化一次
 * @author csy
 * @date 2022年1月15日 下午9:45:21
 */
@Log4j2
public class JarManageNeetInit {
	
	private JarManageNeetInit(){}

	public static void init() {
		
		log.debug(PrintUtils.getFormatString("准备初始化jarManage配置..."));
		initJarManagePath();
		initJarManageAboutFile();
		log.debug(PrintUtils.getFormatString("初始化jarManage配置完毕."));
				
		log.debug(PrintUtils.getFormatString("开始更新jar状态..."));
		checkJarState(true);
		log.debug(PrintUtils.getFormatString("更新jar状态完毕。"));
		
		log.debug(PrintUtils.getFormatString("初始化JMX port分配..."));
		JarJMXPortUtils.init();
		log.debug(PrintUtils.getFormatString("初始化JMX port分配完毕."));
	}

	/**
	 * 
	 * 描述：初始化目录
	 * @author csy
	 * @date 2022年1月15日 下午9:48:05
	 */
	private static void initJarManagePath(){
		Map<String, String> pathMap = new HashMap<String, String>();
		pathMap.put("jar包存储根目录", JarInitBaseConstants.getJarStorageDir());
		pathMap.put("jar对应log存储目录", JarInitBaseConstants.getJarLogDir());
		pathMap.put("系统文件对应存储目录", JarInitBaseConstants.getSystemDir());
		pathMap.put("缓存文件对应存储目录", JarInitBaseConstants.getCacheUoloadDir());
		pathMap.put("备份文件对应存储目录", JarInitBaseConstants.getBakDir());
		pathMap.put("premain目录", JarAgentConstants.getPremainRootDir());
		pathMap.put("agentmain目录", JarAgentConstants.getAgentmainRootDir());
		pathMap.forEach((k, v) -> {
			if (!Paths.get(v).toFile().exists()){
				FileUtils.ifNotExistsCreate(v);
			}
		});
	}
	
	/**
	 * 
	 * 描述：初始化相关文件
	 * @author csy
	 * @date 2022年1月15日 下午10:03:16
	 */
	private static void initJarManageAboutFile() {
		FileUtils.ifNotExistsCreate(JarInitBaseConstants.getJarGobalConfigFilePath());
		FileUtils.ifNotExistsCreate(JarInitBaseConstants.getJarEntityFilePath());
		FileUtils.ifNotExistsCreate(JarInitBaseConstants.getJarProcessFilePath());
	}
	
	/**
	 * 描述：检测jar状态
	 * @author csy 
	 * @date 2022年1月28日 上午9:02:32
	 */
	public static JarApplicatioinAboutMessageBean checkJarState(boolean update){
		Map<String, JarManageBean> jarManageBeanMap = JarEntityUtils.getUnmodifiableMap();
		if (jarManageBeanMap.isEmpty()){
			return JarApplicatioinAboutMessageBean.defaultBean();
		}
		List<String> javaPids = getJavaPids();
		Set<Entry<String, JarManageBean>> sets = jarManageBeanMap.entrySet();
		JarManageBean jarManageBean = null;
		List<String> systemRunPids = new ArrayList<String>();
		int nonStart = 0;
		int runing = 0;
		int unruning = 0;
		for (Entry<String, JarManageBean> entry:sets){
			jarManageBean = entry.getValue();
			if (StringUtils.isBlank(jarManageBean.getPId())){
				nonStart++;
				if (update){
					jarManageBean.setPId("");
					jarManageBean.setIsRuning(JarNumberEnum.UN_RUNING.getValue());
					jarManageBean.setIsAlive(JarNumberEnum.UN_ALIVE.getValue());
					JarEntityUtils.storage(jarManageBean);
				}
				continue;
			}
			systemRunPids.add(jarManageBean.getPId());
			if (javaPids.contains(jarManageBean.getPId())){
				jarManageBean.setIsRuning(JarNumberEnum.RUNING.getValue());
				
				//这里虽然进程存在但是应用接口不知到是否能调通，需要判断是否正常
				jarManageBean.setIsAlive(JarNumberEnum.ALIVE.getValue());
				runing++;
			}else{
				jarManageBean.setPId("");
				jarManageBean.setIsRuning(JarNumberEnum.UN_RUNING.getValue());
				jarManageBean.setIsAlive(JarNumberEnum.UN_ALIVE.getValue());
				unruning++;
			}
			
			if (update){
				JarEntityUtils.storage(jarManageBean);
			}
		}
		if (update){
			String s1 = PrintUtils.getFormatString("存在相关java进程 ,pid：%s", javaPids);
			String s2 = PrintUtils.getFormatString("本机启动过的进程 ,pid：%s", systemRunPids);
			String s3 = PrintUtils.getFormatString("其中共应用：%s, 没有启动：%s, 运行着：%s, 已经停掉的：%s", sets.size(), nonStart, runing, unruning);
			log.debug(s1);
			log.debug(s2);
			log.debug(s3);
		}
		return JarApplicatioinAboutMessageBean.builder()
				.javaProcessPids(javaPids)
				.applicationPids(systemRunPids)
				.total(sets.size())
				.nonStart(nonStart)
				.runing(runing)
				.unruning(unruning)
				.build();
	}
	
	/**
	 * 描述：获取系统jar信息
	 * @author csy 
	 * @date 2022年1月28日 上午11:13:12
	 * @return jar信息
	 */
	public static JarApplicatioinAboutMessageBean getJarState(){
		return checkJarState(false);
	}
	
	/**
	 * 描述：获取java相关进程pid
	 * <br> 重点：如果系统不支持（没有）jps命令，会导致启动时候丢失运行状态
	 * @author csy 
	 * @date 2022年1月28日 上午10:29:49
	 * @return java相关进程pid集合
	 */
	private static List<String> getJavaPids(){
		Process process = null;
		List<String> javaPids = new ArrayList<String>();
		try {
			//执行java命令
			Command command = Command.getBuilder();
			if (OsUtils.isWindow()){
				command.commandStr("jps -ml");
			}else{
				//或者直接指定jdk的jps , xxs/java/bin/jps -l
				command.commandArr("/bin/sh", "-c", "cd / && source /etc/profile && jps -l");
			}
			String printResult = command.toStringContents();
			if (StringUtils.isBlank(printResult)){
				return javaPids;
			}
			Pattern pattern = Pattern.compile("(^|\\s)[^a-zA-z\\s]*\\d+");
			Matcher matcher = pattern.matcher(printResult);
			while (matcher.find()){
				javaPids.add((matcher.group()).replaceAll("(\r\n|\r|\n|\n\r)", "").replaceAll(" ", ""));
			}
			process = command.getProcess();
		} finally {
			if (process != null){
				process.destroy();
			}
		}	
		return javaPids;
	}
}
