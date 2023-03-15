package com.csy.test.commons.codegenerate.base.defaults.writefile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.codegenerate.base.WriteFileBase;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.utils.StrUtil;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述:文件输出-项目型-用于现在项目
 * @author csy
 * @date 2021年1月23日 下午5:18:09
 */
public class WriteFileNowWork implements WriteFileBase{

	@Override
	public void write(CodeGenerateParams codeGenerateParams , String tableName , String contents , FileSuffixEnum fileSuffixEnum) {
		StringBuilder stringBuilder = new StringBuilder(codeGenerateParams.getBasePathMap().get(tableName));
		if (ClassifyConstants.BEAN.equals(fileSuffixEnum.getFileType())) {
			stringBuilder.append(File.separator).append(ClassifyConstants.BEAN).append(File.separator);
		}
		
		if (ClassifyConstants.XML.equals(fileSuffixEnum.getFileType())) {
			stringBuilder.append(File.separator)
			.append(ClassifyConstants.DAO)
			.append(File.separator)
			.append(ClassifyConstants.XML)
			.append(File.separator);
		}
		
		if (ClassifyConstants.DAO.equals(fileSuffixEnum.getFileType())) {
			stringBuilder.append(File.separator).append(ClassifyConstants.DAO).append(File.separator);
		}
		
		if (ClassifyConstants.SERVICE.equals(fileSuffixEnum.getFileType())) {
			stringBuilder.append(File.separator).append(ClassifyConstants.SERVICE).append(File.separator);
		}
		
		if (ClassifyConstants.SERVICE_IMPL.equals(fileSuffixEnum.getFileType())) {
			stringBuilder.append(File.separator)
			.append(ClassifyConstants.SERVICE)
			.append(File.separator)
			.append(ClassifyConstants.SERVICE_IMPL)
			.append(File.separator);
		}
		
		if (ClassifyConstants.CONTROLLER.equals(fileSuffixEnum.getFileType())) {
			stringBuilder.append(File.separator)
			.append(ClassifyConstants.CONTROLLER)
			.append(File.separator);
		}
		
		String path = stringBuilder.toString();
		try {
			Files.createDirectories(Paths.get(path));
		} catch (IOException e) {
			throw new RuntimeException("创建路径失败:" + path, e);
		}
		//文件名字
		String fileName = StringUtils.isNotBlank(codeGenerateParams.getPrefixClassName()) ? 
				codeGenerateParams.getPrefixClassName() : StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
		//文件路径
		path = stringBuilder
				.append(fileName)
				.append(codeGenerateParams.getFileSuffixNameMap().get(fileSuffixEnum.getFileType()))
				.append(".").append(fileSuffixEnum.getSuffixName()).toString();
		FileUtils.writeFile(path , contents);
		
		codeGenerateParams.getCodeFilePaths().add(path);
	}
}
