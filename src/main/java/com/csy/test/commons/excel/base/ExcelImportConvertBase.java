package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 
 * 描述：导出字段格式执行器
 * @author csy
 * @date 2020年7月1日 下午5:08:49
 */
public interface ExcelImportConvertBase {
	
	<T> void convert(T entity , Field field , Cell cell);
}
