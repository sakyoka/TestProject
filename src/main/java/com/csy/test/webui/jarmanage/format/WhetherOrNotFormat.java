package com.csy.test.webui.jarmanage.format;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;

/**
 * 
 * 描述：是否格式化
 * @author csy
 * @date 2022年9月11日 下午9:53:03
 */
public class WhetherOrNotFormat extends ExcelExportFormatBase{

	private static final Integer YES = 1;
	
	@Override
	protected void formatValue(Workbook workbook, Cell cell, DataFormat dataFormat, Object value) {
        if ( value == null) {
            return ;
        }

        cell.setCellValue(YES.equals(value) ? "是" : "否");
	}

	@Override
	protected CellStyle formatStyle(Workbook workbook, Cell cell, ExportExcelField exportExcelField) {
		return super.formatStyleDefault(workbook , cell , exportExcelField);
	}

}
