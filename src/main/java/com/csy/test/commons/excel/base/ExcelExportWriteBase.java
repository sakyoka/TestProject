package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.csy.test.commons.excel.bean.Params;

/**
 * 
 * 描述：cell创建、数据写入
 * @author csy
 * @date 2022年4月22日 下午2:17:58
 */
public interface ExcelExportWriteBase {

	/**
	 * 描述：row根据cellIndex创建cell ，并且设置值
	 */
	<T> Cell write(Params<T> params, ExcelExportInitBaseContextHolder initBaseContextHolder, String fieldName, 
			Row row, int cellIndex, Object entity, Field field) throws IllegalAccessException;
}
