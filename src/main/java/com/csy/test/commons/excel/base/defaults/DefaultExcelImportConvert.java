package com.csy.test.commons.excel.base.defaults;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
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
			Object v = cell != null ? ExcelOperateBase.cellToData(cell) : null;
			field.set(entity, this.autoConvert(field, v));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ExcelImportFormatException("转换值失败 , fieldName ==> " + field.getName() , e);
		}
	}

	/**
	 * 描述：类型转换
	 * @author csy 
	 * @date 2020年10月13日 上午10:41:07
	 * @param field
	 * @param v
	 * @return Object
	 */
	public Object autoConvert(Field field , Object v){
		if (v == null || StringUtils.isBlank(v.toString())){
			return null;
		}
		
		if (Integer.class == field.getType()){
			return Integer.valueOf(v.toString());
		}
		
		if (Double.class == field.getType()){
			return Double.valueOf(v.toString());
		}
		
		if (Float.class == field.getType()){
			return Float.valueOf(v.toString());
		}
		//other
		
		//不处理的话 ， 默认都以String处理
		return v.toString();
	}
}
