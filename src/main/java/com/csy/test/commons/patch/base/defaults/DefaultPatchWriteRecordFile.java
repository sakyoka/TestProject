package com.csy.test.commons.patch.base.defaults;

import java.io.File;

import com.csy.test.commons.patch.base.AbstractWriteRecordFile;
import com.csy.test.commons.utils.file.FileUtils;

public class DefaultPatchWriteRecordFile extends AbstractWriteRecordFile{

	@Override
	public void autoWriteRecordFile() {

		StringBuilder stringBuilder = new StringBuilder();
		for (String path:this.compileFilePaths){
			stringBuilder.append(path).append("\n");
		}
		
		File outFile = this.createDefaultRecodFile();
		
		FileUtils.writeFile(outFile, stringBuilder.toString() , pachInitParams.getUseSamePatchRecordFile());
		
		System.out.println("pach record file path:" + outFile.getAbsolutePath());		
	}

}
