package com.csy.test.commons.patch.base.defaults;

import java.io.File;

import com.csy.test.commons.patch.base.AbstractWriteRecordFile;
import com.csy.test.commons.utils.file.FileUtils;

public class TomcatWriteFileRecordFile extends AbstractWriteRecordFile{

	@Override
	public void autoWriteRecordFile() {

		String workPathPrefix = pachInitParams.getCompilePathPrefix();
		File tomcatFile = new File(workPathPrefix);
		String tomcatPath = tomcatFile.getAbsolutePath().replace('\\', '/');
		StringBuilder stringBuilder = new StringBuilder();
		for (String path:this.compileFilePaths){
			stringBuilder.append(path.replace('\\', '/').replace(tomcatPath, "")).append("\n");
		}
		
		File outFile = this.createDefaultRecodFile();
		
		FileUtils.writeFile(outFile, stringBuilder.toString() , pachInitParams.getUseSamePatchRecordFile());
		
		System.out.println("pach record file path:" + outFile.getAbsolutePath());
	}

}
