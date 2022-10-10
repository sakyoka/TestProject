package com.csy.test.commons.excel.base.defaults.exportinitbase;

import java.lang.reflect.Field;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportInitBase;
import com.csy.test.commons.excel.base.defaults.DefaultCommon;
import com.csy.test.commons.excel.utils.CellStyleUtils;

/**
 * 
 * 描述：默认样式获取
 * @author csy
 * @date 2021年11月26日 下午5:41:54
 */
public class DefaultInitStyleImpl extends ExcelExportInitBase{
	
    private Map<String, CellStyle> xlsStyleTempMap;
    
    private Map<String, CellStyle> xlsHeaderStyleTempMap;
    
	@SuppressWarnings("unused")
	private DefaultInitStyleImpl(){}
	
	public DefaultInitStyleImpl(Map<String, CellStyle> xlsStyleTempMap, Map<String, CellStyle> xlsHeaderStyleTempMap){
		this.xlsStyleTempMap = xlsStyleTempMap;
		this.xlsHeaderStyleTempMap = xlsHeaderStyleTempMap;
	}
	
	@Override
	protected boolean canContinue(Field field, String group) {
		return !xlsStyleTempMap.containsKey(field.getName());
	}

	@Override
	public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
		String fieldName = field.getName();
		xlsStyleTempMap.put(fieldName, CellStyleUtils.createContentCellStyle(workbook, DefaultCommon.DEFAULTE_EXPORTXLS));
		xlsHeaderStyleTempMap.put(fieldName, CellStyleUtils.createHeaderStyle(workbook, DefaultCommon.DEFAULTE_EXPORTXLS));	
	}
}
