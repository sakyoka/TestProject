package com.csy.test.commons.codegenerate.base.defaults;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.csy.test.commons.codegenerate.annotation.MapperTemplate;
import com.csy.test.commons.codegenerate.base.CodeGenerateBase;
import com.csy.test.commons.codegenerate.bean.BeanFieldMessage;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MybatisMapperTemplateBase;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.constants.MethodTypeEnum;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.bean.base.TableMessage;
import com.csy.test.commons.codegenerate.database.util.DataMetaUtils;
import com.csy.test.commons.codegenerate.mappertemplate.MapperTemplateMessage;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.StrUtil;
import com.csy.test.commons.utils.TemplateUtils;

/**
 * 
 * 描述:生成各种文件
 * @author csy
 * @date 2021年1月23日 下午8:08:55
 */
public class DefaultCodeGenerate implements CodeGenerateBase{

	private CodeGenerateParams codeGenerateParams;
	
	private DataMetaBase dataMetaBase;
	
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
	
	private List<BeanFieldMessage> beanFieldMessages;
	
	public DefaultCodeGenerate(CodeGenerateParams codeGenerateParams) {
		this.codeGenerateParams = codeGenerateParams;
	}
	
	@Override
	public DefaultCodeGenerate preInit(DataMetaBase dataMetaBase) {
		
		this.dataMetaBase = dataMetaBase;
		
		this.tableName = dataMetaBase.getTableMessage().getTableName();
		
		this.beanName = StrUtil.upperFirst(StrUtil.toCamelCase(this.tableName));
		
		String basePackage = this.codeGenerateParams.getBasePackageMap().get(this.tableName); 		
		String prePath = basePackage + "." + StringUtils.lowerCase(tableName.replace("_", "")) + ".";
		this.daoPath = prePath + ClassifyConstants.DAO; 
		this.fullDaoName = this.beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.DAO);
		
