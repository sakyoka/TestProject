package com.csy.test.commons.excel.bean;

import com.csy.test.commons.excel.annotion.ExportExcelField;

/**
 * 临时对象
 * @author csy
 * @date 2019年12月19日
 */
public class ExportExcelTempBean {
	
    private String fieldName;

    private ExportExcelField exportExcelField;

    public ExportExcelTempBean fieldName(String fieldName){
        this.fieldName = fieldName;
        return this;
    }
    
    public ExportExcelTempBean exportExcelField(ExportExcelField exportExcelField){
        this.exportExcelField = exportExcelField;
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public ExportExcelField getExportExcelField() {
        return exportExcelField;
    }
}
