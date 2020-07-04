package com.csy.test.commons.patch.base;

import java.io.File;
import java.util.List;

import com.csy.test.commons.patch.bean.PatchInitParams;
import com.csy.test.commons.utils.UUID;

public abstract class AbstractWriteRecordFile {
	
	protected PatchInitParams pachInitParams;
	
	protected List<String> compileFilePaths;
	
	public AbstractWriteRecordFile() {}
	
	public abstract void autoWriteRecordFile();
	

	public AbstractWriteRecordFile pachInitParams(PatchInitParams pachInitParams) {
		this.pachInitParams = pachInitParams;
		return this;
	}

	public AbstractWriteRecordFile compileFilePaths(List<String> compileFilePaths) {
		this.compileFilePaths = compileFilePaths;
		return this;
	}
	
	/**
	 * 
	 * 描述：创建默认记录文件
	 * @author csy
	 * @date 2020年6月24日 下午10:03:02
	 * @return 空白记录文件
	 */
	protected File createDefaultRecodFile() {
		String cachePathPrefix = pachInitParams.getCachePathPrefix();
		String outFilePath = new StringBuilder()
				.append(cachePathPrefix)
				.append(File.separator)
				.append(pachInitParams.getCachePathUuid())
				.append(File.separator)
				.append(pachInitParams.getPatchFileName())
				.toString();
		
		File outFile = new File(outFilePath);
		if (!pachInitParams.getUseSamePatchRecordFile() && outFile.exists()){
			outFilePath = new StringBuilder().append(cachePathPrefix)
					.append(File.separator)
					.append(pachInitParams.getCachePathUuid())
					.append(File.separator)
					.append(UUID.getString())
					.append(this.pachInitParams.getPatchFileName().substring(pachInitParams.getPatchFileName().lastIndexOf(".")))
					.toString();
			outFile = new File(outFilePath);
		}
		return outFile;
	}
}
