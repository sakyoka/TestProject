package com.csy.test.commons.patch.base.defaults;

import java.io.File;
import java.io.IOException;

import com.csy.test.commons.patch.base.AbstractPatchGenerate;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.exception.NotFoundCompileClassException;
import com.csy.test.commons.patch.exception.PatchGenerateFailException;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述:标准生成
 * @author csy
 * @date 2020 下午2:19:02
 */
public class DefaultPatchStandardGenerate extends AbstractPatchGenerate{

	@Override
	protected void copyFile(PatchInitParams pachInitParams , String compilePath) {
		File file = new File(compilePath);
		String cachePathPrefix = pachInitParams.getCachePathPrefix();
    	String uuid = pachInitParams.getCachePathUuid();
		String cachePath = cachePathPrefix + File.separator + uuid;
		
		if (!file.exists()){
			throw new NotFoundCompileClassException("文件找不到:" + compilePath);
		}
		
		try {
			FileUtils.coppyTo(file, cachePath);
		} catch (IOException e) {
			throw new PatchGenerateFailException(e);
		}		
	}
}
