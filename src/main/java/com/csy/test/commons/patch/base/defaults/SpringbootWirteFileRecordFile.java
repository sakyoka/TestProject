package com.csy.test.commons.patch.base.defaults;

import java.io.File;

import com.csy.test.commons.patch.base.AbstractWriteRecordFile;
import com.csy.test.commons.patch.cache.CompileRefSourcePathCache;
import com.csy.test.commons.patch.constatns.PatchDefinedConstants;
import com.csy.test.commons.utils.file.FileUtils;

public class SpringbootWirteFileRecordFile extends AbstractWriteRecordFile{

	@Override
	public void autoWriteRecordFile() {
		
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
		
		File outFile = this.createDefaultRecodFile();
		
		FileUtils.writeFile(outFile, stringBuilder.toString() , pachInitParams.getUseSamePatchRecordFile());
		
		System.out.println("pach record file path:" + outFile.getAbsolutePath());
	}

}
