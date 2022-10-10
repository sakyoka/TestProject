package com.csy.test.commons.patch.base.defaults;

import java.io.File;
import java.io.IOException;

import com.csy.test.commons.patch.base.AbstractPatchGenerate;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.patch.exception.NotFoundCompileClassException;
import com.csy.test.commons.patch.exception.PatchGenerateFailException;
import com.csy.test.commons.utils.file.FileUtils;

public class SpringbootPatchOriginalGenerate extends AbstractPatchGenerate{

	@Override
	protected void copyFile(PatchInitParams pachInitParams, String compilePath) {
		File file = new File(compilePath);
		String cachePathPrefix = pachInitParams.getCachePathPrefix();
    	String uuid = pachInitParams.getCachePathUuid();
		String cachePath = cachePathPrefix + File.separator + uuid;
		
		if (!file.exists()){
			throw new NotFoundCompileClassException("文件找不到:" + compilePath);
		}
		
		String sourcePath = CompileRefSourcePathCache.get(compilePath);
		System.out.println(sourcePath);
		if (sourcePath.endsWith(PatchDefinedConstants.KEY_JAVA)) {
			sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(".") + 1) + PatchDefinedConstants.KEY_CLASS;
		}
		
		String outPath = new StringBuilder()
                            .append(cachePath)
                            .append(File.separator)
                            .append(pachInitParams.getProjectEnName())
                            .append(File.separator)
                            .append(sourcePath)
                            .toString();
		
		try {
			FileUtils.coppyTo(file, outPath);
		} catch (IOException e) {
			throw new PatchGenerateFailException("复制文件失败，路径：" + outPath, e);
		}		
	}

}
