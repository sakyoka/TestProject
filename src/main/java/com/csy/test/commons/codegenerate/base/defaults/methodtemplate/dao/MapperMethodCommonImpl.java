package com.csy.test.commons.codegenerate.base.defaults.methodtemplate.dao;

import java.util.Collections;
import java.util.List;

import com.csy.test.commons.codegenerate.base.MethodTemplateGenerate;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.base.template.MapperTemplateMessage;
import com.csy.test.commons.codegenerate.bean.BeanFieldMessage;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.codegenerate.bean.MybatisMapperTemplateBase;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.util.DataMetaUtils;
import com.csy.test.commons.database.bean.TableMessage;
import com.csy.test.commons.utils.StrUtil;
import com.csy.test.commons.utils.TemplateUtils;

/**
 * 
 * 描述：mapper内容
 * @author csy
 * @date 2022年7月5日 下午2:41:13
 */
public class MapperMethodCommonImpl implements MethodTemplateGenerate{

	@Override
	public int order() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MethodGenerateRecord generateMethodTemplate(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams) {
		DataMetaBase dataMetaBase = codeGenerateParams.getDataMetaBase();
		String tableName = dataMetaBase.getTableMessage().getTableName();
		TableMessage tableMessage = dataMetaBase.getTableMessage();
		StringBuilder getBuilder = new StringBuilder("select ");
		StringBuilder updateBuilder = new StringBuilder("update ").append(tableMessage.getTableName()).append(" set ");
		StringBuilder dataBaseinsertBuilder = new StringBuilder("(");
		StringBuilder javainsertBuilder = new StringBuilder("(");
		String primaryKey = null;
		String shortTableName = StrUtil.getShort(tableName);
		List<BeanFieldMessage> beanFieldMessages = DataMetaUtils.tranferToBeanFields(dataMetaBase.getColumnMetaDatas());
		for (BeanFieldMessage e:beanFieldMessages) {
 			
			String dataBaseField = StrUtil.toUnderlineCase(e.getFieldName());
 			
 			getBuilder.append(LineConstants.WRAP)
 			.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
 			.append(shortTableName).append(".").append(dataBaseField).append(",");
 			
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
			.append(" from ").append(tableMessage.getTableName()).append(" ").append(shortTableName);
 		String findListSql = getBuilder.toString();
 		String getOneSql = getBuilder.append(LineConstants.WRAP)
 				.append(LineConstants.BLANK_SPACE_FOUR).append(LineConstants.BLANK_SPACE_FOUR)
 				.append(" where ").append(shortTableName + "." + primaryKey)
 				.append(" = ").append("#{").append(StrUtil.toCamelCase(primaryKey)).append("}")
 				.toString();
 		
 		String deleteSql = "delete from "+ tableMessage.getTableName() + whereSql.toString();
 		
 		String daoPath = codeGenerateBaseInitParams.getDaoPath() + "." +  codeGenerateBaseInitParams.getFullDaoName();
 		String beanPath = codeGenerateBaseInitParams.getBeanPath() + "." + codeGenerateBaseInitParams.getFullBeanName();
 		MybatisMapperTemplateBase templateBase = new MybatisMapperTemplateBase(daoPath , beanPath , findListSql , getOneSql , insertSql , updateSql ,deleteSql);
		String templateContents = MapperTemplateMessage.getTemplateContents();
		String content = TemplateUtils.fillTemplate(MapperTemplateMessage.getTemplateContentsMap(templateBase), templateContents);
		MethodGenerateRecord methodGenerateRecord = new MethodGenerateRecord();
		methodGenerateRecord.setMethodContent(content);
		methodGenerateRecord.setImpportBeans(Collections.EMPTY_LIST);
		return methodGenerateRecord;
	}

}
