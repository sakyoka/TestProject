package com.csy.test.commons.codegenerate.base.defaults;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
	public void write(CodeGenerateParams codeGenerateParams , String tableName , String contents , String beanNameSuffix , FileSuffixEnum fileSuffixEnum) {
		String path = codeGenerateParams.getBasePathMap().get(tableName);
		if (ClassifyConstants.BEAN.equals(fileSuffixEnum.getFileType())) {
			path += File.separator + ClassifyConstants.BEAN + File.separator;
		}
		
		if (ClassifyConstants.XML.equals(fileSuffixEnum.getFileType())) {
			path += File.separator + ClassifyConstants.DAO + File.separator + ClassifyConstants.XML + File.separator;
		}
		
		if (ClassifyConstants.DAO.equals(fileSuffixEnum.getFileType())) {
			path += File.separator + ClassifyConstants.DAO + File.separator;
		}
		
		if (ClassifyConstants.SERVICE.equals(fileSuffixEnum.getFileType())) {
			path += File.separator + ClassifyConstants.SERVICE + File.separator;
		}
		
		if (ClassifyConstants.SERVICE_IMPL.equals(fileSuffixEnum.getFileType())) {
			path += File.separator + ClassifyConstants.SERVICE + File.separator + ClassifyConstants.SERVICE_IMPL + File.separator;
		}
		
		try {
			Files.createDirectories(Paths.get(path));
		} catch (IOException e) {
			throw new RuntimeException("创建路径失败:" + path, e);
		}
		
		path = path + StrUtil.lowerFirst(StrUtil.toCamelCase(tableName)) + beanNameSuffix + "." + ClassifyConstants.JAVA;
		FileUtils.writeFile(path , contents);
		
		codeGenerateParams.getCodeFilePaths().add(path);
	}
}
