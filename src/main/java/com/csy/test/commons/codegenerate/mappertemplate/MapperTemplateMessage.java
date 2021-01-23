package com.csy.test.commons.codegenerate.mappertemplate;

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
}
