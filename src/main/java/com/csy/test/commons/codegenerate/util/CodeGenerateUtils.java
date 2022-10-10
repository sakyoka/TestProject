package com.csy.test.commons.codegenerate.util;

import java.io.File;
import com.csy.test.commons.codegenerate.base.CodeGenerateBase;
import com.csy.test.commons.codegenerate.base.TranferFileBase;
import com.csy.test.commons.codegenerate.base.defaults.codegenerate.DefaultCodeGenerate;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.utils.Objects;
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
		
		excueteGenerate(codeGenerateParams , codeGenerate);
		
		tranferFileToTarget(codeGenerateParams);	
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
	
	/**
	 * 描述：代码生成
	 * @author csy 
	 * @date 2021年1月29日 上午11:31:25
	 * @param codeGenerateParams
	 * @param codeGenerate
	 */
	private static void excueteGenerate(CodeGenerateParams codeGenerateParams , CodeGenerateBase codeGenerate){
		try {
			System.out.println("starting to generate code...");
			DataMetaBase dataMetaBase = codeGenerateParams.getDataMetaBase();
			codeGenerateParams.getBasePackageMap().forEach((k , v) -> {
				codeGenerate.preInit(dataMetaBase.tableName(k).initDataMetaBase())
				            .generateBean()
							.generateMapper()
							.generateDao()
							.generateService()
							.generateServiceImpl()
							.generateController();
			});
				
			System.out.println("end generate code...");
			System.out.println();
		}catch (Exception e) {
			String deleteDir = codeGenerateParams.getCodeCacheBasePath() + File.separator + codeGenerateParams.getUuidPath();
			System.out.println("generate code fail" + " , and then delete(" + deleteDir + ")dir...");
			e.printStackTrace();
			try {
				FileUtils.deletes(deleteDir);
			} catch (Exception e2) {
				System.out.println("delete " + deleteDir + " fail: " + e2.getMessage());
			}
		} 
	}

	/**
	 * 描述：文件转移
	 * @author csy 
	 * @date 2021年1月29日 上午11:31:30
	 * @param codeGenerateParams
	 */
	private static void tranferFileToTarget(CodeGenerateParams codeGenerateParams){
		System.out.println("starting to tranfer targetDir");
		if (Objects.notNull(codeGenerateParams.getBaseProjectPathMap())){
			//转移不一定要成功
			TranferFileBase tranferFileBase = codeGenerateParams.getTranferFileBase();
			codeGenerateParams.getBasePathMap().forEach((k , v) -> {
				try {
					if (Objects.notNull(codeGenerateParams.getBaseProjectPathMap().get(k)))
						tranferFileBase.tranfer(codeGenerateParams , k , v);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		System.out.println("end to tranfer targetDir");	
	}
}
