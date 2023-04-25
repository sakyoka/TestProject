package com.csy.test.commons.codegenerate.base.defaults.methodtemplate.controller;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.codegenerate.base.MethodTemplateGenerate;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.base.template.CodeTemplate;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.utils.TemplateUtils;

/**
 * 
 * 描述：Controller模板内容填充
 * @author csy
 * @date 2022年7月5日 下午5:19:50
 */
public class ControllerMethodTemplate implements MethodTemplateGenerate{

	@Override
	public int order() {

		return 0;
	}

	@Override
	public MethodGenerateRecord generateMethodTemplate(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams) {
		
		String template = CodeTemplate.getControllerTemplateContents();
		String content = TemplateUtils.fillTemplate(
				getCommonParam(codeGenerateBaseInitParams, codeGenerateParams), template);
		MethodGenerateRecord methodGenerateRecord = new MethodGenerateRecord();
		List<String> impportBeans = new ArrayList<String>();
		impportBeans.add("org.springframework.web.bind.annotation.GetMapping");
		impportBeans.add("org.springframework.web.bind.annotation.PostMapping");
		impportBeans.add("org.springframework.web.bind.annotation.RequestBody");
		impportBeans.add("org.springframework.web.bind.annotation.RequestMapping");
		impportBeans.add("org.springframework.web.bind.annotation.RequestParam");
		impportBeans.add("org.springframework.beans.factory.annotation.Autowired");
		
		impportBeans.add("io.swagger.annotations.Api");
		impportBeans.add("io.swagger.annotations.ApiImplicitParam");
		impportBeans.add("io.swagger.annotations.ApiOperation");
		impportBeans.add("io.swagger.annotations.ApiSort");
		impportBeans.add("io.swagger.annotations.ApiSort");
		impportBeans.add("io.swagger.annotations.ApiSort");
		methodGenerateRecord.setImpportBeans(impportBeans);
		methodGenerateRecord.setMethodContent(content);
		return methodGenerateRecord;
	}

}
