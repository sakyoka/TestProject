package com.csy.test.commons.jarmanage.constants;

import java.io.File;

import com.csy.test.commons.utils.OsUtils;
import com.csy.test.commons.utils.Properties;

/**
 * 
 * 描述：jar管理基础常量
 * @author csy
 * @date 2022年1月7日 下午3:12:27
 */
public class JarInitBaseConstants {
	
	/** jar存储目录前缀*/
	private static String DEFAULT_STORE_PATH_PREFIX;
	
	/** 日志存储根名字*/
	public static final String DEFAULT_LOG_PATH_NAME = "jarLog";
	
	/** jar包存储根名字*/
	public static final String DEFAULT_JAR_PATH_NAME = "jarStroage";
	
	/** pid存储根文件夹名字*/
	public static final String DEFAULT_PID_PATH_NAME = "jarPid";
	
	/** 上传缓存目录名字*/
	public static final String DEFAULT_CACHE_FILE_NAME = "cacheUpload";
	
	/** 系统目录名字*/
	public static final String DEFAULT_SYSTEM_PATH_NAME = "system";
	
	/** 备份文件夹名字*/
	public static final String DEFAULT_BAK_PATH_NAME = "bak";
	
	/** 导入文件夹名字*/
	public static final String DEFAULT_IMPORT_NAME = "import";
	
	/** 导出文件夹名字*/
	public static final String DEFATUL_EXPORT_NAME = "export";
	
	/** 单个实例json文件名*/
	public static final String DETAULT_SINGLE_ENTITY_NAME = "entityJson.json";
	
	/** 全局配置文件*/
	public static final String DEFAULT_GOBALCONFIG_FILE_NAME = "gobalConfig.json";
	
	/** 全部jar配置实例文件*/
	public static final String DEFAULT_ALLJARENTITY_FILE_NAME = "allJarEntity.json";
	
	/**所有的jar进程id文件*/
	public static final String DEFAULT_ALLJARPROCESS_FILE_NAME = "allJarProcess.json";
	
	/** 系统日志文件*/
	public static final String DEFAULT_SYSTEMLOG_FILE_NAME = "system.log";
	
	/** 系统日志文件路径（绝对路径）*/
	private static String DEFAULT_SYSTEM_LOG_PATH;
	
	/**
	 * 所有jar配置存储文件<br>
	 * PS:现在是所有对象存储在一个文件，还有另外一种方案是每个文件夹下对应存储一个文件
	 */
	private static String DEFAULT_JAR_ENTITY_STORAGE_PATH;
	
	/**
	 * 全局配置存储文件
	 * PS:现在是所有对象存储在一个文件，还有另外一种方案是每个文件夹下对应存储一个文件
	 */
	private static String DEFAULT_CONFIG_PATH;
	
	/**
	 * jarId ref pid存储文件
	 * PS:现在是所有对象存储在一个文件，还有另外一种方案是每个文件夹下对应存储一个文件
	 */
	private static String DEFAULT_JAR_PROCESS_STORAGE_PATH;
	
	private static final String DEFAULT_BASE_DIR = "jarManage";
	
	static{
		if (OsUtils.isWindow()){
			DEFAULT_STORE_PATH_PREFIX = Properties.get("jar.storage.path.prefix", "D:\\jarManage");
		}else{
			DEFAULT_STORE_PATH_PREFIX = Properties.get("jar.storage.path.prefix", "/mnt/jarManage");
		}
		
		if (!DEFAULT_STORE_PATH_PREFIX.contains(DEFAULT_BASE_DIR)) {
			DEFAULT_STORE_PATH_PREFIX += File.separator + DEFAULT_BASE_DIR;
		}
		
		DEFAULT_CONFIG_PATH = DEFAULT_STORE_PATH_PREFIX + File.separator + DEFAULT_GOBALCONFIG_FILE_NAME;
		DEFAULT_JAR_ENTITY_STORAGE_PATH = DEFAULT_STORE_PATH_PREFIX + File.separator + DEFAULT_ALLJARENTITY_FILE_NAME;
		DEFAULT_JAR_PROCESS_STORAGE_PATH = DEFAULT_STORE_PATH_PREFIX + File.separator + DEFAULT_ALLJARPROCESS_FILE_NAME;
		DEFAULT_SYSTEM_LOG_PATH = DEFAULT_STORE_PATH_PREFIX + File.separator + DEFAULT_SYSTEM_PATH_NAME + File.separator + DEFAULT_SYSTEMLOG_FILE_NAME;
	}
	
