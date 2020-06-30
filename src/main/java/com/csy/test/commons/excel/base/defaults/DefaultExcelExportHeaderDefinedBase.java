package com.csy.test.commons.excel.base.defaults;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.base.ExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;
import com.csy.test.commons.excel.utils.CellStyleUtils;

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
		cellStyleMap.put(TITLE_STYLE_KEY, CellStyleUtils.createHeaderStyle(workbook, DefaultCommon.DEFAULTE_EXPORTXLS));
		return cellStyleMap;
	}
}
