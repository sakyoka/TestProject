package com.csy.test.commons.patch.valid;

import java.io.File;

import com.csy.test.commons.valid.base.AbstractValidBase;
import com.csy.test.commons.valid.exception.ValidException;

/**
 * 
 * 描述:校验路径是否是文件夹
 * @author csy
 * @date 2020 下午11:51:24
 */
public class FileDirValid extends AbstractValidBase {

	@Override
	public boolean valid(Object value) {
		String dirPath = value.toString();
		File file = new File(dirPath);
		try {
			
			if (!file.exists()) {
				file.mkdirs();
			}

			if (!file.isDirectory())
				throw new ValidException();
			
			return true;
		}catch (ValidException e) {
			throw new ValidException("当前字段值不是文件夹路径!");
		}catch (Exception e) {
			if (file != null){
				file.delete();
			}
			throw new RuntimeException("校验文件夹路径失败" ,e);
		} 
		
	}

}
