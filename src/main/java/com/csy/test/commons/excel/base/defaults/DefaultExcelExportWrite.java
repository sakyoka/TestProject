package com.csy.test.commons.excel.base.defaults;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;

import com.csy.test.commons.excel.base.ExcelExportFormatBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.excel.base.ExcelExportWriteBase;
import com.csy.test.commons.excel.bean.Params;

/**
 * 
 * 描述：普通cell创建、数据写入
 * @author csy
 * @date 2022年4月22日 下午2:18:36
 */
public class DefaultExcelExportWrite implements ExcelExportWriteBase{
	
	/**
	 * 没有加同步，多线程操作sheet时候会存在异常（单线程使用没有问题）
	 */
	public static final ExcelExportWriteBase UN_SAFE_CREATE_AND_FILL = new DefaultExcelExportWrite();

	@Override
	public <T> Cell write(Params<T> params, ExcelExportInitBaseContextHolder initBaseContextHolder, 
			String fieldName, Row row, int cellIndex, Object entity, Field field) throws IllegalAccessException {
        //创建单元格 
        Cell cell = row.createCell(cellIndex);
        Map<String, CellStyle> temStyleMap = initBaseContextHolder.getStyleMap();
        
        //根据fieldName获取style实例
        cell.setCellStyle(temStyleMap.get(fieldName));
        
        //根据fieldName获取format实例
        Map<String, ExcelExportFormatBase> temImplMap = initBaseContextHolder.getFormatMap();
        temImplMap.get(fieldName).format(params.getWorkbook() , cell , params.getDataFormat() , entity , field);
        return cell;
	}
}