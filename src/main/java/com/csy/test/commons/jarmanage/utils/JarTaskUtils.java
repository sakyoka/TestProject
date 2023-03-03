package com.csy.test.commons.jarmanage.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.excel.utils.CollectionUtils;
import com.csy.test.commons.jarmanage.base.impl.JarInstanceNameThreadFactory;
import com.csy.test.commons.jarmanage.base.impl.JarLogNameThreadFactory;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.bean.JarRefJmxRmiIpPortBean;
import com.csy.test.commons.jarmanage.bean.JmxRmiInfoBean;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.exception.JarManageException;
import com.csy.test.commons.utils.Command;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.OsUtils;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.commons.utils.Properties;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：jar线程任务
 * @author csy
 * @date 2022年1月12日 上午11:35:45
 */
@Log4j2
public class JarTaskUtils {
	
	private JarTaskUtils(){}
	
	/**
	 * jar执行后，主线程沉睡多少秒
	 */
	private static long AFTER_START_SLEEP_TIME;
	
	/**
	 * jar执行后，循环次数沉睡次数
	 */
	private static int INTERVAL_TIMES;

	/**
	 * jar 启动线程池
	 */
    private static ExecutorService JAR_EXECUTOR_SERVICE;
    
    /**
     * log 线程池
     */
    private static ExecutorService LOG_EXECUTOR_SERVICE;
    
    static{

    	JAR_EXECUTOR_SERVICE = Executors.newCachedThreadPool(JarInstanceNameThreadFactory.newJarNameThreadFactoryDefault());

    	LOG_EXECUTOR_SERVICE = Executors.newCachedThreadPool(JarLogNameThreadFactory.newLogNameThreadFactoryDefault());
    	
    	String mainThreadSleepTime = Properties.get("jar.start.main.thread.sleep.time", "2000");
    	AFTER_START_SLEEP_TIME = Long.valueOf(mainThreadSleepTime);
    	String mainThreadInteralTimes = Properties.get("jar.start.main.thread.interval.times", "5");
    	INTERVAL_TIMES = Integer.valueOf(mainThreadInteralTimes);
    }
    
    /**
     * 描述：获取jar ExecutorService实例
     * @author csy 
     * @date 2022年1月13日 下午12:40:54
     * @return ExecutorService
     */
    public static ExecutorService getJarExecutorService(){
    	return JAR_EXECUTOR_SERVICE;
    }
    
    /**
     * 描述：获取log ExecutorService实例
     * @author csy 
     * @date 2022年1月13日 下午5:22:49
     * @return ExecutorService
     */
    public static ExecutorService getLogExecutorService(){
    	return LOG_EXECUTOR_SERVICE;
    }
    
    private static final Map<String, Boolean> JAR_STOP_MAP = new ConcurrentHashMap<String, Boolean>();
    
    /**
     * 描述：启动线程执行jar
     * @author csy 
     * @date 2022年1月12日 下午2:51:04
     * @param jarId
     */
    public synchronized static void start(String jarId){
    	try {
    		privateStart(jarId);
		} finally {
    		tryToStopExceptionApplications(jarId);
		}
    }
    
