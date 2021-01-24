package com.csy.test.commons.codegenerate.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.codegenerate.base.CodeGenerateBase;
import com.csy.test.commons.codegenerate.base.defaults.DefaultCodeGenerate;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：代码生成
 * @author csy
 * @date 2021年1月22日 下午6:01:33
 */
public class CodeGenerateUtils {
	
	private CodeGenerateUtils(){}
	
	/**
	 * 
	 * 描述：代码生成
	 * @author csy
	 * @date 2021年1月24日 下午4:43:14
	 * @param codeGenerateParams 初始化参数对象
	 * @param codeGenerate 生成器
	 */
	public static void generate(CodeGenerateParams codeGenerateParams , CodeGenerateBase codeGenerate){
		Map<String, String> basePackageMap = codeGenerateParams.getBasePackageMap();
		try {
			System.out.println("starting to generate code...");
			basePackageMap.forEach((k , v) -> {
				
				String basePath = codeGenerateParams.getBasePathMap().get(k) + StringUtils.lowerCase(k).replace("_", "");
				codeGenerateParams.getBasePathMap().put(k, basePath);
				DataMetaBase dataMetaBase = ClassUtils.newInstance(codeGenerateParams.getDataMetaBaseClass());
				codeGenerate.preInit(dataMetaBase.tableName(k).initDataMetaBase())
				            .generateBean()
							.generateMapper()
							.generateDao()
							.generateService()
							.generateServiceImpl()
							.generateController();
			});
			
			System.out.println("end generate code...");
		}catch (Exception e) {
			String deleteDir = codeGenerateParams.getCodeCacheBasePath() + File.separator + codeGenerateParams.getUuidPath();
			System.out.println("generate code fail" + " , and then delete(" + deleteDir + ")dir...");
			System.out.println(e);
			try {
				FileUtils.deletes(deleteDir);
			} catch (Exception e2) {
				System.out.println("delete " + deleteDir + " fail: " + e2.getMessage());
			}
		} 

	}
	
	/**
	 * 
	 * 描述：代码生成
	 * @author csy
	 * @date 2021年1月24日 下午4:43:07
	 * @param codeGenerateParams 初始化参数对象
	 */
	public static void generate(CodeGenerateParams codeGenerateParams){
		//使用默认的生成器DefaultCodeGenerate
		generate(codeGenerateParams , new DefaultCodeGenerate(codeGenerateParams));
	}
}
