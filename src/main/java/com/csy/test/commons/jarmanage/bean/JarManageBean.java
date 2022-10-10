package com.csy.test.commons.jarmanage.bean;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.utils.JarGobalConfigUtils;
import com.csy.test.commons.jarmanage.utils.JarJMXPortUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.OsUtils;
import com.csy.test.commons.utils.UUID;
import com.csy.test.commons.utils.file.FileUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 
 * 描述：jar管理记录实体类
 * @author csy
 * @date 2022年1月7日 上午11:49:01
 */
@Data
@Builder
@AllArgsConstructor
public class JarManageBean implements Serializable{
	
	private static final long serialVersionUID = 6915226072463100361L;

	/** 给jar一个uuid*/
	private String jarId;
	
	/** 进程ID*/
	private String pId;
	
	/** jar中文名字*/
	private String jarChName;
	
	/** jar英文名字*/
	private String jarEnName;
	
	/** jar描述*/
	private String jarDesc;
	
	/** 日志路径*/
	private String logPath;
	
	/** jar包保存相对路径*/
	private String jarPath;
	
	/** 是否运行正常 0否 1是*/
	private Integer isAlive;
	
	/** 是否启动0否 1是*/
	private Integer isRuning;
		
	/** 文件uuid目录*/
	private String dirUuid;
	
	/** 列表展示排序号,越小越靠前*/
	private Integer orderNumber;
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
	
	/** jar对应的jdk配置，可选*/
	private JarJdkManageBean jarJdkManageBean;
	
	/**配置开关对象*/
	private JarOnOffManageBean jarOnOffManageBean;
	
	/** jmx连接参数*/
	private JarRefJmxRmiIpPortBean jarRefJmxRmiIpPortBean;
	
	/** jar启动配置参数对象*/
	private JarPropertiesBean jarPropertiesBean;
	
	/** 删除标识：0否，1是*/
	private Integer deleteFlag;
	
	/** 目标服务信息*/
	private JarServerInfoBean jarServerInfoBean;
	
	/**包后缀*/
	private String sufixName;
	
	public JarManageBean(){}
	
	/**
	 * 描述：初始化存储前参数，仅第一次保存时候
	 * @author csy 
	 * @date 2022年1月12日 上午9:58:01
	 */
	public void preStroage(){
		
		Objects.notNullAssert(this.jarEnName, "jarEnName is not allow null");
		
		if (StringUtils.isBlank(this.jarId)){
			this.jarId = UUID.getString();
			this.createTime = new Date();
		}
		
		this.jarRefJmxRmiIpPortBean = JarRefJmxRmiIpPortBean.builder()
				.ip(JarJMXPortUtils.JMX_IP)
				.port(JarJMXPortUtils.getAvaliablePort())
				.build();
		
		this.updateTime = new Date();
		this.isAlive = JarNumberEnum.UN_ALIVE.getValue();
		this.isRuning = JarNumberEnum.UN_RUNING.getValue();
		this.orderNumber = Objects.ifNullDefault(this.orderNumber, 9999);
		this.jarPath = this.getJarDir() + this.jarEnName + "." + this.sufixName;
		this.logPath = this.getLogDir() + this.jarEnName + ".log";
		//初始化日志文件
		String fullLogPath = JarInitBaseConstants.getJarRootPath() + logPath;
		FileUtils.ifNotExistsCreate(fullLogPath);
		
		this.jarOnOffManageBean = Objects.ifNullDefault(this.jarOnOffManageBean, JarOnOffManageBean.DEFAULT_UN_AUTO);
		
		this.jarPropertiesBean = Objects.ifNullDefault(this.jarPropertiesBean, JarPropertiesBean.DETAULT_NONE);
	}
	
	public String getLogDir(){
		return File.separator + JarInitBaseConstants.DEFAULT_LOG_PATH_NAME + File.separator + this.dirUuid + File.separator 
				+ this.jarEnName + File.separator ;
	}
	
	public String getJarDir(){
		return File.separator + JarInitBaseConstants.DEFAULT_JAR_PATH_NAME + File.separator + this.dirUuid + File.separator 
				+ this.jarEnName + File.separator;
	}
	