    /**
     * 描述：启动线程执行jar
     * @author csy 
     * @date 2022年1月12日 下午2:51:04
     * @param jarId
     */
    private static void privateStart(String jarId){
    	//获取jar配置
		JarManageBean jarManageBean = JarEntityUtils.get(jarId);
		Objects.notNullAssert(jarManageBean, PrintUtils.getFormatString("启动jarId失败，jarId:%s, 获取对象为空", jarId));

		Objects.isConditionAssert(JarNumberEnum.UN_RUNING.equalValue(jarManageBean.getIsRuning()), 
				RuntimeException.class, "应用已经启动，无需启动。");
		
		JAR_STOP_MAP.remove(jarId);
		
		log.debug(PrintUtils.getFormatString("jarStart jarId: %s", jarId));
		
		//获取启动命令 JmxRmiInfoBean端口一对一，需要待处理
		String startCmd = jarManageBean.startExecuteCmdStr();
		//构建执行对象
		Command command = Command.getBuilder().commandStr(startCmd).isPrint(false);
		//如果不是window系统，用数组命令
		if (!OsUtils.isWindow()){
			command.commandArr("/bin/sh", "-c", startCmd);
		}
		log.debug(PrintUtils.getFormatString("jarStart  cmd : %s", (command.isUseStrCommand() ? 
				command.getCommandStr() : Arrays.toString(command.getCommandArr()))));
		
		//启动新线程
		getJarExecutorService().execute(() -> {
    		
			//执行启动命令
    		String exemsg = command.toStringContents();
    		
    		log.debug(PrintUtils.getFormatString("应用已停止，jarId:%s，应用名：%s(%s)", 
    				jarManageBean.getJarId(), jarManageBean.getJarEnName(), jarManageBean.getJarChName()));
    		//这里热加载、热部署时候，执行kill之后，会有异常，但是可以忽略异常
    		afterStopRefreshManageBean(jarManageBean);
    		JAR_STOP_MAP.put(jarId, true);
    		log.debug(PrintUtils.getFormatString("停止信息：%s" , exemsg));
    	});

    	log.debug(PrintUtils.getFormatString("已执行jarId:%s 启动，等待应用启动完毕。", jarId));
    	
    	disposePidOfJmx(jarManageBean, jarId, command);
    	
    	String pid = jarManageBean.getPId();
    	afterSuccessRefreshJarManageBean(jarManageBean, pid);
    	
    	log.debug(PrintUtils.getFormatString("应用信息更新完毕，jarId:%s", jarId));
    }
    
    /**
     * 
     * 描述：尝试关闭异常应用，仅当匹配上时候才执行kill
     * @author csy
     * @date 2023年3月3日 上午10:20:04
     * @param jarId
     * @param runtimeStatus 
     */
    private static void tryToStopExceptionApplications(String jarId) {
		JarManageBean jarManageBean = JarEntityUtils.get(jarId);
		//如果是运行着状态的，忽略
		if (JarNumberEnum.RUNING
				.equalValue(jarManageBean.getIsRuning())){
			return ;
		} 
		Process process = null;
		String pid = null;
		try {
			//执行java命令
			Command command = Command.getBuilder();
			if (OsUtils.isWindow()){
				command.commandStr("jps -l");
			}else{
				//或者直接指定jdk的jps , xxs/java/bin/jps -l
				command.commandArr("/bin/sh", "-c", "cd / && source /etc/profile && jps -l");
			}
			List<String> printResults = command.toListContets();
			if (CollectionUtils.isEmpty(printResults)){
				return ;
			}
			for (String applicationMessage:printResults){
				String[] arr = applicationMessage.split(" ");
				if (Objects.notNull(arr) && arr.length > 1){
					pid = arr[0];
					//匹配启动路径
					if (StringUtils.isNotBlank(arr[1]) &&
							arr[1].equals(jarManageBean.getJarPath())){
						//关闭进程
						kill(jarId, pid);
					}
				}
			}
			process = command.getProcess();
		}catch(Exception e){
			log.error(PrintUtils.getFormatString("尝试关闭jar应用失败, jarId:%s, pid:%s", jarId, pid), e);
		} finally {
			if (process != null){
				process.destroy();
			}
		}	
	}

	/**
     * 描述：处理pid
     * @author csy 
     * @date 2022年6月7日 下午5:55:56
     * @param jarManageBean
     * @param jarId
     * @param command
     */
    private static void disposePidOfJmx(JarManageBean jarManageBean, String jarId, Command command){
    	JarRefJmxRmiIpPortBean jarRefJmxRmiIpPortBean = jarManageBean.getJarRefJmxRmiIpPortBean();
    	String pid = null;
    	JmxRmiInfoBean jmxRmiInfoBean = null;
    	for (int i = 0; i < INTERVAL_TIMES; i++){
        	try {
        		Thread.sleep(AFTER_START_SLEEP_TIME);
            	jmxRmiInfoBean = JmxRmiInfoBean.newIntance(jarRefJmxRmiIpPortBean.getIp(), jarRefJmxRmiIpPortBean.getPort());
            	pid = jmxRmiInfoBean.getPid();
            	if (pid != null){
            		jarManageBean.setPId(pid);
            		log.debug(PrintUtils.getFormatString("应用启动完毕, jarId:%s, pid:%s", jarId, pid));
            		return ;
            	}	
        	} catch (Exception e) {/*do not thing*/}finally{
        		if (Objects.notNull(jmxRmiInfoBean)){
        			jmxRmiInfoBean.closeConnetor();
        		}
        	}
        	
        	Objects.isConditionAssert(!JAR_STOP_MAP.containsKey(jarId), JarManageException.class, command.getContents());
    	} 
    	
    	Objects.notNullAssert(pid, JarManageException.class, "获取pid失败，获取启动信息失败.");
    }
    	        
