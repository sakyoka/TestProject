package com.csy.test.commons.excel.base.defaults;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Cell;

import com.csy.test.commons.excel.base.ExcelImportConvertBase;
import com.csy.test.commons.excel.base.ExcelOperateBase;
import com.csy.test.commons.excel.exception.ExcelImportFormatException;

/**
 * 
 * 描述：默认转换器
 * @author csy
 * @date 2020年7月1日 下午6:11:04
 */
public class DefaultExcelImportConvert implements ExcelImportConvertBase{

	@Override
	public <T> void convert(T entity, Field field, Cell cell) {
		if (cell == null)
			return ;
		
		try {
			Object v = ExcelOperateBase.cellToData(cell);
			field.set(entity, v);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ExcelImportFormatException("转换值失败 , fieldName ==> " + field.getName() , e);
		}
	}

}
