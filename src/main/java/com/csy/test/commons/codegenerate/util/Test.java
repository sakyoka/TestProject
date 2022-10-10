package com.csy.test.commons.codegenerate.util;

import com.csy.test.commons.codegenerate.base.defaults.writefile.WriteFileNowWork;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;

public class Test {
	
	public static void main(String[] args) {

		//Map<String, String> basePackageMap = new HashMap<String, String>();
		//basePackageMap.put("appr_approriate_record", "com.csy.test.commons");//表名-生成包目录
		CodeGenerateParams codeGenerateParams = CodeGenerateParams.getBuilder()
				//.basePackageMap(basePackageMap)
				.codeCacheBasePath("D:\\code_cache")
				.author("csy")
				.writeFileBase(new WriteFileNowWork())
				//.baseProjectPathMap("appr_approriate_record", "D:\\githubwordpacenew\\TestProject")
				.baseProjectPathAndPackageMap("scheduler_task_ref_local", "D:\\githubwordpacenew\\TestProject", "com.csy.test.commons")
				.build();
		
		if (!codeGenerateParams.getIsBuild()) {
			throw new RuntimeException("需要执行build");
		}
		
		CodeGenerateUtils.generate(codeGenerateParams);
	}
}
