package com.csy.test.commons.codegenerate.base.defaults.methodtemplate.serviceimpl;

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
 * 描述：ServiceImpl模板形式
 * @author csy
 * @date 2022年7月5日 上午11:47:14
 */
public class ServiceImplMethodTemplateImpl implements MethodTemplateGenerate{

	@Override
	public int order() {
		return 0;
	}

	@Override
	public MethodGenerateRecord generateMethodTemplate(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams) {
		
		Map<String, String> tempValueMap = this.getCommonParam(codeGenerateBaseInitParams, codeGenerateParams);
		String template = CodeTemplate.getServiceImplTemplateContents();
		String content = TemplateUtils.fillTemplate(tempValueMap, template);
		
		MethodGenerateRecord methodGenerateRecord = new MethodGenerateRecord();
		methodGenerateRecord.setMethodContent(content);
		List<String> impportBeans = new ArrayList<String>();
		impportBeans.add("java.util.List");
		impportBeans.add("java.util.Optional");
		String beanPath = codeGenerateBaseInitParams.getBeanPath();
		
		String beanName = codeGenerateBaseInitParams.getBeanName();
		String modelName = codeGenerateBaseInitParams.getFullBeanName();
		String queryObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.QUERY);
		String voObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.VO);
		String dtoObjectBeanName = beanName + StrUtil.upperFirst(ClassifyConstants.DTO);
		String daoName = codeGenerateBaseInitParams.getFullDaoName();
		impportBeans.add(beanPath + "." + ClassifyConstants.VO + "." + voObjectBeanName);
		impportBeans.add(beanPath + "." + ClassifyConstants.QUERY + "." + queryObjectBeanName);
		impportBeans.add(beanPath + "." + ClassifyConstants.DTO + "." + dtoObjectBeanName);
		impportBeans.add(beanPath + "." + modelName);
		impportBeans.add(codeGenerateBaseInitParams.getDaoPath() + "." + daoName);
		
		impportBeans.add("java.util.Collections");
		impportBeans.add("java.util.Date");
		impportBeans.add("java.util.List");
		impportBeans.add("org.apache.commons.lang.StringUtils");
		impportBeans.add("org.springframework.beans.factory.annotation.Autowired");
		impportBeans.add("org.springframework.transaction.annotation.Transactional");
		methodGenerateRecord.setImpportBeans(impportBeans);
		return methodGenerateRecord;
	}
}
