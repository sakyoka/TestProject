package com.csy.test.commons.codegenerate.base.template;

import com.csy.test.commons.utils.file.FileUtils;

/**
 * 
 * 描述：模板字符串获取
 * @author csy
 * @date 2022年7月5日 下午12:20:34
 */
public class CodeTemplate {

	public static final String SERVICE_FILE_NAME = "service.tmp";
	
	public static final String SERVICE_IMPL_FILE_NAME = "service-impl.tmp";
	
	public static final String CONTROLLER_FILE_NAME = "controller.tmp";
	
	private static String SERVICE_TEMPLATE;
	
	private static String SERVICE_IMPL_TEMPLATE;
	
	private static String CONTROLLER_TEMPLATE;
	
	static {
		String serviceFilePath = CodeTemplate.class.getResource(SERVICE_FILE_NAME).getFile();
		SERVICE_TEMPLATE = FileUtils.read(serviceFilePath);
		
		String serviceImplFilePath = CodeTemplate.class.getResource(SERVICE_IMPL_FILE_NAME).getFile();
		SERVICE_IMPL_TEMPLATE = FileUtils.read(serviceImplFilePath);
		
		String controllerFilePath = CodeTemplate.class.getResource(CONTROLLER_FILE_NAME).getFile();
		CONTROLLER_TEMPLATE = FileUtils.read(controllerFilePath);
	}
	
	/**
	 * 描述：service模板内容
	 * @author csy 
	 * @date 2022年7月5日 下午12:30:00
	 * @return service模板内容
	 */
	public static String getServiceTemplateContents() {
		return SERVICE_TEMPLATE;
	}
	
	/**
	 * 描述：service实现类模板内容
	 * @author csy 
	 * @date 2022年7月5日 下午12:30:17
	 * @return service实现类模板内容
	 */
	public static String getServiceImplTemplateContents() {
		return SERVICE_IMPL_TEMPLATE;
	}
	
	/**
	 * 描述：controller模板内容
	 * @author csy 
	 * @date 2022年7月5日 下午12:30:37
	 * @return controller模板内容
	 */
	public static String getControllerTemplateContents() {
		return CONTROLLER_TEMPLATE;
	}
}
