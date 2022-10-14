package com.csy.test.webui.jarmanage.task.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.jarmanage.bean.JarGobalConfigBean;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.constants.JarNumberEnum;
import com.csy.test.commons.jarmanage.constants.JarTaskKeyEnum;
import com.csy.test.commons.jarmanage.utils.JarGobalConfigUtils;
import com.csy.test.commons.jarmanage.utils.JarScheduleTaskUtils;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.commons.utils.file.FileUtils;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

import lombok.extern.log4j.Log4j2;


/**
 * 
 * 描述：系统启动-自动清除垃圾
 * @author csy
 * @date 2022年9月10日 下午11:47:20
 */
@Log4j2
public class AutoClearTask implements InitTaskBase{

	@Override
	public int order() {
		return 1;
	}
	
	@Override
	public void execute() {
		JarGobalConfigBean jarGobalConfigBean = JarGobalConfigUtils.readConfig();
		if (JarNumberEnum.AUTO_CLEAR_CACHE.equalValue(jarGobalConfigBean.getAutoClearCache())){
			log.debug("系统配置自动清除缓存任务，正在执行...");
			addOrRemoveClearCacheTask(jarGobalConfigBean.getAutoClearCache());
		}else{
			log.debug("系统没有配置自动清除缓存任务.");
		}
		if (JarNumberEnum.AUTO_CLEAR_NOAVAIL_JAR.equalValue(jarGobalConfigBean.getAutoClearCache())){
			log.debug("系统配置自动清除无效jar任务，正在执行...");
			addOrRemoveClearNoAvailJarTask(jarGobalConfigBean.getAutoClearNoAvailJar());
		}else{
			log.debug("系统没有配置自动清除无效jar任务.");
		}
	}
	
    /**
     * 描述：添加或移除自动清除缓存文件
     * @author csy 
     * @date 2022年2月23日 下午3:23:21
     * @param autoClearCache
     */
	public static void addOrRemoveClearCacheTask(Integer autoClearCache) {
		if (JarNumberEnum.AUTO_CLEAR_CACHE.equalValue(autoClearCache)){
			//晚上12点执行
			JarScheduleTaskUtils.addTaskForHour(JarTaskKeyEnum.CLEAR_CACHE_TASK.getMark(), 24, () -> {
				clearCache();
			});
		}else{
			JarScheduleTaskUtils.removeTask(JarTaskKeyEnum.CLEAR_CACHE_TASK.getMark());
		}
	}
	
	/**
	 * 描述：清除缓存文件
	 * @author csy 
	 * @date 2022年3月10日 下午4:32:55
	 */
	public static void clearCache() {
		//上传的缓存文件文件
		FileUtils.deletesExceptRoot(JarInitBaseConstants.getJarRootPath() + File.separator 
				+ JarInitBaseConstants.DEFAULT_CACHE_FILE_NAME);
		//其它缓存
	}

	/**
	 * 描述：添加或移除自动清除无效的jar相关
	 * @author csy 
	 * @date 2022年2月23日 下午3:24:33
	 * @param autoClearNoAvailJar
	 */
	public static void addOrRemoveClearNoAvailJarTask(Integer autoClearNoAvailJar) {
		if (JarNumberEnum.AUTO_CLEAR_NOAVAIL_JAR.equalValue(autoClearNoAvailJar)){
			//晚上12点执行
			JarScheduleTaskUtils.addTaskForHour(JarTaskKeyEnum.CLEAR_NOAVAIL_JAR_TASK.getMark(), 24, () -> {
				clearNoAvailJar();
			});
		}else{
			JarScheduleTaskUtils.removeTask(JarTaskKeyEnum.CLEAR_NOAVAIL_JAR_TASK.getMark());
		}	
	}
	
	/**
	 * 描述：清除无效的jar相关
	 * @author csy 
	 * @date 2022年3月10日 下午4:33:06
	 */
	public static void clearNoAvailJar(){
		//不采取由总配置文件不存在实例删除方式
		//采取，对应单个实例有删除标识删除
		String jarDirPath = JarInitBaseConstants.getJarRootPath() + File.separator + JarInitBaseConstants.DEFAULT_JAR_PATH_NAME;
		log.debug(PrintUtils.getFormatString("开始扫描目录：%s下的无效jar", jarDirPath));
		List<String> dirUuids = new ArrayList<String>();
		try {
			Files.walkFileTree(Paths.get(jarDirPath), new SimpleFileVisitor<Path> (){	
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					String filePath = file.toFile().getAbsolutePath();
					//entity.json文件
					if (filePath.endsWith(JarInitBaseConstants.DETAULT_SINGLE_ENTITY_NAME)){
						String contents = FileUtils.read(filePath);
						JarManageBean jarManageBean = JSON.toJavaObject(JSON.parseObject(contents), JarManageBean.class);
						//收集有删除标识的jar配置
						if (JarNumberEnum.DELETED.equalValue(jarManageBean.getDeleteFlag())){
							dirUuids.add(jarManageBean.getDirUuid());
						}
					}
					return FileVisitResult.CONTINUE;
				}
				
			});
		} catch (Exception e) {
			log.error("处理无效jar过程存在异常", e);
		}
		
		log.debug(PrintUtils.getFormatString("收集到带有删除标识的diruuid：%s", dirUuids));
		//运行目录的jar
		deleteLogOrJarNoAvailDirAndFiles(JarInitBaseConstants.getJarRootPath() + File.separator 
				+ JarInitBaseConstants.DEFAULT_JAR_PATH_NAME, dirUuids);
		//日志
		deleteLogOrJarNoAvailDirAndFiles(JarInitBaseConstants.getJarRootPath() + File.separator 
				+ JarInitBaseConstants.DEFAULT_LOG_PATH_NAME, dirUuids);		
	}
	
	private static void deleteLogOrJarNoAvailDirAndFiles(String path, List<String> dirUuids){
		
		if (dirUuids.isEmpty()){
			return ;
		}
		
		File rootDir = new File(path);
		File[] uuidDirs= rootDir.listFiles();
		for (File dir:uuidDirs){
			//必须根目录对得上
			//而且是目录属性
			//目录名字对得上
			if (dir.getAbsolutePath().startsWith(JarInitBaseConstants.getJarRootPath()) 
					&& dir.isDirectory() 
					&& dirUuids.contains(dir.getName())){
				//删除
				log.debug(PrintUtils.getFormatString("删除目录：%s", dir.getAbsolutePath()));
				FileUtils.deletes(dir.getAbsolutePath());
			}
		}		
	}
}
