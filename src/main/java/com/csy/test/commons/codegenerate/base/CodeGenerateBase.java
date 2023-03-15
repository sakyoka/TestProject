package com.csy.test.commons.codegenerate.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.utils.Objects;
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
		
		String beanName = StringUtils.isNotBlank(codeGenerateParams.getPrefixClassName()) 
				? codeGenerateParams.getPrefixClassName() : StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
		
		String basePackage = codeGenerateParams.getBasePackageMap().get(tableName);
		String initPackageName = codeGenerateParams.getExtraInitPackageNameMap().get(tableName);
		String prePath = basePackage + "." + (Objects.isNull(initPackageName) ? StringUtils.lowerCase(tableName.replace("_", "")) : initPackageName) + ".";
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
	
	/**
	 * 描述：收集生成对应类的内部方法
	 * @author csy 
	 * @date 2022年3月15日 下午5:54:24
	 * @param fileSuffixEnum  需要收集的类分类
	 * @param codeGenerateParams
	 * @param codeGenerateBaseInitParams
	 * @return MethodGenerateRecord
	 */
	default MethodGenerateRecord collectClassMethod(FileSuffixEnum fileSuffixEnum, CodeGenerateParams codeGenerateParams, 
			CodeGenerateBaseInitParams codeGenerateBaseInitParams){
		
		Map<FileSuffixEnum, List<MethodTemplateGenerate>> classGenerateMethodsMap = codeGenerateParams.getClassGenerateMethodsMap();
		if (!classGenerateMethodsMap.containsKey(fileSuffixEnum)){
			return null;
		}
		
		List<MethodTemplateGenerate> methodTemplateGenerates = classGenerateMethodsMap.get(fileSuffixEnum);
		//对方法排序
		Collections.sort(methodTemplateGenerates, (o1, o2) -> {
			return o1.order() - o2.order();
		});
		
		MethodGenerateRecord finalMethodGenerateRecord = new MethodGenerateRecord();
		//去重，可能存在相同引入
		Set<String> importBeans = new HashSet<String>();
		StringBuilder methodBudiler = new StringBuilder();
		methodTemplateGenerates.forEach(e -> {
			MethodGenerateRecord result = e.generateMethodTemplate(codeGenerateBaseInitParams, codeGenerateParams);
			if (StringUtils.isNotBlank(result.getMethodContent())){
				methodBudiler.append(result.getMethodContent()).append(LineConstants.WRAP);
			}
			
			if (CollectionUtils.isNotEmpty(result.getImpportBeans())){
				importBeans.addAll(result.getImpportBeans());
			}
		});
		
		List<String> newImportList = new ArrayList<String>(importBeans.size());
		newImportList.addAll(importBeans);
		//对引入包排序
		Collections.sort(newImportList, (o1, o2) -> {
			return o1.compareTo(o2);
		});
		
		finalMethodGenerateRecord.setImpportBeans(newImportList);
		finalMethodGenerateRecord.setMethodContent(methodBudiler.toString());
		return finalMethodGenerateRecord;
	}
}
