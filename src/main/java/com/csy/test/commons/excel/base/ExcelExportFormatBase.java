package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;


/**
 * 导出format基类
 * @author chensy
 * @Description
 * @date: 2019-12-02 11:03
 */
public abstract class ExcelExportFormatBase {

    /**
     * @Description //处理value
     * @Author csy
     * @Date 2019/12/2 11:14 
     * @Param [workbook, cell, field, value]
     * @return void
     **/
     protected abstract void formatValue(Workbook workbook , Cell cell , DataFormat dataFormat , Object value );

    /**
     * @Description //处理样式
     * @Author csy
     * @Date 2019/12/2 11:26
     * @Param [workbook, cell, exportExcelField]
     * @return org.apache.poi.ss.usermodel.CellStyle
     **/
    protected abstract CellStyle formatStyle(Workbook workbook , Cell cell, ExportExcelField exportExcelField);

    /**
     * @Description //默认调用 ， 重写覆盖这个方法
     * <br>如果不调用这个方法 , 可以单独调formatValueDefault、formatStyleDefault
     * @Author csy
     * @Date 2019/12/2 11:54
     * @Param [workbook, cell, dataFormat, field, value]
     * @return void
     **/
    public <T> void format(Workbook workbook , Cell cell , DataFormat dataFormat , T entity , Field field) throws IllegalAccessException {

        //格式化样式
    	ExportExcelField exportExcelField = field.getAnnotation(ExportExcelField.class);
        this.formatStyle(workbook , cell , exportExcelField);

        //格式化值
        try {
            field.setAccessible(true);
            Object value = field.get(entity);
            this.formatValue(workbook , cell , dataFormat , value);
        }finally {
            field.setAccessible(false);
        }

    }

    /**
     * @Description //默认格式化值
     * @Author csy
     * @Date 2019/12/2 12:46 
     * @Param [workbook, cell, dataFormat, value]
     * @return void
     **/
    public void formatValueDefault(Workbook workbook , Cell cell , DataFormat dataFormat , Object value ){
        //设置值
        if (value == null){
            return ;
        }

        //日期类型
        if (value instanceof Date){
            cell.setCellValue((Date)value);
            return ;
        }

        //字符串类型
        if (value instanceof String){
            CellStyle cellStyle = cell.getCellStyle();
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cellStyle.setDataFormat(dataFormat.getFormat("@"));
            cell.setCellValue((String) value);
            return ;
        }

        //布尔类型
        if(value instanceof  Boolean){
            cell.setCellValue((Boolean) value);
            return ;
        }

        //int类型
        if (value instanceof Integer){
            cell.setCellValue((Integer) value);
            return ;
        }

        //double类型
        if (value instanceof Double){
            cell.setCellValue((Double) value);
            return ;
        }

        throw new RuntimeException("未知数据类型");
    }

    /**
     * @Description //默认样式格式化
     * @Author csy
     * @Date 2019/12/2 11:54
     * @Param [workbook, cell, exportExcelField]
     * @return org.apache.poi.ss.usermodel.CellStyle
     **/
    public CellStyle formatStyleDefault(Workbook workbook , Cell cell, ExportExcelField exportExcelField){
         CellStyle cellStyle = cell.getCellStyle();
         return cellStyle;
    }

}
