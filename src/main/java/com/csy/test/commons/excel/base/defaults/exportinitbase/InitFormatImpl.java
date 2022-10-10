package com.csy.test.commons.excel.base.defaults.exportinitbase;

import java.lang.reflect.Field;
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;
import com.csy.test.commons.excel.base.ExcelExportInitBase;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 
 * 描述：字段格式化获取
 * @author csy
 * @date 2021年11月26日 上午9:55:53
 */
public class InitFormatImpl extends ExcelExportInitBase{
	
    private Map<String, ExcelExportFormatBase> xlsFormatTempMap;
	
	@SuppressWarnings("unused")
	private InitFormatImpl(){}
	
	public InitFormatImpl(Map<String, ExcelExportFormatBase> xlsFormatTempMap){
		this.xlsFormatTempMap = xlsFormatTempMap;
	}
	
	@Override
	protected boolean canContinue(Field field, String group) {
		return this.defaultCanContinue(field, group);
	}

	@Override
	public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
		String fieldName = field.getName();
		try {
			xlsFormatTempMap.put(fieldName, ClassUtils.newInstance(exportExcelField.formatClass()));
		} catch (Exception e) {
			throw new RuntimeException("实例化format对象失败!" , e);
		}	
	}
}
