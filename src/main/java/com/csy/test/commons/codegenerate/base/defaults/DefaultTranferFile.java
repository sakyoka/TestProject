package com.csy.test.commons.codegenerate.base.defaults;

import java.nio.file.Paths;

import com.csy.test.commons.codegenerate.base.TranferFileBase;
import com.csy.test.commons.utils.file.FileUtils;

public class DefaultTranferFile implements TranferFileBase{

	@Override
	public void tranfer(String sourceDir, String targetDir) {
		
		FileUtils.copyTo(Paths.get(sourceDir), Paths.get(targetDir));
	}
}
