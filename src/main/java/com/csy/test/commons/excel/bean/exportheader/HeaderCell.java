package com.csy.test.commons.excel.bean.exportheader;

public class HeaderCell {
	
	private Integer rowNum;//第几行
	
	private Integer colNum;//第几列
	
	private String keyName;//标识。暂时用处，根据标识获取样式对象
	
	private String cellValue;//单元格值
	
	private Integer rowHeight = 30;
	
	private Integer cellWidth = 50 * 100;

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}

	public String getCellValue() {
		return cellValue;
	}

	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public Integer getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(Integer rowHeight) {
		this.rowHeight = rowHeight;
	}

	public Integer getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(Integer cellWidth) {
		this.cellWidth = cellWidth;
	}
	
	
}
