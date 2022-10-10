package com.csy.test.commons.excel.bean;

import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.base.ExcelExportHeaderDefinedBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.base.ExcelExportWriteBase;
import com.csy.test.commons.excel.base.defaults.DefaultExcelExportWrite;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;

/**
 * 参数对象
 * @author csy
 * @date 2020年1月6日
 * @param <T>
 */
public class Params<T>{

	private Workbook workbook;
	
	private Sheet sheet;
	
	private List<T> beanList;
	
	private List<T> subList;
	
	private int useRow;
	
	private ExportExcelHeader exportExcelHeader;
	
	private ExcelExportInitBaseContextHolder initBaseContextHolder;
	
	private boolean needIndex;
	
	private CellStyle cellStyle;
	
	private Class<? extends ExcelExportHeaderDefinedBase> headerDefinedBaseClazz;
	
	private ExcelExportHeaderData headerData;
	
	private String xlsType; 
	
	private Class<T> clazz;
	
	private ExportConfig exportConfig;
	
	private ExcelExportWriteBase excelExportWriteBase;
	
	private DataFormat dataFormat;

	@SuppressWarnings("rawtypes")
	public static <T> Params getBuilder(){
		return new Params<T>();
	}
	
	public Params(){
		
		this.exportConfig = new ExportConfig();
		
		this.excelExportWriteBase = DefaultExcelExportWrite.UN_SAFE_CREATE_AND_FILL;
	}
	
	public Params<T> dataFormat(DataFormat dataFormat) {
		this.dataFormat = dataFormat;
		return this;
	}
	
	public Params<T> excelExportWriteBase(ExcelExportWriteBase excelExportWriteBase) {
		this.excelExportWriteBase = excelExportWriteBase;
		return this;
	}
	
	public Params<T> exportConfig(ExportConfig exportConfig) {
		this.exportConfig = exportConfig;
		return this;
	}

	public Params<T> headerDefinedBaseClazz(Class<? extends ExcelExportHeaderDefinedBase> clazz) {
		this.headerDefinedBaseClazz = clazz;
		return this;
	}
	
	public Params<T> headerData(ExcelExportHeaderData headerData) {
		this.headerData = headerData;
		return this;
	}
	
	public Params<T> xlsType(String xlsType) {
		this.xlsType = xlsType;
		return this;
	}
	
	public Params<T> clazz(Class<T> clazz) {
		this.clazz = clazz;
		return this;
	}

	public Params<T> workbook(Workbook workbook) {
		this.workbook = workbook;
		return this;
	}

	public Params<T> sheet(Sheet sheet) {
		this.sheet = sheet;
		return this;
	}

	public Params<T> beanList(List<T> beanList) {
		this.beanList = beanList;
		return this;
	}
	
	public Params<T> subList(List<T> subList) {
		this.subList = subList;
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

	public Class<? extends ExcelExportHeaderDefinedBase> getHeaderDefinedBaseClazz() {
		return headerDefinedBaseClazz;
	}

	public ExcelExportHeaderData getHeaderData() {
		return headerData;
	}

	public String getXlsType() {
		return xlsType;
	}

	public Class<T> getClazz() {
		return clazz;
	}

	public List<T> getSubList() {
		return subList;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}

	public void setSubList(List<T> subList) {
		this.subList = subList;
	}

	public void setUseRow(int useRow) {
		this.useRow = useRow;
	}

	public void setExportExcelHeader(ExportExcelHeader exportExcelHeader) {
		this.exportExcelHeader = exportExcelHeader;
	}

	public void setInitBaseContextHolder(ExcelExportInitBaseContextHolder initBaseContextHolder) {
		this.initBaseContextHolder = initBaseContextHolder;
	}

	public void setNeedIndex(boolean needIndex) {
		this.needIndex = needIndex;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	public void setHeaderDefinedBaseClazz(Class<? extends ExcelExportHeaderDefinedBase> headerDefinedBaseClazz) {
		this.headerDefinedBaseClazz = headerDefinedBaseClazz;
	}

	public void setHeaderData(ExcelExportHeaderData headerData) {
		this.headerData = headerData;
	}

	public void setXlsType(String xlsType) {
		this.xlsType = xlsType;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public ExportConfig getExportConfig() {
		return exportConfig;
	}

	public void setExportConfig(ExportConfig exportConfig) {
		this.exportConfig = exportConfig;
	}

	public DataFormat getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(DataFormat dataFormat) {
		this.dataFormat = dataFormat;
	}

	public ExcelExportWriteBase getExcelExportWriteBase() {
		return excelExportWriteBase;
	}

	public void setExcelExportWriteBase(ExcelExportWriteBase excelExportWriteBase) {
		this.excelExportWriteBase = excelExportWriteBase;
	}
}
