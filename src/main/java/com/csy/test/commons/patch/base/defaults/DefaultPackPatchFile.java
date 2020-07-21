package com.csy.test.commons.patch.base.defaults;

import java.io.File;

import com.csy.test.commons.patch.base.AbstractPackPatchFile;
import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.patch.constatns.PatchInitConstants;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：默认打包方法
 * @author csy
 * @date 2020年7月3日 下午3:47:08
 */
public class DefaultPackPatchFile extends AbstractPackPatchFile{

	@Override
	public void packFile(PatchInitParams patchInitParams) {
		
		//执行压缩打包
		String sourceFilePath = patchInitParams.getCachePathPrefix() + File.separator + patchInitParams.getCachePathUuid();
		File dir = new File(sourceFilePath);
		if (!dir.exists()){
			System.err.println(String.format("compress file fail due to source dir(%s) not exists" , sourceFilePath));
			return ;
		}
		
		String packFilePath = patchInitParams.getPackFilePath() != null ? 
				                                    patchInitParams.getPackFilePath() : 
				                                    PatchInitConstants.DEFAULT_CACHE_PATH_PREFIX + File.separator + "补丁.rar";
		FileUtils.compressZip(packFilePath , sourceFilePath);		
	}

}
