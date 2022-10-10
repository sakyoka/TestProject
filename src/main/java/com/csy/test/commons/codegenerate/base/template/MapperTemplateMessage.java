package com.csy.test.commons.codegenerate.base.template;

import java.lang.reflect.Field;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.codegenerate.annotation.MapperTemplate;
import com.csy.test.commons.codegenerate.bean.MybatisMapperTemplateBase;
import com.csy.test.commons.utils.file.FileUtils;

public class MapperTemplateMessage {

	public static final String FILE_NAME = "mybatis-mapper.tmp";
	
	private static String TEMPLATE;
	
	static {
		String filePath = MapperTemplateMessage.class.getResource(MapperTemplateMessage.FILE_NAME).getFile();
		TEMPLATE = FileUtils.read(filePath);
	}
	
	public static String getTemplateContents() {
		return TEMPLATE;
	}
	
	public static Map<String, String> getTemplateContentsMap(MybatisMapperTemplateBase templateBase){
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
			return contentMap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
}
