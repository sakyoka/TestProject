package com.csy.test.commons.codegenerate.base.defaults.methodtemplate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.csy.test.commons.codegenerate.base.MethodTemplateGenerate;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.base.template.CodeTemplate;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.utils.StrUtil;
import com.csy.test.commons.utils.TemplateUtils;

/**
 * 
 * 描述：Service模板形式
 * @author csy
 * @date 2022年7月5日 上午11:47:14
 */
public class ServiceMethodTemplateImpl implements MethodTemplateGenerate{

	@Override
	public int order() {
		return 0;
	}

	@Override
	public MethodGenerateRecord generateMethodTemplate(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams) {

		Map<String, String> tempValueMap = this.getCommonParam(codeGenerateBaseInitParams, codeGenerateParams);
		String template = CodeTemplate.getServiceTemplateContents();
		String content = TemplateUtils.fillTemplate(tempValueMap, template);
		
		MethodGenerateRecord methodGenerateRecord = new MethodGenerateRecord();
		methodGenerateRecord.setMethodContent(content);
		List<String> impportBeans = new ArrayList<String>();
		impportBeans.add("java.util.List");
		
		String beanPath = codeGenerateBaseInitParams.getBeanPath();
		String beanName = codeGenerateBaseInitParams.getBeanName();
		String voObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.VO);
		String queryObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.QUERY);
		String dtoObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.DTO);
		impportBeans.add(beanPath + "." + ClassifyConstants.VO + "." + voObjectBeanName);
		impportBeans.add(beanPath + "." + ClassifyConstants.QUERY + "." + queryObjectBeanName);
		impportBeans.add(beanPath + "." + ClassifyConstants.DTO + "." + dtoObjectBeanName);
		methodGenerateRecord.setImpportBeans(impportBeans);
		return methodGenerateRecord;
	}
}
