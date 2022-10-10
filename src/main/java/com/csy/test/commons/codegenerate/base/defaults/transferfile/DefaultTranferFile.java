package com.csy.test.commons.codegenerate.base.defaults.transferfile;

import java.io.File;
import java.nio.file.Paths;

import com.csy.test.commons.codegenerate.base.TranferFileBase;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.file.FileUtils;

public class DefaultTranferFile implements TranferFileBase{

	private static final String PREFIXX_PATH = "src" + File.separator + "main" + File.separator + "java";
	
	@Override
	public void tranfer(CodeGenerateParams codeGenerateParams , String tableName , String sourceDir) {
		//targetDir
		String initPackageName = codeGenerateParams.getExtraInitPackageNameMap().get(tableName);
		String finallyTargetPath =  new StringBuilder()
				.append(codeGenerateParams.getBaseProjectPathMap().get(tableName))
				.append(File.separator)
				.append(PREFIXX_PATH)
				.append(File.separator)
				.append(codeGenerateParams.getBasePackageMap().get(tableName).replace(".", File.separator))
				.append(File.separator)
				.append(Objects.isNull(initPackageName) ? tableName.replace("_", "") : initPackageName)
				.toString();
		FileUtils.copyTo(Paths.get(sourceDir), Paths.get(finallyTargetPath));
	}
}
