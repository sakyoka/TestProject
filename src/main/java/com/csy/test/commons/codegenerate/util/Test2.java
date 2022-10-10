package com.csy.test.commons.codegenerate.util;

import com.csy.test.commons.codegenerate.base.defaults.codegenerate.NowNewWorkCodeGenerate;
import com.csy.test.commons.codegenerate.base.defaults.transferfile.NowNewWorkTransferFile;
import com.csy.test.commons.codegenerate.base.defaults.writefile.WriteFileNowWork;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.database.bean.base.defaults.DataMetaOfCustomize;

public class Test2 {
	
	public static void main(String[] args) {

		CodeGenerateParams codeGenerateParams = CodeGenerateParams.getBuilder()
				//.basePackageMap(basePackageMap)
				.codeCacheBasePath("D:\\code_cache")
				.author("csy")
				.writeFileBase(new WriteFileNowWork())
				.tranferFileBase(new NowNewWorkTransferFile())
				.fileSuffixNameMap(ClassifyConstants.QUERY, "Query")
				.fileSuffixNameMap(ClassifyConstants.DTO, "Dto")
				.fileSuffixNameMap(ClassifyConstants.VO, "Vo")
				//special_procedure_monitor为虚拟表由DataMetaOfCustomize自定义
				.baseProjectPathAndPackageMap("scheduler_task_ref_local", "D:\\souece\\TestProject", "com.csy.test")
				.dataMetaBase(new DataMetaOfCustomize()
						.remarks("测试虚拟表")
						.addPrimaryKeyColumnMedaData("test_id", String.class, "测试id")
						.addCommonColumnMedaData("test_name", Integer.class, "测试名称"))
				.build();
		
		if (!codeGenerateParams.getIsBuild()) {
			throw new RuntimeException("需要执行build");
		}
		
		CodeGenerateUtils.generate(codeGenerateParams, new NowNewWorkCodeGenerate(codeGenerateParams));
	}
}