	/**
	 * 
	 * 描述：(目录)获取JAR根目录
	 * @author csy
	 * @date 2022年1月16日 下午3:07:45
	 * @return 根目录
	 */
	public static String getJarRootPath() {
		return DEFAULT_STORE_PATH_PREFIX;
	}
	
	/**
	 * 
	 * 描述：（文件）获取全局配置存储文件路径
	 * @author csy
	 * @date 2022年1月16日 下午3:08:17
	 * @return 全局配置文件路径
	 */
	public static String getJarGobalConfigFilePath() {
		return DEFAULT_CONFIG_PATH;
	}
	
	/**
	 * 
	 * 描述：（文件）获取Jar配置实例存储文件路径
	 * @author csy
	 * @date 2022年1月16日 下午3:08:38
	 * @return Jar配置实例存储文件路径
	 */
	public static String getJarEntityFilePath() {
		return DEFAULT_JAR_ENTITY_STORAGE_PATH;
	}
	
	/**
	 * 
	 * 描述：（文件）获取Jar进程存储文件路径
	 * @author csy
	 * @date 2022年1月16日 下午3:09:16
	 * @return Jar进程存储文件路径
	 */
	public static String getJarProcessFilePath() {
		return DEFAULT_JAR_PROCESS_STORAGE_PATH;
	}
	
	/**
	 * 描述：（文件）系统日志文件路径（绝对路径）
	 * @author csy 
	 * @date 2022年2月24日 下午3:55:33
	 * @return 系统日志文件路径（绝对路径）
	 */
	public static String getSystemLogFilePath(){
		return DEFAULT_SYSTEM_LOG_PATH;
	}
	
	/**
	 * 
	 * 描述：(目录)jaLog根目录
	 * @author csy
	 * @date 2022年3月6日 下午2:40:00
	 * @return jaLog根目录
	 */
	public static String getJarLogDir() {
		return getJarRootPath() + File.separator + DEFAULT_LOG_PATH_NAME;
	}
	
	/**
	 * 
	 * 描述：(目录)运行jar根目录
	 * @author csy
	 * @date 2022年3月6日 下午2:38:38
	 * @return 运行jar根目录
	 */
	public static String getJarStorageDir() {
		return getJarRootPath() + File.separator + DEFAULT_JAR_PATH_NAME;
	}
	
	/**
	 * 
	 * 描述：(目录)system根目录
	 * @author csy
	 * @date 2022年3月6日 下午2:39:05
	 * @return system根目录
	 */
	public static String getSystemDir() {
		return getJarRootPath() + File.separator + DEFAULT_SYSTEM_PATH_NAME;
	}
	
	/**
	 * 
	 * 描述：(目录)jar上传根目录
	 * @author csy
	 * @date 2022年3月6日 下午2:39:23
	 * @return jar上传根目录
	 */
	public static String getCacheUoloadDir() {
		return getJarRootPath() + File.separator + DEFAULT_CACHE_FILE_NAME;
	}
	
	/**
	 * 
	 * 描述：(目录)备份文件根目录
	 * @author csy
	 * @date 2022年3月6日 下午2:39:40
	 * @return 备份文件根目录
	 */
	public static String getBakDir() {
		return getJarRootPath() + File.separator + DEFAULT_BAK_PATH_NAME;
	}
}
