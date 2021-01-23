package com.csy.test.commons.codegenerate.util;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.codegenerate.base.CodeGenerateBase;
import com.csy.test.commons.codegenerate.base.defaults.DefaultCodeGenerate;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.bean.base.defaults.DataMetaTemplate;
import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：代码生成
 * @author csy
 * @date 2021年1月22日 下午6:01:33
 */
public class CodeGenerateUtils {
	
	private CodeGenerateUtils(){}
	
	public static void generate(CodeGenerateParams codeGenerateParams){
		Map<String, String> basePackageMap = codeGenerateParams.getBasePackageMap();
		try {
			System.out.println("starting to generate code...");
			CodeGenerateBase codeGenerate = new DefaultCodeGenerate(codeGenerateParams); 
			basePackageMap.forEach((k , v) -> {
				
				String basePath = codeGenerateParams.getBasePathMap().get(k) + StringUtils.lowerCase(k).replace("_", "");
				codeGenerateParams.getBasePathMap().put(k, basePath);
				DataMetaBase dataMetaBase = new DataMetaTemplate(null , k);
				codeGenerate.preInit(dataMetaBase)
				            .generateBean()
							.generateMapper()
							.generateDao()
							.generateService()
							.generateServiceImpl();
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
}
