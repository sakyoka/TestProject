package com.csy.test.commons.excel.bean;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;

/**
 * 参数对象
 * @author csy
 * @date 2020年1月6日
 * @param <T>
 */
public class Params<T> {

	private Workbook workbook;
	
	private Sheet sheet;
	
	private List<T> beanList;
	
	private int useRow;
	
	private ExportExcelHeader exportExcelHeader;
	
	private ExcelExportInitBaseContextHolder initBaseContextHolder;
	
	private boolean needIndex;
	
	private CellStyle cellStyle;
	
	@SuppressWarnings("rawtypes")
	public static <T> Params getBuilder(){
		return new Params<T>();
	}

	public Params<T> workbook(Workbook workbook) {
		this.workbook = workbook;
		return this;
	}

	public Params<T> sheet(Sheet sheet) {
		this.sheet = sheet;
		return this;
	}

	public Params<T> beanList(List<T> subList) {
		this.beanList = subList;
		return this;
	}

	public Params<T> useRow(int useRow) {
		this.useRow = useRow;
		return this;
	}

	public Params<T> exportExcelHeader(ExportExcelHeader exportExcelHeader) {
		this.exportExcelHeader = exportExcelHeader;
		return this;
	}

	public Params<T> initBaseContextHolder(ExcelExportInitBaseContextHolder initBaseContextHolder) {
		this.initBaseContextHolder = initBaseContextHolder;
		return this;
	}

	public Params<T> needIndex(boolean needIndex) {
		this.needIndex = needIndex;
		return this;
	}

	public Params<T> cellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
		return this;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public List<T> getBeanList() {
		return beanList;
	}

	public int getUseRow() {
		return useRow;
	}

	public ExportExcelHeader getExportExcelHeader() {
		return exportExcelHeader;
	}

	public ExcelExportInitBaseContextHolder getInitBaseContextHolder() {
		return initBaseContextHolder;
	}

	public boolean getNeedIndex() {
		return needIndex;
	}

	public CellStyle getCellStyle() {
		return cellStyle;
	}
}
