package com.csy.test.ui.patch.cache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.csy.test.commons.patch.constatns.PatchInitConstants;
import com.csy.test.commons.utils.file.FileUtils;
import com.csy.test.ui.patch.constants.ConfigConstant;
import com.csy.test.ui.patch.exception.DeloyParamCacheException;

/**
 * 
 * 描述：配置参数缓存
 * @author csy
 * @date 2020年9月28日 下午3:48:49
 */
public class DeloyParamCache {
	
	private static final String FILE_PATH = PatchInitConstants.DEFAULT_CACHE_PATH_PREFIX + ConfigConstant.CONFIG_DIR_NAME + File.separator + "deploy.text";
	
	/**
	 * 描述：写入配置文件
	 * @author csy 
	 * @date 2020年9月28日 下午4:44:29
	 * @param text
	 * @param dirName
	 */
	public static void storage(String text){

		synchronized (DeloyParamCache.class) {
			File file = new File(FILE_PATH);
			try {
				Files.createDirectories(Paths.get(FILE_PATH));
			} catch (IOException e) {
				throw new DeloyParamCacheException("创建文件失败" , e);
			}
			
			FileUtils.writeFile(file, text);
		}
	}
	
	/**
	 * 描述：读取配置文件
	 * @author csy 
	 * @date 2020年9月28日 下午4:44:44
	 * @return
	 */
	public static String get(){
		List<String> vList = null;
		try {
			vList = Files.readAllLines(Paths.get(FILE_PATH));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if (vList != null){
			StringBuilder stringBuilder = new StringBuilder();
			vList.forEach((e) -> {
				stringBuilder.append(e);
			});
			return stringBuilder.toString();
		}
		return null;
	}
}
