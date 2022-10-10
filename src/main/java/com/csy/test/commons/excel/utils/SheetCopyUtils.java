package com.csy.test.commons.excel.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：sheet复制
 * @author csy
 * @date 2021年11月23日 下午2:23:30
 */
public class SheetCopyUtils {
	
	private SheetCopyUtils(){}
	
	private static final int END_ROWS = -1;
	
	/**
	 * 描述：sheet复制到另外一个sheet
	 * @author csy 
	 * @date 2021年11月23日 下午2:24:06
	 * @param orgin  数据源对象
	 * @param target 目标对象
	 * @param start  开始行
	 * @param end    结束行
	 */
	public static void copy(Sheet orgin, Sheet target, int startRows, int endRows){
		
		int useRows = orgin.getPhysicalNumberOfRows();
		
		Objects.isConditionAssert(useRows >= endRows, RuntimeException.class, "max endRows not allow");
		
		if (END_ROWS == endRows){
			endRows = useRows;
		}
		
		if (startRows > 0 && startRows >= endRows){
			throw new RuntimeException("不合理的开始行数startRows:" + startRows + ",结束行数:" + endRows);
		}
		
		//合并单元格
		mergerRegion(orgin, target);
		
		//设置值、样式
		setCellAndStyle(orgin, target, startRows, endRows);
	}
	
	/**
	 * 描述：sheet复制到另外一个sheet
	 * @author csy 
	 * @date 2021年11月23日 下午2:20:30
	 * @param orgin
	 * @param target
	 */
	public static void copy(Sheet orgin, Sheet target){
		copy(orgin, target, 0, END_ROWS);
	}
	
	/**
	 * 描述：设置值和样式
	 * @author csy 
	 * @date 2021年11月23日 下午6:33:23
	 * @param orgin
	 * @param target
	 * @param startRows
	 * @param endRows
	 */
	private static void setCellAndStyle(Sheet orgin, Sheet target, int startRows, int endRows){
		
		for (int rowNum = startRows; rowNum < endRows; rowNum++){
			Row originRow = orgin.getRow(rowNum);
			Row targetRow = target.createRow(rowNum);
			
			setRowStyle(originRow, targetRow);
			
			int cells = originRow.getPhysicalNumberOfCells();
			for (int cellNum = 0; cellNum < cells; cellNum++){
				Cell originCell = originRow.getCell(cellNum);
				Cell targetCell = targetRow.createCell(cellNum);
				setCellValue(originCell, targetCell);
				targetCell.setCellStyle(originCell.getCellStyle());
				
				setColumnStyle(cellNum, orgin, target);
			}
		}		
	}
	
	/**
	 * 描述：设置columnStyle
	 * @author csy 
	 * @date 2021年11月24日 上午10:23:15
	 * @param orgin
	 * @param target
	 */
	private static void setColumnStyle(int index, Sheet orgin, Sheet target) {
		
		target.setColumnWidth(index, orgin.getColumnWidth(index));
		CellStyle cellStyle = orgin.getColumnStyle(index);
		if (Objects.notNull(cellStyle))
			target.setDefaultColumnStyle(index, cellStyle);
		
		//other operate
	}

	/**
	 * 描述：设置行样式
	 * @author csy 
	 * @date 2021年11月24日 上午10:14:31
	 * @param originRow
	 * @param targetRow
	 */
	private static void setRowStyle(Row originRow, Row targetRow) {
		targetRow.setHeight(originRow.getHeight());
		targetRow.setHeightInPoints(targetRow.getHeightInPoints());
		
		if (Objects.notNull(targetRow.getRowStyle()))
			targetRow.setRowStyle(targetRow.getRowStyle());
		
		targetRow.setZeroHeight(targetRow.getZeroHeight());
		
		//other operate 
	}

	/**
	 * 描述：设置值
	 * @author csy 
	 * @date 2021年11月23日 下午6:25:49
	 * @param originCell
	 * @param targetCell
	 */
	private static void setCellValue(Cell originCell, Cell targetCell){
//		int type = originCell.getCellType();
//	     * @see Cell#CELL_TYPE_BLANK
//	     * @see Cell#CELL_TYPE_NUMERIC
//	     * @see Cell#CELL_TYPE_STRING
//	     * @see Cell#CELL_TYPE_FORMULA
//	     * @see Cell#CELL_TYPE_BOOLEAN
//	     * @see Cell#CELL_TYPE_ERROR
		targetCell.setCellValue(originCell.getStringCellValue());
		//other operate
	}
	
	/**
	 * 描述：复制合并单元格
	 * @author csy 
	 * @date 2021年11月23日 下午3:00:52
	 * @param orgin
	 * @param target
	 */
	private static void mergerRegion(Sheet orgin, Sheet target) {
		int sheetMergerCount = orgin.getNumMergedRegions();
		for (int i = 0; i < sheetMergerCount; i++) {
			target.addMergedRegion(orgin.getMergedRegion(i));
		}
	}
}
