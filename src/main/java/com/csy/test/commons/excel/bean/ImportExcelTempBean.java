package com.csy.test.commons.excel.bean;

import com.csy.test.commons.excel.annotion.ImportExcelField;

public class ImportExcelTempBean {
	
	private String fieldName;
	
	private ImportExcelField importExcelField;
	
	public static ImportExcelTempBean getBuilder() {
		return new ImportExcelTempBean();
	}
	
	public ImportExcelTempBean fieldName(String fieldName) {
		this.fieldName = fieldName;
		return this;
	}
	
	public ImportExcelTempBean importExcelField(ImportExcelField importExcelField) {
		this.importExcelField = importExcelField;
		return this;
	}	

	public String getFieldName() {
		return fieldName;
	}

	public ImportExcelField getImportExcelField() {
		return importExcelField;
	}
}
