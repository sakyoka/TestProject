package com.csy.test.commons.excel.base.defaults;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.base.ExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;

/**
 * 默认实现类
 * @author csy
 * @date 2019年12月9日
 */
public class DefaultExcelExportHeaderDefinedBase extends ExcelExportHeaderDefinedBase{
	
	public static final String TITLE_STYLE_KEY = "defaultCellStyle";
	
	@Override
	public <T> Sheet initHeaderByHeaderData(Workbook workbook, Class<T> clazz, int sheetIndex,
			ExcelExportInitBaseContextHolder initBaseContextHolder , ExcelExportHeaderData headerData){
		return super.initHeaderByHeaderDataDefaul(workbook, clazz, sheetIndex, initBaseContextHolder, headerData);
	}

	@Override
	public <T> Sheet initHeaderByClass(Workbook workbook, Class<T> clazz, int sheetIndex,
			ExcelExportInitBaseContextHolder initBaseContextHolder) {
		return super.initHeaderByClassDefault(workbook, clazz, sheetIndex, initBaseContextHolder);
	}

	@Override
	protected Map<String, CellStyle> addExtraCellStyle(Workbook workbook) {
		Map<String, CellStyle> cellStyleMap = new HashMap<String, CellStyle>();
		CellStyle cellStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("宋体");//字体类型:宋体
		font.setFontHeightInPoints((short)12);//字体大小：12
        font.setBoldweight((short) 0x2bc);//字体粗细
        cellStyle.setAlignment((short)2);//水平对齐情况:居中
        cellStyle.setVerticalAlignment((short) 1);//上下对齐情况:居中
        cellStyle.setFont(font );
        cellStyle.setWrapText(true);//是否自动换行:是
		cellStyleMap.put(TITLE_STYLE_KEY, cellStyle);
		return cellStyleMap;
	}
}
