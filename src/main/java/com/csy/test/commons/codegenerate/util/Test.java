package com.csy.test.commons.codegenerate.util;

import java.util.HashMap;
import java.util.Map;

import com.csy.test.commons.codegenerate.base.defaults.WriteFileNowWork;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.valid.bean.ParamValidResult;
import com.csy.test.commons.valid.utils.ValidUtils;

public class Test {
	
	public static void main(String[] args) {

		Map<String, String> basePackageMap = new HashMap<String, String>();
		basePackageMap.put("appr_approriate_record", "com.csy.test.commons");//表名-生成包目录
		CodeGenerateParams codeGenerateParams = CodeGenerateParams.getBuilder()
				.basePackageMap(basePackageMap)
				.codeCacheBasePath("D:\\code_cache")
				.author("csy")
				.writeFileBase(new WriteFileNowWork())
				.build();
		
		ParamValidResult validResult = ValidUtils.valid(codeGenerateParams);
		if(validResult.getHasError()) {
			throw new RuntimeException(validResult.toString());
		}
		
		if (!codeGenerateParams.getIsBuild()) {
			throw new RuntimeException("需要执行build");
		}
		
		CodeGenerateUtils.generate(codeGenerateParams);
	}
}
