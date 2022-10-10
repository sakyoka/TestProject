package com.csy.test.commons.excel.base.defaults.exportinitbase;

import java.lang.reflect.Field;
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;
import com.csy.test.commons.excel.base.ExcelExportInitBase;
import com.csy.test.commons.excel.base.defaults.DefaultCommon;

/**
 * 
 * 描述：默认字段格式化获取
 * @author csy
 * @date 2021年11月26日 上午9:55:53
 */
public class DefaultInitFormatImpl extends ExcelExportInitBase{
	
    private Map<String, ExcelExportFormatBase> xlsFormatTempMap;
	
	@SuppressWarnings("unused")
	private DefaultInitFormatImpl(){}
	
	public DefaultInitFormatImpl(Map<String, ExcelExportFormatBase> xlsFormatTempMap){
		this.xlsFormatTempMap = xlsFormatTempMap;
	}
	
	@Override
	protected boolean canContinue(Field field, String group) {
		return !xlsFormatTempMap.containsKey(field.getName());
	}

	@Override
	public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
		String fieldName = field.getName();
		xlsFormatTempMap.put(fieldName , DefaultCommon.DEFAULT_FORMAT_IMPL);
	}
}