 		this.beanPath = prePath + ClassifyConstants.BEAN;
 		this.fullBeanName = this.beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.BEAN);
 		
 		this.xmlPath = prePath + ClassifyConstants.DAO + "." + ClassifyConstants.XML;
 		
 		this.servicePath = prePath + ClassifyConstants.SERVICE;
 		this.fullServiceName = this.beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.SERVICE);
 		
		this.serviceImplPath = prePath + ClassifyConstants.SERVICE + "." + ClassifyConstants.SERVICE_IMPL;
		this.fullServiceImplName = this.beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.SERVICE_IMPL);
		
		this.controllerPath =  prePath + ClassifyConstants.CONTROLLER;
		this.fullControllerName = this.beanName + codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.CONTROLLER);
		
		this.beanFieldMessages = DataMetaUtils.tranferToBeanFields(dataMetaBase.getColumnMetaDatas());
 		return this;
	}
	
	@Override
	public CodeGenerateBase generateBean() {
		
		this.preAssert();
		
		String contents = this.getBeanContents();
		System.out.println("execuete generateBean starting to create file:" + this.beanPath);
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.BEAN);
		System.out.println();
		return this;
	}
	
	@Override
	public CodeGenerateBase generateMapper() {
		
		this.preAssert();
		
		String contents = this.getMapperContents();
		System.out.println("execuete generateMapper starting to create file:" + this.xmlPath);
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.MAPPER);
		System.out.println();
		return this;
	}
	
	@Override
	public CodeGenerateBase generateDao() {
		
		this.preAssert();
		
		String contents = this.getDaoContents();
		System.out.println("execuete generateDao starting to create file:" + this.daoPath);
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.DAO);
		System.out.println();
		return this;
	}
	
	@Override
	public CodeGenerateBase generateService() {
		
		this.preAssert();
		
		String contents = this.getServiceContents();
		System.out.println("execuete generateService starting to create file:" + this.servicePath);
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.SERVICE);
		System.out.println();
		return this;
	}
	

	@Override
	public CodeGenerateBase generateServiceImpl() {
		
		this.preAssert();
		
		String contents = this.getServiceImplContents();
		System.out.println("execuete generateServiceImpl starting to create file:" + this.serviceImplPath);
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.SERVICE_IMPL);
		System.out.println();
		return this;
	}
	
	@Override
	public CodeGenerateBase generateController() {
		
		this.preAssert();
		
		String contents = this.getControllerContents();
		System.out.println("execuete generateController starting to create file:" + this.controllerPath);
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , this.tableName , contents , FileSuffixEnum.CONTRLLER);
		System.out.println();
		return this;
	}

	/**
	 * 
	 * 描述：获取bean内容
	 * @author csy
	 * @date 2021年1月24日 下午1:52:29
	 * @return bean内容
	 */
	private String getBeanContents() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.beanPath).append(";")
			.append(LineConstants.WRAP).append(LineConstants.WRAP)
		    .append("import java.util.*;")
		    .append(LineConstants.WRAP).append(LineConstants.WRAP);
		appendClassNote(stringBuilder , dataMetaBase.getTableMessage().getRemarks() + "实体类")
			.append("public class ").append(this.fullBeanName)
			.append(" {").append(LineConstants.WRAP).append(LineConstants.WRAP);
		
		this.beanFieldMessages.forEach((e) -> {
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			             .append("private ").append(e.getFieldType()).append(" ").append(e.getFieldName()).append(";")
			             .append("//").append(StrUtil.removeAllLineBreaks(e.getRemarks()))
			             .append(LineConstants.WRAP).append(LineConstants.WRAP);
		}); 
		
		this.beanFieldMessages.forEach((e) -> {
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			             .append("public ").append(e.getFieldType()).append(" ")
			             .append("get").append(StrUtil.upperFirst(e.getFieldName())).append("(){")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
			             .append("return ") .append(e.getFieldName()) .append(";").append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append("}")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.WRAP);
			
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			             .append("public void set").append(StrUtil.upperFirst(e.getFieldName())).append("(").append(e.getFieldType()).append(" ").append(e.getFieldName()).append("){")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append("this.").append(e.getFieldName()).append(" = ").append(e.getFieldName()).append(";")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.BLANK_SPACE_FOUR)
			             .append("}")
			             .append(LineConstants.WRAP)
			             .append(LineConstants.WRAP);
		}); 
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取xml内容
	 * @author csy
	 * @date 2021年1月24日 下午1:51:16
	 * @return xml内容
	 */
	private String getMapperContents() {
		TableMessage tableMessage = dataMetaBase.getTableMessage(); 
		StringBuilder getBuilder = new StringBuilder("select ");
		StringBuilder updateBuilder = new StringBuilder("update ").append(tableMessage.getTableName()).append(" set ");
		StringBuilder dataBaseinsertBuilder = new StringBuilder("(");
		StringBuilder javainsertBuilder = new StringBuilder("(");
		String primaryKey = null;
		for (BeanFieldMessage e:this.beanFieldMessages) {
 			
			String dataBaseField = StrUtil.toUnderlineCase(e.getFieldName());
 			
 			getBuilder.append(LineConstants.WRAP)
 			.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
 			.append(dataBaseField).append(",");
 			
 			dataBaseinsertBuilder.append(LineConstants.WRAP)
				.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
				.append(dataBaseField).append(",");
 			javainsertBuilder.append(LineConstants.WRAP)
				.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
				.append("#{").append(e.getFieldName()).append("},");
 			
 			if (e.getPrimaryKey()) {
 				primaryKey = dataBaseField;
 			}else {
 				updateBuilder.append(LineConstants.WRAP)
				.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
				.append(dataBaseField).append(" = #{").append(e.getFieldName()).append("},");
 			}
		}
		
		dataBaseinsertBuilder.setLength(dataBaseinsertBuilder.length() - 1);
		javainsertBuilder.setLength(javainsertBuilder.length() - 1);
		dataBaseinsertBuilder.append(LineConstants.WRAP)
			.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
			.append(")");
		javainsertBuilder.append(")");
		String insertSql = new StringBuilder("insert into ")
								.append(tableMessage.getTableName())
								.append(dataBaseinsertBuilder)
								.append(" values ")
								.append(javainsertBuilder)
								.toString();
		StringBuilder whereSql = new StringBuilder().append(" where ").append(primaryKey).append(" = ").append("#{").append(StrUtil.toCamelCase(primaryKey)).append("}");
		updateBuilder.setLength(updateBuilder.length() - 1);
		String updateSql = updateBuilder.append(LineConstants.WRAP)
				.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
				.append(whereSql).toString();
		
		getBuilder.setLength(getBuilder.length() - 1);
 		getBuilder.append(LineConstants.WRAP)
			.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
			.append(" from ").append(tableMessage.getTableName());
 		String findListSql = getBuilder.toString();
 		String getOneSql = getBuilder.append(LineConstants.WRAP)
 				.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
 				.append(whereSql).toString();
 		
 		String deleteSql = "delete from "+ tableMessage.getTableName() + whereSql.toString();
 		
 		String daoPath = this.daoPath + "." + this.fullDaoName;
 		String beanPath = this.beanPath + "." + this.fullBeanName;
 		MybatisMapperTemplateBase templateBase = new MybatisMapperTemplateBase(daoPath , beanPath , findListSql , getOneSql , insertSql , updateSql ,deleteSql);
		String templateContents = MapperTemplateMessage.getTemplateContents();
		String content = TemplateUtils.fillTemplate(MapperTemplateMessage.getTemplateContentsMap(templateBase), templateContents);
		return content;
	}
	
	/**
	 * 
	 * 描述：获取dao内容
	 * @author csy
	 * @date 2021年1月24日 下午2:01:31
	 * @return dao内容
	 */
	private String getDaoContents() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.daoPath).append(";")
			.append(LineConstants.WRAP).append(LineConstants.WRAP)
		    .append("import java.util.*;")
		    .append(LineConstants.WRAP)
			.append("import ").append(this.beanPath).append(".").append(this.fullBeanName).append(";")
			.append(LineConstants.WRAP)
			.append(LineConstants.WRAP);
			appendClassNote(stringBuilder, dataMetaBase.getTableMessage().getRemarks() + "dao")
			.append("public interface ").append(this.beanName).append(this.codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.DAO)).append(" {")
			.append(LineConstants.WRAP).append(LineConstants.WRAP);

		String primaryKey = null;
		String fieldType = null;
		for (BeanFieldMessage e:this.beanFieldMessages) {
 			if (e.getPrimaryKey()) { 
 				primaryKey = e.getFieldName();
 			    fieldType = e.getFieldType();
 			}
		}
		
		@SuppressWarnings("rawtypes")
		Class clazz = MybatisMapperTemplateBase.class;
		Field[] fields = clazz.getDeclaredFields();
		String beanName = this.beanName + this.codeGenerateParams.getFileSuffixNameMap().get(ClassifyConstants.BEAN);
		Map<String, String> params = new HashMap<String, String>();
		for (Field field:fields) {
			if (field.isAnnotationPresent(MapperTemplate.class)) {
				MapperTemplate mapperTemplate = field.getAnnotation(MapperTemplate.class);
				params.clear();
				if (MethodTypeEnum.FIND_LIST.equalsType(mapperTemplate.methodType())) {
					params.put(StrUtil.lowerFirst(beanName), null);
					appendMethodNote(stringBuilder , mapperTemplate.desc() , params , "List<"+ beanName +">" );
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append("List<").append(beanName).append("> ").append(mapperTemplate.idName()).append("(")
					             .append(beanName).append(" ").append(StrUtil.lowerFirst(beanName)).append(");")
					             .append(LineConstants.WRAP);
				}
				
				if (MethodTypeEnum.GET_ONE.equalsType(mapperTemplate.methodType())) {
					params.put(primaryKey, null);
					appendMethodNote(stringBuilder , mapperTemplate.desc() , params , beanName);
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append(beanName).append(" ").append(mapperTemplate.idName()).append("(")
					             .append(fieldType).append(" ").append(primaryKey).append(");")
					             .append(LineConstants.WRAP);					
				}
				
				if (MethodTypeEnum.INSERT.equalsType(mapperTemplate.methodType())) {
					params.put(StrUtil.lowerFirst(beanName), null);
					appendMethodNote(stringBuilder , mapperTemplate.desc() , params , "int");
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append("int ").append(mapperTemplate.idName()).append("(")
					             .append(beanName).append(" ").append(StrUtil.lowerFirst(beanName)).append(");")
					             .append(LineConstants.WRAP);						
				}
				
				if (MethodTypeEnum.UPDATE.equalsType(mapperTemplate.methodType())) {
					params.put(StrUtil.lowerFirst(beanName), null);
					appendMethodNote(stringBuilder , mapperTemplate.desc() , params , "int");
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append("int ").append(mapperTemplate.idName()).append("(")
					             .append(beanName).append(" ").append(StrUtil.lowerFirst(beanName)).append(");")
					             .append(LineConstants.WRAP);						
				}
				
				if (MethodTypeEnum.DELETE.equalsType(mapperTemplate.methodType())) {
					params.put(primaryKey , null);
					appendMethodNote(stringBuilder , mapperTemplate.desc() , params , "int");
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append("int ").append(mapperTemplate.idName()).append("(")
					             .append(fieldType).append(" ").append(primaryKey).append(");")
					             .append(LineConstants.WRAP);	
				}
			}
		}		
		
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取serviceImpl内容
	 * @author csy
	 * @date 2021年1月24日 下午2:01:08
	 * @return serviceImpl内容
	 */
	private String getServiceImplContents() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.serviceImplPath).append(";")
			.append(LineConstants.WRAP)
			.append(LineConstants.WRAP)
			.append("import ").append(this.servicePath).append(".").append(this.fullServiceName).append(";")
			//.append("import ").append(this.beanPath).append(";")
			.append(LineConstants.WRAP)
			.append(LineConstants.WRAP);
		appendClassNote(stringBuilder , dataMetaBase.getTableMessage().getRemarks() + "业务实现类")
			.append("public class ").append(this.fullServiceImplName)
			.append(" implements ").append(this.fullServiceName)
			.append(" {")
			.append(LineConstants.WRAP).append(LineConstants.WRAP);

		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取service内容
	 * @author csy
	 * @date 2021年1月24日 下午2:01:42
	 * @return service内容
	 */
	private String getServiceContents() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.servicePath).append(";")
			.append(LineConstants.WRAP)
			.append(LineConstants.WRAP);
		appendClassNote(stringBuilder , dataMetaBase.getTableMessage().getRemarks() + "业务接口")
			.append("public interface ").append(this.fullServiceName).append(" {")
			.append(LineConstants.WRAP).append(LineConstants.WRAP);

		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：获取controller内容
	 * @author csy
	 * @date 2021年1月24日 下午3:39:15
	 * @return controller内容
	 */
	private String getControllerContents() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.controllerPath).append(";")
			.append(LineConstants.WRAP)
			.append(LineConstants.WRAP);
		appendClassNote(stringBuilder , dataMetaBase.getTableMessage().getRemarks() + "控制层")
			.append("public class ").append(this.fullControllerName).append(" {")
			.append(LineConstants.WRAP).append(LineConstants.WRAP);

		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * 描述：判断前置条件是否执行没有
	 * @author csy
	 * @date 2021年1月24日 下午2:11:21
	 */
	private void preAssert() {
		Objects.notNullAssert(this.dataMetaBase, "dataMetaBase is not allow null");
	}
	
	/**
	 * 描述：类注释
	 * @author csy 
	 * @date 2021年1月29日 下午3:22:01
	 * @param stringBuilder
	 * @param desc 描述
	 * @return StringBuilder
	 */
	private StringBuilder appendClassNote(StringBuilder stringBuilder , String desc) {
		stringBuilder.append(LineConstants.WRAP)
		             .append("/**").append(LineConstants.WRAP)
		             .append(" * 描述：").append(desc == null ? "" : desc).append(LineConstants.WRAP)
		             .append(" * @author ").append(codeGenerateParams.getAuthor()).append(LineConstants.WRAP)
		             .append(" * @date ").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append(LineConstants.WRAP)
		             .append(" */").append(LineConstants.WRAP);
		return stringBuilder;
	}
	
	/**
	 * 描述：方法注释
	 * @author csy 
	 * @date 2021年1月29日 下午3:22:12
	 * @param stringBuilder
	 * @param desc 描述
	 * @param params 参数集合
	 * @param returnParam 返回值
	 * @return StringBuilder
	 */
	private StringBuilder appendMethodNote(StringBuilder stringBuilder , String desc , Map<String, String> params , String returnParam) {
		stringBuilder.append(LineConstants.WRAP)
		             .append(LineConstants.BLANK_SPACE_FOUR)
		             .append("/**").append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR)
		             .append(" * 描述：").append(desc == null ? "" : desc).append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR)
		             .append(" * @author ").append(codeGenerateParams.getAuthor()).append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR)
		             .append(" * @date ").append(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")).append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR);
		
		if (params != null){
			params.forEach((k , v) -> {
				if (k != null)
				    stringBuilder
				     .append(" * @param ").append(k).append(" ").append(v != null ? v : "").append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR);
			});
		}
		if (returnParam != null){
		    stringBuilder
		             .append(" * @return ").append(returnParam).append(LineConstants.WRAP).append(LineConstants.BLANK_SPACE_FOUR);			
		}
		stringBuilder.append(" */").append(LineConstants.WRAP);
		return stringBuilder;
	}
}
