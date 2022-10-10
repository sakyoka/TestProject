package com.csy.test.commons.patch.base.defaults;


import java.io.File;
import java.io.IOException;

import com.csy.test.commons.patch.base.AbstractPatchGenerate;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.patch.exception.NotFoundCompileClassException;
import com.csy.test.commons.patch.exception.PatchGenerateFailException;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述:原目录生成
 * @author csy
 * @date 2020 下午2:18:50
 */
public class TomcatPatchOriginalGenerate extends AbstractPatchGenerate{

	@Override
	protected void copyFile(PatchInitParams pachInitParams, String compilePath) {
		File file = new File(compilePath);
		String cachePathPrefix = pachInitParams.getCachePathPrefix();
    	String uuid = pachInitParams.getCachePathUuid();
		String cachePath = cachePathPrefix + File.separator + uuid;
		
		if (!file.exists()){
			throw new NotFoundCompileClassException("文件找不到:" + compilePath);
		}
		
		String path = compilePath.replace(pachInitParams.getCompilePathPrefix(), PatchDefinedConstants.EMPTY_STRING)
		           .replace(PatchDefinedConstants.TARGET_CLASS, PatchDefinedConstants.EMPTY_STRING);
		
		String outPath = new StringBuilder()
                            .append(cachePath)
                            .append(File.separator)
                            .append(path)
                            .toString();
		try {
			FileUtils.coppyTo(file, outPath);
		} catch (IOException e) {
			throw new PatchGenerateFailException("复制文件失败，路径：" + outPath, e);
		}			
	}
}
