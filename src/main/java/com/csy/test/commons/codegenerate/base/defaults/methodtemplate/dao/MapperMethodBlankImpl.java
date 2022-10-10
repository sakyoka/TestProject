package com.csy.test.commons.codegenerate.base.defaults.methodtemplate.dao;

import com.csy.test.commons.codegenerate.base.MethodTemplateGenerate;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.codegenerate.constants.LineConstants;

/**
 * 
 * 描述：mapper空sql内容
 * @author csy
 * @date 2022年7月5日 下午2:41:13
 */
public class MapperMethodBlankImpl implements MethodTemplateGenerate{

	@Override
	public int order() {
		return 0;
	}

	@Override
	public MethodGenerateRecord generateMethodTemplate(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams) {
		MethodGenerateRecord methodGenerateRecord = new MethodGenerateRecord();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(LineConstants.WRAP);
		stringBuilder.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">")
		             .append(LineConstants.WRAP);
		String daoPath = codeGenerateBaseInitParams.getDaoPath();
		String daoName = codeGenerateBaseInitParams.getFullDaoName();
		stringBuilder.append("<mapper namespace=\""+ daoPath + "." + daoName +"\">")
		             .append(LineConstants.WRAP)
		             .append(LineConstants.WRAP);
		stringBuilder.append("</mapper>");
		methodGenerateRecord.setMethodContent(stringBuilder.toString());
		return methodGenerateRecord;
	}

}
