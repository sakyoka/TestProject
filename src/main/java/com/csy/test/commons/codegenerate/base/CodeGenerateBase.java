package com.csy.test.commons.codegenerate.base;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述：生成器
 * @author csy
 * @date 2021年1月29日 上午10:34:35
 */
public interface CodeGenerateBase {

	/**
	 * 
	 * 描述：初始化一些参数
	 * @author csy
	 * @date 2021年1月23日 下午9:32:51
	 * @param dataMetaBase
	 * @return CodeGenerateBase
	 */
	CodeGenerateBase preInit(DataMetaBase dataMetaBase);
	
	/**
	 * 
	 * 描述：生成bean文件
	 * @author csy
	 * @date 2021年1月23日 下午9:33:07
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateBean();
	
	/**
	 * 
	 * 描述：生成xml
	 * @author csy
	 * @date 2021年1月23日 下午9:33:13
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateMapper();
	
	/**
	 * 
	 * 描述：生成DAO
	 * @author csy
	 * @date 2021年1月23日 下午9:33:17
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateDao();
	
	/**
	 * 
	 * 描述：生成service
	 * @author csy
	 * @date 2021年1月23日 下午9:33:21
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateService();
	
	/**
	 * 
	 * 描述：生成service impl
	 * @author csy
	 * @date 2021年1月23日 下午9:33:25
	 * @return dataMetaBase
	 */
	CodeGenerateBase generateServiceImpl();
	
	/**
	 * 
	 * 描述：生成controller
	 * @author csy
	 * @date 2021年1月24日 下午3:34:19
	 * @return CodeGenerateBase
	 */
	CodeGenerateBase generateController();
	
	/**
	 * 描述：初始化参数
	 * @author csy 
	 * @date 2021年3月10日 上午11:15:43
	 * @param codeGenerateParams
	 * @param dataMetaBase
	 * @return CodeGenerateBaseInitParams
	 */
	default CodeGenerateBaseInitParams initCodeGenerateBaseInitParams(CodeGenerateParams codeGenerateParams , DataMetaBase dataMetaBase){
		
		String tableName = dataMetaBase.getTableMessage().getTableName();
		
		String beanName = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
		
		String basePackage = codeGenerateParams.getBasePackageMap().get(tableName); 		
		String prePath = basePackage + "." + StringUtils.lowerCase(tableName.replace("_", "")) + ".";
		String daoPath = prePath + ClassifyConstants.DAO; 
		String fullDaoName = beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.DAO);
		
		String beanPath = prePath + ClassifyConstants.BEAN;
		String fullBeanName = beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.BEAN);
 		
		String xmlPath = prePath + ClassifyConstants.DAO + "." + ClassifyConstants.XML;
 		
		String servicePath = prePath + ClassifyConstants.SERVICE;
		String fullServiceName = beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.SERVICE);
 		
		String serviceImplPath = prePath + ClassifyConstants.SERVICE + "." + ClassifyConstants.SERVICE_IMPL;
		String fullServiceImplName = beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.SERVICE_IMPL);
		
		String controllerPath =  prePath + ClassifyConstants.CONTROLLER;
		String fullControllerName = beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.CONTROLLER);
		return CodeGenerateBaseInitParams.builder()
				.tableName(tableName)
				.beanName(beanName)
				.daoPath(daoPath)
				.fullDaoName(fullDaoName)
				.beanPath(beanPath)
				.fullBeanName(fullBeanName)
				.xmlPath(xmlPath)
				.servicePath(servicePath)
				.fullServiceName(fullServiceName)
				.serviceImplPath(serviceImplPath)
				.fullServiceImplName(fullServiceImplName)
				.controllerPath(controllerPath)
				.fullControllerName(fullControllerName)
				.build();
		
	}
}
