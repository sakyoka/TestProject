package com.csy.test.commons.codegenerate.util;

import java.util.HashMap;
import java.util.Map;

import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;

public class Test {
	
	public static void main(String[] args) {

		Map<String, String> basePackageMap = new HashMap<String, String>();
		basePackageMap.put("generate_field_test", "com.csy.test.commons");
		CodeGenerateParams codeGenerateParams = CodeGenerateParams.getBuilder()
				.basePackageMap(basePackageMap)
				.beanSuffixName("Model")
				.daoSuffixName("Dao")
				.codeCacheBasePath("D:\\code_cache")
				.build();
		if (!codeGenerateParams.getIsBuild()) {
			throw new RuntimeException("需要执行build");
		}
		CodeGenerateUtils.generate(codeGenerateParams);
	}
}
