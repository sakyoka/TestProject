package com.csy.test.commons.codegenerate.base.defaults.methodtemplate.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csy.test.commons.codegenerate.annotation.MapperTemplate;
import com.csy.test.commons.codegenerate.base.MethodTemplateGenerate;
import com.csy.test.commons.codegenerate.base.bean.CodeGenerateBaseInitParams;
import com.csy.test.commons.codegenerate.base.utils.NoteStringUitls;
import com.csy.test.commons.codegenerate.bean.BeanFieldMessage;
import com.csy.test.commons.codegenerate.bean.CodeGenerateParams;
import com.csy.test.commons.codegenerate.bean.MethodGenerateRecord;
import com.csy.test.commons.codegenerate.bean.MybatisMapperTemplateBase;
import com.csy.test.commons.codegenerate.constants.LineConstants;
import com.csy.test.commons.codegenerate.constants.MethodTypeEnum;
import com.csy.test.commons.codegenerate.database.bean.base.DataMetaBase;
import com.csy.test.commons.codegenerate.database.util.DataMetaUtils;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述：DAO模板方法
 * @author csy
 * @date 2022年7月5日 下午2:30:33
 */
public class DaoMethodCommonImpl implements MethodTemplateGenerate{

	@Override
	public int order() {

		return 0;
	}

	@Override
	public MethodGenerateRecord generateMethodTemplate(CodeGenerateBaseInitParams codeGenerateBaseInitParams,
			CodeGenerateParams codeGenerateParams) {
		String primaryKey = null;
		String fieldType = null;
		DataMetaBase dataMetaBase = codeGenerateParams.getDataMetaBase();
		List<BeanFieldMessage> beanFieldMessages = DataMetaUtils.tranferToBeanFields(dataMetaBase.getColumnMetaDatas());
		for (BeanFieldMessage e:beanFieldMessages) {
 			if (e.getPrimaryKey()) { 
 				primaryKey = e.getFieldName();
 			    fieldType = e.getFieldType();
 			}
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		@SuppressWarnings("rawtypes")
		Class clazz = MybatisMapperTemplateBase.class;
		Field[] fields = clazz.getDeclaredFields();
		String beanName = codeGenerateBaseInitParams.getFullBeanName();
		Map<String, String> params = new HashMap<String, String>();
		for (Field field:fields) {
			if (field.isAnnotationPresent(MapperTemplate.class)) {
				MapperTemplate mapperTemplate = field.getAnnotation(MapperTemplate.class);
				params.clear();
				if (MethodTypeEnum.FIND_LIST.equalsType(mapperTemplate.methodType())) {
					params.put(StrUtil.lowerFirst(beanName), null);
					NoteStringUitls.appendMethodNote(stringBuilder , codeGenerateParams.getAuthor(), mapperTemplate.desc() , params , "List<"+ beanName +">" );
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append("List<").append(beanName).append("> ").append(mapperTemplate.idName()).append("(")
					             .append(beanName).append(" ").append(StrUtil.lowerFirst(beanName)).append(");")
					             .append(LineConstants.WRAP);
				}
				
				if (MethodTypeEnum.GET_ONE.equalsType(mapperTemplate.methodType())) {
					params.put(primaryKey, null);
					NoteStringUitls.appendMethodNote(stringBuilder , codeGenerateParams.getAuthor(), mapperTemplate.desc() , params , beanName);
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append(beanName).append(" ").append(mapperTemplate.idName()).append("(")
					             .append(fieldType).append(" ").append(primaryKey).append(");")
					             .append(LineConstants.WRAP);					
				}
				
				if (MethodTypeEnum.INSERT.equalsType(mapperTemplate.methodType())) {
					params.put(StrUtil.lowerFirst(beanName), null);
					NoteStringUitls.appendMethodNote(stringBuilder , codeGenerateParams.getAuthor(), mapperTemplate.desc() , params , "int");
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append("int ").append(mapperTemplate.idName()).append("(")
					             .append(beanName).append(" ").append(StrUtil.lowerFirst(beanName)).append(");")
					             .append(LineConstants.WRAP);						
				}
				
				if (MethodTypeEnum.UPDATE.equalsType(mapperTemplate.methodType())) {
					params.put(StrUtil.lowerFirst(beanName), null);
					NoteStringUitls.appendMethodNote(stringBuilder , codeGenerateParams.getAuthor(), mapperTemplate.desc() , params , "int");
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append("int ").append(mapperTemplate.idName()).append("(")
					             .append(beanName).append(" ").append(StrUtil.lowerFirst(beanName)).append(");")
					             .append(LineConstants.WRAP);						
				}
				
				if (MethodTypeEnum.DELETE.equalsType(mapperTemplate.methodType())) {
					params.put(primaryKey , null);
					NoteStringUitls.appendMethodNote(stringBuilder , codeGenerateParams.getAuthor(), mapperTemplate.desc() , params , "int");
					stringBuilder.append(LineConstants.BLANK_SPACE_FOUR)
					             .append("int ").append(mapperTemplate.idName()).append("(")
					             .append(fieldType).append(" ").append(primaryKey).append(");")
					             .append(LineConstants.WRAP);	
				}
			}
		}
		MethodGenerateRecord methodGenerateRecord = new MethodGenerateRecord();
		methodGenerateRecord.setMethodContent(stringBuilder.toString());
		List<String> impportBeans = new ArrayList<String>();
		impportBeans.add("java.util.*");
		impportBeans.add(codeGenerateBaseInitParams.getBeanPath() + "." + codeGenerateBaseInitParams.getFullBeanName());
		methodGenerateRecord.setImpportBeans(impportBeans);
		return methodGenerateRecord;
	}

}
