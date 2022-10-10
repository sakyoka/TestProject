package com.csy.test.commons.jarmanage.utils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.csy.test.commons.jarmanage.base.OperateFile;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.file.FileUtils;

public class BakFileUtils {
	
	private BakFileUtils(){}
	
	/**
	 * 描述：判断目录下的备份文件时候大于max次数，如果是，其余删除
	 * @author csy 
	 * @date 2022年3月2日 下午2:00:50
	 * @param dirPath   目录
	 * @param fileNameKeyWord 文件名关键词
	 * @param max  最大次数
	 */
	public static void juageFileNumberThenDeletes(String dirPath, String fileNameKeyWord, int max){
		juageFileNumberThenDeletes(new File(dirPath), fileNameKeyWord, max, null);
	}
	
	/**
	 * 描述：判断目录下的备份文件时候大于max次数，如果是，其余删除
	 * @author csy 
	 * @date 2022年3月2日 下午2:00:50
	 * @param dir   目录
	 * @param fileNameKeyWord 文件名关键词
	 * @param max  最大次数
	 */
	public static void juageFileNumberThenDeletes(File dir, String fileNameKeyWord, int max){
		juageFileNumberThenDeletes(dir, fileNameKeyWord, max, null);
	}
	
	/**
	 * 描述：判断目录下的备份文件时候大于max次数，如果是，其余删除
	 * @author csy 
	 * @date 2022年3月2日 下午2:00:50
	 * @param dirPath   目录
	 * @param fileNameKeyWord 文件名关键词
	 * @param max  最大次数
	 * @param operateFile 额外操作
	 */
	public static void juageFileNumberThenDeletes(String dirPath, String fileNameKeyWord, int max, OperateFile operateFile){
		juageFileNumberThenDeletes(new File(dirPath), fileNameKeyWord, max, operateFile);
	}
	
	/**
	 * 描述：判断目录下的备份文件时候大于max次数，如果是，其余删除
	 * @author csy 
	 * @date 2022年3月2日 下午2:00:50
	 * @param dir   目录
	 * @param fileNameKeyWord 文件名关键词
	 * @param max  最大次数
	 * @param operateFile 额外操作
	 */
	public static void juageFileNumberThenDeletes(File dir, String fileNameKeyWord, int max, OperateFile operateFile){
		operateFile(dir, operateFile, (path, paths) -> {
			if (path.contains(fileNameKeyWord)){
				if (paths.size() > max){
					FileUtils.deletes(path);
					return ;
				}
				paths.add(path);
			}			
		});
	}
	
	/**
	 * 描述：目录下的文件操作前后
	 * @author csy 
	 * @date 2022年3月2日 下午2:19:04
	 * @param dir  目录
	 * @param perOperateFile 操作前
	 * @param afterOperateFile 操作后
	 */
	public static void operateFile(File dir, OperateFile perOperateFile, OperateFile afterOperateFile){
		if (dir.exists() && dir.isDirectory()){
			File[] files = dir.listFiles();
			if (Objects.notNull(files) && files.length > 0){
				List<String> perPathList = new ArrayList<String>();
				List<String> afterPathList = new ArrayList<String>();
				List<File> newFiles = sortFiles(files);
				for (File file:newFiles){
					if (file.isDirectory()){
						continue;
					}
					String path = file.getAbsolutePath();
					if (Objects.notNull(perOperateFile)){
						perOperateFile.operate(path, perPathList);
					}
					
					if (Objects.notNull(afterOperateFile)){
						afterOperateFile.operate(path, afterPathList);
					}					
				}
			}	
		}
	}
	
	/**
	 * 描述：备份allJarEntity.json
	 * @author csy 
	 * @date 2022年3月4日 下午2:54:12
	 * @param suffixName 文件后缀包含名字
	 * @param max 存在最大次数
	 */
	public static void bakAllEntityFile(String suffixName, int max){
		String jarEntityFilePath = JarInitBaseConstants.getJarEntityFilePath();
		String bakDirPath = JarInitBaseConstants.getJarRootPath() + File.separator + JarInitBaseConstants.DEFAULT_BAK_PATH_NAME;
		juageFileNumberThenDeletes(bakDirPath, suffixName, max);
		//备份文件
		String bakFilePath = jarEntityFilePath.replace(JarInitBaseConstants.getJarRootPath(), 
				JarInitBaseConstants.getJarRootPath() + File.separator + JarInitBaseConstants.DEFAULT_BAK_PATH_NAME)
				+ suffixName + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
		FileUtils.ifNotExistsCreate(bakFilePath);
		FileUtils.copyTo(Paths.get(jarEntityFilePath), Paths.get(bakFilePath), false);		
	}
	
	/**
	 * 
	 * 描述：备份gobalConfig.json
	 * @author csy
	 * @date 2022年3月6日 下午1:57:38
	 * @param suffixName 文件后缀包含名字
	 * @param max 存在最大次数
	 */
	public static void bakGobalConfigFile(String suffixName, int max){
		String gobalConfigFilePath = JarInitBaseConstants.getJarGobalConfigFilePath();
		String bakDirPath = JarInitBaseConstants.getJarRootPath() + File.separator + JarInitBaseConstants.DEFAULT_BAK_PATH_NAME;
		juageFileNumberThenDeletes(bakDirPath, suffixName, max);
		//备份文件
		String bakFilePath = gobalConfigFilePath.replace(JarInitBaseConstants.getJarRootPath(), 
				JarInitBaseConstants.getJarRootPath() + File.separator + JarInitBaseConstants.DEFAULT_BAK_PATH_NAME)
				+ suffixName + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
		FileUtils.ifNotExistsCreate(bakFilePath);
		FileUtils.copyTo(Paths.get(gobalConfigFilePath), Paths.get(bakFilePath), false);		
	}
	
	private static List<File> sortFiles(File[] files){
		List<File> newFiles = Arrays.asList(files);
		Collections.sort(newFiles, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return (int) (o2.lastModified() - o1.lastModified());
			}
		});
		return newFiles;
	}
}
