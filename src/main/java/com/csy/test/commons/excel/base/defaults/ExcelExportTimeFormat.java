package com.csy.test.commons.excel.base.defaults;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;

import java.util.Date;

/**
 * time格式化
 * @author chensy
 * @Description
 * @date: 2019-12-02 11:41
 */
public class ExcelExportTimeFormat extends ExcelExportFormatBase{

    @Override
    protected void formatValue(Workbook workbook, Cell cell, DataFormat dataFormat , Object value) {

        if ( value == null)
            return ;

        cell.setCellValue(DateFormatUtils.format((Date)value , "yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    protected CellStyle formatStyle(Workbook workbook, Cell cell, ExportExcelField exportExcelField) {
        return super.formatStyleDefault(workbook , cell , exportExcelField);
    }
}
