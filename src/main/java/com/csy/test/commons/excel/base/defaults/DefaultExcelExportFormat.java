package com.csy.test.commons.excel.base.defaults;

import org.apache.poi.ss.usermodel.*;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;

/**
 * 默认公共格式处理器
 * @author chensy
 * @Description
 * @date: 2019-12-02 11:09
 */
public class DefaultExcelExportFormat extends ExcelExportFormatBase{

    @Override
    protected void formatValue(Workbook workbook, Cell cell, DataFormat dataFormat , Object value) {
        super.formatValueDefault(workbook , cell , dataFormat , value);
    }

    @Override
    protected CellStyle formatStyle(Workbook workbook, Cell cell, ExportExcelField exportExcelField) {
        return super.formatStyleDefault(workbook , cell , exportExcelField);
    }
}