    /**
     * 描述：关闭进程
     * @author csy 
     * @date 2022年1月12日 下午2:47:33
     * @param jarId 
     */
    public static void kill(String jarId){
    	String pid = JarProcessUtils.get(jarId);
    	kill(jarId, pid);
    }
    
    /**
     * 
     * 描述：关闭进程
     * @author csy
     * @date 2023年3月3日 上午10:13:50
     * @param jarId
     * @param pid
     */
    public static void kill(String jarId, String pid){
		String killCmd = PrintUtils.getFormatString((OsUtils.isWindow() ? "taskkill /pid %s /f" : "kill -9 %s"), pid);
		log.debug(PrintUtils.getFormatString("kill cmd >> %s , jarId: %s", killCmd, jarId));
		Command command = Command.getBuilder().commandStr(killCmd).isPrint(true);
		try {
			//执行
			command.exec();
			//等待执行完毕
			command.getProcess().waitFor();
			//移除pid关联
			afterStopRefreshManageBean(JarEntityUtils.get(jarId));
			//打印结束进程
			log.debug(PrintUtils.getFormatString("已关闭进程: %s , jarId: %s", pid, jarId));
		} catch (InterruptedException e) {
			throw new RuntimeException(PrintUtils.getFormatString("关掉端口：%s失败, jarId:%s", pid, jarId));
		}finally {
			try {command.getProcess().destroy();} catch(Exception e){}
		}      	
    }
    
    /**
     * 描述：重启 
     * @author csy 
     * @date 2022年1月26日 下午4:59:18
     * @param jarId
     */
	public static void restart(String jarId) {
		kill(jarId);
		
		try {
			//在kill之后，之前的start还占用着资源，此时立刻启动会有异常
			Thread.sleep(AFTER_START_SLEEP_TIME);
		} catch (InterruptedException e1) {}
		
		try {
			start(jarId);
		} catch (Exception e) {
			try {
				Thread.sleep(AFTER_START_SLEEP_TIME);
			} catch (InterruptedException e1) {}
			start(jarId);
		}
	}
    
    /**
     * 描述：应用停止之后 ，更新pid、jar配置对象
     * @author csy 
     * @date 2022年1月13日 下午3:46:43
     * @param command  命令对象
     * @param jarManageBean jar配置对象
     */
    private static void afterStopRefreshManageBean(JarManageBean jarManageBean){
    	//更新状态
		jarManageBean.setIsAlive(JarNumberEnum.UN_ALIVE.getValue());
    	jarManageBean.setIsRuning(JarNumberEnum.UN_RUNING.getValue());
    	jarManageBean.setPId("");
    	jarManageBean.setJarServerInfoBean(null);
    	JarEntityUtils.storage(jarManageBean);
    	
    	//移除pid
    	JarProcessUtils.remove(jarManageBean.getJarId());
    }
    
    /**
     * 描述：jar启动成功后，更新pid、jar配置对象
     * @author csy 
     * @date 2022年1月13日 下午3:47:36
     * @param jarManageBean jar配置对象
     * @param pid jar进程id
     */
    private static void afterSuccessRefreshJarManageBean(JarManageBean jarManageBean, String pid){
    	//存储pid
    	JarProcessUtils.storage(jarManageBean.getJarId(), pid);
    	//更新状态、设置pid
    	jarManageBean.setPId(pid);
    	jarManageBean.setIsAlive(JarNumberEnum.ALIVE.getValue());
    	jarManageBean.setIsRuning(JarNumberEnum.RUNING.getValue());
    	JarEntityUtils.storage(jarManageBean);   	
    }
}
