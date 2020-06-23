package com.csy.test.commons.patch.base.defaults;

import java.io.File;

import com.csy.test.commons.patch.base.AbstractWriteRecordFile;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.utils.UUID;
import com.csy.test.commons.utils.file.FileUtils;

public class SpringbootWirteFileRecordFile extends AbstractWriteRecordFile{

	@Override
	public void autoWriteRecordFile() {
		
		String cachePathPrefix = pachInitParams.getCachePathPrefix();
		String outFilePath = new StringBuilder()
				.append(cachePathPrefix)
				.append(File.separator)
				.append(pachInitParams.getCachePathUuid())
				.append(File.separator)
				.append(pachInitParams.getPatchFileName())
				.toString();
		
		File outFile = new File(outFilePath);
		if (outFile.exists()){
			outFilePath = new StringBuilder().append(cachePathPrefix)
					.append(File.separator)
					.append(pachInitParams.getCachePathUuid())
					.append(UUID.getString())
					.append(this.pachInitParams.getPatchFileName().substring(pachInitParams.getPatchFileName().lastIndexOf(".") +1))
					.toString();
			outFile = new File(outFilePath);
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		String sourcePath = null;
		for (String path:this.compileFilePaths){
			sourcePath = CompileRefSourcePathCache.get(path);
			if (sourcePath.endsWith(PatchDefinedConstants.KEY_JAVA)) {
				sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(".") + 1) + PatchDefinedConstants.KEY_CLASS;
			}
			stringBuilder.append(this.pachInitParams.getProjectEnName())
			             .append(File.separator)
			             .append(sourcePath).append("\n");
		}
		FileUtils.writeFile(outFile, stringBuilder.toString());
		
		System.out.println("pach record file path:" + outFilePath);
	}

}