	/**
	 * 描述：获取执行启动命令
	 * @author csy 
	 * @param restart 
	 * @date 2022年1月12日 上午9:50:25
	 * @return 执行jar启动命令
	 */
	public String startExecuteCmdStr(){

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(OsUtils.isWindow() ? "cmd /c " : "")
		             //进入该目录下再执行命令
		             .append("cd ").append(JarInitBaseConstants.getJarRootPath()).append(this.getJarDir()).append(" && ");
		
		if (Objects.notNull(jarJdkManageBean) 
				&& StringUtils.isNotBlank(jarJdkManageBean.getJdkPath())){
			//配置了jdk路径的
			stringBuilder.append(jarJdkManageBean.getJdkPath())
			             .append(File.separator)
			             .append("bin")
			             .append(File.separator)
			             .append("java");
			if (OsUtils.isWindow()){
				stringBuilder.append(".exe");
			}
		}else{
			JarGobalConfigBean jarGobalConfigBean = JarGobalConfigUtils.readConfig();
			if (Objects.notNull(jarGobalConfigBean) && StringUtils.isNotBlank(jarGobalConfigBean.getJdkPath())){
				//全局的JDK
				stringBuilder.append(jarGobalConfigBean.getJdkPath())
				             .append(File.separator)
				             .append("bin")
				             .append(File.separator)
				             .append("java");
				if (OsUtils.isWindow()){
					stringBuilder.append(".exe");
				}				
			}else{
				//默认使用jdk
				stringBuilder.append("java -version && java");	
			}
		}
		
		if (Objects.notNull(jarJdkManageBean) 
				&& StringUtils.isNotBlank(jarJdkManageBean.getJvm())){
			stringBuilder.append(" ").append(jarJdkManageBean.getJvm());
		}

		stringBuilder.append(" -Dfile.encoding=utf-8")
		             //配置rmi
		             .append(" -Djava.rmi.server.hostname=").append(this.getJarRefJmxRmiIpPortBean().getIp())
		             .append(" -Dcom.sun.management.jmxremote=")
		             .append(" -Dcom.sun.management.jmxremote.port=").append(this.getJarRefJmxRmiIpPortBean().getPort())
		             .append(" -Dcom.sun.management.jmxremote.authenticate=false")
			         .append(" -Dcom.sun.management.jmxremote.ssl=false")
			         .append(" -jar ").append(JarInitBaseConstants.getJarRootPath()).append(this.getJarPath())
			         //这种写pid需要springboot 启动时候添加ApplicationPidFileWriter监听器
//			         .append(" --spring.pid.file=").append(JarInitBaseConstants.DEFAULT_STORE_PATH_PREFIX)
//											       .append(File.separator)
//											       .append(JarInitBaseConstants.DEFAULT_PID_PATH_NAME)
//											       .append(File.separator)
//											       .append(this.jarEnName)
//											       .append(File.separator)
//											 	   .append(this.jarEnName + ".pid")
			         //需要追加模式
			         .append(" >> ").append(JarInitBaseConstants.getJarRootPath()).append(this.getLogPath());
		return stringBuilder.toString();		
	}
	
	/**
	 * 描述：直接删除jarId相关的文件夹及文件
	 * @author csy 
	 * @date 2022年2月10日 上午10:00:34
	 * @param jarId
	 */
	@Deprecated
	public void deleteAboutDir(){
		//缓存jar包.这个缓存目录记住的只是最新的一个，上传多个时，清除不了，需要定时器清除或者不管了，手动清除
		//按照顺序先删除 缓存文件、运行的jar文件、日志文件（日志文件需要排最后，因为可能失败，页面WebScoket占用文件）
		String cacheJarDir = JarInitBaseConstants.getJarRootPath() + File.separator 
				+ JarInitBaseConstants.DEFAULT_CACHE_FILE_NAME + File.separator + this.dirUuid;	
		FileUtils.deletes(cacheJarDir);
		
		//运行的jar及目录
		String jarDir = JarInitBaseConstants.getJarRootPath() + File.separator 
				+ JarInitBaseConstants.DEFAULT_JAR_PATH_NAME + File.separator + this.dirUuid;	
		FileUtils.deletes(jarDir); 			
		
		//日志文件及目录
		String logJarDir = JarInitBaseConstants.getJarRootPath() + File.separator 
				+ JarInitBaseConstants.DEFAULT_LOG_PATH_NAME + File.separator + this.dirUuid;
		FileUtils.deletes(logJarDir); 			

	}
}
