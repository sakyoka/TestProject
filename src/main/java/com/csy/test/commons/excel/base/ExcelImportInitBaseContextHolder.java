package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csy.test.commons.excel.annotion.ImportExcelField;
import com.csy.test.commons.excel.bean.ImportExcelTempBean;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 
 * 描述：导出相关初始化
 * @author csy
 * @date 2020年7月1日 下午6:20:55
 */
public class ExcelImportInitBaseContextHolder {

	private ExcelImportInitBaseContextHolder(){}
	
	/**
	 * 存储字段对应的转换器
	 */
	private Map<String, ExcelImportConvertBase> convertMap;

	private List<ImportExcelTempBean> tempBeans;
	
	/**
	 * 描述：实例化
	 * @author csy 
	 * @date 2020年7月1日 下午6:21:41
	 * @return ExcelImportInitBase
	 */
	public static ExcelImportInitBaseContextHolder newInstance(){
		return new ExcelImportInitBaseContextHolder();
	}
	
	/**
	 * 描述：初始化Convert
	 * @author csy 
	 * @date 2020年7月1日 下午6:20:30
	 * @param clazz
	 * @return ExcelImportInitBase
	 */
	public ExcelImportInitBaseContextHolder initConvert(Class<?> clazz){
		
		if (this.convertMap == null){
			this.convertMap = new HashMap<String, ExcelImportConvertBase>();
		}
		
		Field[] fields = clazz.getDeclaredFields();
		ExcelImportConvertBase convertBase = null;
		ImportExcelField importExcelField= null;
		for (Field field:fields){
			if (!field.isAnnotationPresent(ImportExcelField.class))
				continue;
			
			importExcelField = field.getAnnotation(ImportExcelField.class);
			try {
				convertBase = ClassUtils.newInstance(importExcelField.convertClazz());
			} catch (Exception e) {
				throw new RuntimeException("初始化ImportExcelField convert失败 , fieldName ===>>> " + field.getName() , e);
			}
			
			this.convertMap.put(field.getName(), convertBase);
		}
		
		return this;
	}
	
	/**
	 * 
	 * 描述：初始化ImportExcelTempBean
	 * @author csy
	 * @date 2020年7月1日 下午10:46:00
	 * @param clazz
	 * @return ExcelImportInitBase
	 */
	public ExcelImportInitBaseContextHolder initTempBeans(Class<?> clazz) {
		
		if (this.tempBeans == null) {
			this.tempBeans = new ArrayList<ImportExcelTempBean>();
		}
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field field:fields) {
			
			if (!field.isAnnotationPresent(ImportExcelField.class))
				continue;
			
			this.tempBeans.add(ImportExcelTempBean.getBuilder()
					                                 .fieldName(field.getName())
					                                 .importExcelField(field.getAnnotation(ImportExcelField.class))
					                                 );
		}
		
		Collections.sort(this.tempBeans, new Comparator<ImportExcelTempBean>() {
		
			@Override
			public int compare(ImportExcelTempBean o1, ImportExcelTempBean o2) {
				return o1.getImportExcelField().order() - o2.getImportExcelField().order();
			}
			
		});
		
		return this;
	}
	
	public Map<String, ExcelImportConvertBase> getConvertMap() {
		return convertMap;
	}

	public List<ImportExcelTempBean> getTempBeans() {
		return tempBeans;
	}
}
