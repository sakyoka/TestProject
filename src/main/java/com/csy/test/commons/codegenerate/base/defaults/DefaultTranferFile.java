package com.csy.test.commons.codegenerate.base.defaults;

import java.io.File;
import java.nio.file.Paths;

import com.csy.test.commons.codegenerate.base.TranferFileBase;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.utils.file.FileUtils;

public class DefaultTranferFile implements TranferFileBase{

	private static final String PREFIXX_PATH = "src" + File.separator + "main" + File.separator + "java";
	
	@Override
	public void tranfer(CodeGenerateParams codeGenerateParams , String tableName , String sourceDir) {
		//targetDir
		String finallyTargetPath =  new StringBuilder()
				.append(codeGenerateParams.getBaseProjectPathMap().get(tableName))
				.append(File.separator)
				.append(PREFIXX_PATH)
				.append(File.separator)
				.append(codeGenerateParams.getBasePackageMap().get(tableName).replace(".", File.separator))
				.append(File.separator)
				.append(tableName.replace("_", ""))
				.toString();
		FileUtils.copyTo(Paths.get(sourceDir), Paths.get(finallyTargetPath));
	}
}
