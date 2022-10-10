package com.csy.test.commons.codegenerate.base.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeGenerateBaseInitParams {

	private String daoPath;
	
	private String beanPath;
	
	private String xmlPath;
	
	private String servicePath;
	
	private String serviceImplPath;
	
	private String controllerPath;
	
	private String beanName;
	
	private String tableName;
	
	private String fullDaoName;
	
	private String fullBeanName;

	private String fullServiceName;
	
	private String fullServiceImplName;
	
	private String fullControllerName;
}
