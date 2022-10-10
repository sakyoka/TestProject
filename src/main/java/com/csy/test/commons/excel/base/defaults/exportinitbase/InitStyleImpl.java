package com.csy.test.commons.excel.base.defaults.exportinitbase;

import java.lang.reflect.Field;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportInitBase;
import com.csy.test.commons.excel.utils.CellStyleUtils;

/**
 * 
 * 描述：初始化样式获取
 * @author csy
 * @date 2021年11月26日 上午9:55:53
 */
public class InitStyleImpl extends ExcelExportInitBase{
	
    private Map<String, CellStyle> xlsStyleTempMap;
    
    private Map<String, CellStyle> xlsHeaderStyleTempMap;
	
	@SuppressWarnings("unused")
	private InitStyleImpl(){}
	
	public InitStyleImpl(Map<String, CellStyle> xlsStyleTempMap, Map<String, CellStyle> xlsHeaderStyleTempMap){
		this.xlsStyleTempMap = xlsStyleTempMap;
		this.xlsHeaderStyleTempMap = xlsHeaderStyleTempMap;
	}
	
	@Override
	protected boolean canContinue(Field field, String group) {
		return this.defaultCanContinue(field, group);
	}

	@Override
	public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
        //每个字段对应的style实例
		String fieldName = field.getName();
        xlsStyleTempMap.put(fieldName , CellStyleUtils.createContentCellStyle(workbook, exportExcelField));
        xlsHeaderStyleTempMap.put(fieldName, CellStyleUtils.createHeaderStyle(workbook, exportExcelField));		
	}
}
