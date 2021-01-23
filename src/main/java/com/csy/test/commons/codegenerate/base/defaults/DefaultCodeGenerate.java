package com.csy.test.commons.codegenerate.base.defaults;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.codegenerate.annotation.MapperTemplate;
import com.csy.test.commons.codegenerate.base.CodeGenerateBase;
import com.csy.test.commons.codegenerate.bean.BeanFieldMessage;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MybatisMapperTemplateBase;
import com.csy.test.commons.codegenerate.constants.ClassifyConstants;
import com.csy.test.commons.codegenerate.constants.FileSuffixEnum;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.bean.base.TableMessage;
import com.csy.test.commons.codegenerate.database.util.DataMetaUtils;
import com.csy.test.commons.codegenerate.mappertemplate.MapperTemplateMessage;
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
	
	private String beanName;
	
	public DefaultCodeGenerate(CodeGenerateParams codeGenerateParams) {
		this.codeGenerateParams = codeGenerateParams;
	}
	
	@Override
	public DefaultCodeGenerate preInit(DataMetaBase dataMetaBase) {
		
		this.dataMetaBase = dataMetaBase;
		
		String tableName = dataMetaBase.getTableMessage().getTableName();
		this.beanName = StrUtil.upperFirst(StrUtil.toCamelCase(tableName));
		
		String basePackage = this.codeGenerateParams.getBasePackageMap().get(tableName);
 		String daoPath = basePackage + "." 
 		                 + StringUtils.lowerCase(tableName.replace("_", "")) 
 		                 + "." + ClassifyConstants.DAO + "." + this.beanName + this.codeGenerateParams.getDaoSuffixName();
		this.daoPath = daoPath;
		
 		String beanPath = basePackage + "." 
		                 + StringUtils.lowerCase(tableName.replace("_", "")) 
		                 + "." + ClassifyConstants.BEAN + "." +  this.beanName + this.codeGenerateParams.getBeanSuffixName();
 		this.beanPath = beanPath;
 		
		return this;
	}
	
	@Override
	public CodeGenerateBase generateBean() {
		TableMessage tableMessage = dataMetaBase.getTableMessage(); 
		List<BeanFieldMessage> beanFieldMessages = DataMetaUtils.tranferToBeanFields(dataMetaBase.getColumnMetaDatas());
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
			.append("package ").append(this.beanPath).append(";")
			.append(LineConstants.WRAP).append(LineConstants.WRAP)
			.append("public class ").append(this.beanName).append(" {")
			.append(LineConstants.WRAP).append(LineConstants.WRAP);
		
		beanFieldMessages.forEach((e) -> {
			stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
			             .append("private ").append(e.getFieldType()).append(" ").append(e.getFieldName()).append(";").append("//").append(e.getRemarks())
			             .append(LineConstants.WRAP).append(LineConstants.WRAP);
		}); 
		
		beanFieldMessages.forEach((e) -> {
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
			             .append(LineConstants.WRAP);
		}); 
		stringBuilder.append("}");
		System.out.println("starting to create file:" + this.beanPath);
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , 
				tableMessage.getTableName() , stringBuilder.toString() , codeGenerateParams.getBeanSuffixName() , FileSuffixEnum.BEAN);
		return this;
	}
	
	@Override
	public CodeGenerateBase generateMapper() {
		TableMessage tableMessage = dataMetaBase.getTableMessage(); 
		List<BeanFieldMessage> beanFieldMessages = DataMetaUtils.tranferToBeanFields(dataMetaBase.getColumnMetaDatas());

		StringBuilder getBuilder = new StringBuilder("select ");
		StringBuilder updateBuilder = new StringBuilder("update ").append(tableMessage.getTableName()).append(" set ");
		StringBuilder insertBuilder = new StringBuilder("insert into ").append(tableMessage.getTableName()).append("(");
		String primaryKey = null;
		for (BeanFieldMessage e:beanFieldMessages) {
 			String dataBaseField = StrUtil.toUnderlineCase(e.getFieldName());
 			getBuilder.append(dataBaseField).append(",");
 			if (e.getPrimaryKey()) primaryKey = dataBaseField;		
		}
		
		String getString = getBuilder.substring(0, getBuilder.length() - 1);
		getBuilder.setLength(0);
 		getBuilder.append(getString).append(" from ").append(tableMessage.getTableName());
 		String findListSql = getBuilder.toString();
 		String getOneSql = getBuilder.append(" where ")
					 				 .append(primaryKey).append(" = ").append("#{").append(StrUtil.toCamelCase(primaryKey)).append("}")
					 				 .toString();
 		String deleteSql = "delete from "+ tableMessage.getTableName() +" where " + primaryKey + " = #{" + StrUtil.toCamelCase(primaryKey) + "}";
 		
 		MybatisMapperTemplateBase templateBase = new MybatisMapperTemplateBase();
		templateBase.setDaoPath(this.daoPath);
		templateBase.setJavaBeanPath(this.beanPath);
		templateBase.setDeleteSql(deleteSql);
		templateBase.setFindListSql(findListSql);
		templateBase.setGetOneSql(getOneSql);

		String templateContents = MapperTemplateMessage.getTemplateContents();
		String content = TemplateUtils.fillTemplate(this.getTemplateContentsMap(templateBase), templateContents);
		System.out.println("starting to create file:" + this.daoPath.replace("Dao", "Mapper"));
		codeGenerateParams.getWriteFileBase().write(codeGenerateParams , 
				tableMessage.getTableName() , content, "Mapper", FileSuffixEnum.MAPPER);
		return this;
	}
	
	@Override
	public CodeGenerateBase generateDao() {
		return this;
	}
	
	@Override
	public CodeGenerateBase generateService() {
		return this;
	}
	
	@Override
	public CodeGenerateBase generateServiceImpl() {
		return this;
	}
	
	private Map<String, String> getTemplateContentsMap(MybatisMapperTemplateBase templateBase){
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> contentMap = JSON.parseObject(JSON.toJSONString(templateBase), Map.class);
			@SuppressWarnings("rawtypes")
			Class clazz = templateBase.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field:fields) {
				if (field.isAnnotationPresent(MapperTemplate.class)) {
					MapperTemplate mapperTemplate = field.getAnnotation(MapperTemplate.class);
					contentMap.put(mapperTemplate.tempName() , mapperTemplate.idName());
				}
			}
			System.out.println(contentMap);
			return contentMap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}
