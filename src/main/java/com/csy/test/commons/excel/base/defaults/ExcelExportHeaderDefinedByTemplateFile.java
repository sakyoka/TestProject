package com.csy.test.commons.excel.base.defaults;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.base.ExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.base.ExcelOperateBase;
import com.csy.test.commons.excel.bean.Params;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;
import com.csy.test.commons.excel.utils.SheetCopyUtils;

/**
 * 
 * 描述：template file 的头部信息生成
 * @author csy
 * @date 2021年11月23日 上午11:29:47
 */
public class ExcelExportHeaderDefinedByTemplateFile extends ExcelExportHeaderDefinedBase{

	private Sheet cacheOriginSheet;
	
	@Override
	public <T> Sheet initHeaderByHeaderData(Workbook workbook, Class<T> clazz, int sheetIndex,
			ExcelExportInitBaseContextHolder initBaseContextHolder, ExcelExportHeaderData headerData) {
		//不需要实现
		return null;
	}

	@Override
	public <T> Sheet initHeaderByClass(Workbook workbook, Class<T> clazz, int sheetIndex,
			ExcelExportInitBaseContextHolder initBaseContextHolder) {
		//不需要实现
		return null;
	}

	@Override
	protected Map<String, CellStyle> addExtraCellStyle(Workbook workbook) {
		return new HashMap<String, CellStyle>(0);
	}
	
	@Override
	public <T> Sheet initHeader(int sheetIndex, Params<T> params){
		
		Workbook workbook = params.getWorkbook();
		
		//第0个不需要生成
		if (sheetIndex == 0){
			//复制一份作为下一个sheet的头部模板
			//复制之后已经产生一个新的sheet
			this.cacheOriginSheet = workbook.cloneSheet(0);
			//所以要删除掉
			workbook.removeSheetAt(1);
			return workbook.getSheetAt(0);
		}
		
		Sheet sheet = ExcelOperateBase.createSheet(workbook, sheetIndex, params.getExportExcelHeader().sheetName());
		SheetCopyUtils.copy(cacheOriginSheet, sheet);
		return sheet;
	}
}
