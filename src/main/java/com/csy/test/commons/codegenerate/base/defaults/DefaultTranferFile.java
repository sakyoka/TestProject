package com.csy.test.commons.codegenerate.base.defaults;

import java.io.File;

import com.csy.test.commons.codegenerate.base.TranferFileBase;
import com.csy.test.commons.utils.file.FileUtils;

public class DefaultTranferFile implements TranferFileBase{

	@Override
	public void tranfer(String sourceDir, String targetDir) {
		
		FileUtils.coppyTo(new File(sourceDir), new File(targetDir));
	}
}
