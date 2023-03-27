package com.csy.test.commons.codegenerate.base.defaults.writefile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.csy.test.commons.codegenerate.base.WriteFileBase;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述:文件输出,普通型-在同一个文件夹下罗列
 * @author csy
 * @date 2021年1月23日 下午5:15:54
 */
public class WriteFileGeneralDefault implements WriteFileBase{

	@Override
	public void write(CodeGenerateParams codeGenerateParams , String tableName, String contents , FileSuffixEnum fileSuffixEnum) {
		String path = codeGenerateParams.getBasePathMap().get(tableName);

		try {
			Files.createDirectories(Paths.get(path));
		} catch (IOException e) {
			throw new RuntimeException("创建路径失败:" + path, e);
		}
		
		String fileName = codeGenerateParams.getPreFixClassNameMap().get(tableName);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(path)
					 .append(File.separator)
					 .append(fileName)
					 .append(codeGenerateParams.getFileSuffixNameMap().get(fileSuffixEnum.getFileType()))
					 .append(".")
					 .append(fileSuffixEnum.getSuffixName());

		path = stringBuilder.toString();
		FileUtils.writeFile(path , contents);
		
		codeGenerateParams.getCodeFilePaths().add(path);
	}
}
