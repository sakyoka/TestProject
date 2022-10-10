package com.csy.test.commons.codegenerate.base.defaults.methodtemplate.dao;

import com.csy.test.commons.codegenerate.base.MethodTemplateGenerate;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;

/**
 * 
 * 描述：DAO空模板方法
 * @author csy
 * @date 2022年7月5日 下午2:30:33
 */
public class DaoMethodBlankImpl implements MethodTemplateGenerate{

	@Override
	public int order() {

		return 0;
	}

	@Override
	public MethodGenerateRecord generateMethodTemplate(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams) {
		MethodGenerateRecord methodGenerateRecord = new MethodGenerateRecord();
		return methodGenerateRecord;
	}

}
