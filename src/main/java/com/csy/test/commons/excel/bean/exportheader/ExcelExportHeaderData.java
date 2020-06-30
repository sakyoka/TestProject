package com.csy.test.commons.excel.bean.exportheader;

import java.util.List;

public class ExcelExportHeaderData {
	
	private List<MergeOrder> mergeOrders;//合并指令集合
	
	private List<HeaderCell> headerCells;//单元格对象
	
	private List<HeaderField> headerFields;
	
	private Integer totalRow = 1;//总行数 , 默认1
	
	private Boolean needIndex = false;//是否需要index 默认false
	
	private String headerName;//表头名字

	public List<MergeOrder> getMergeOrders() {
		return mergeOrders;
	}

	public void setMergeOrders(List<MergeOrder> mergeOrders) {
		this.mergeOrders = mergeOrders;
	}

	public List<HeaderCell> getHeaderCells() {
		return headerCells;
	}

	public void setHeaderCells(List<HeaderCell> headerCells) {
		this.headerCells = headerCells;
	}

	public Integer getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(Integer totalRow) {
		this.totalRow = totalRow;
	}

	public List<HeaderField> getHeaderFields() {
		return headerFields;
	}

	public void setHeaderFields(List<HeaderField> headerFields) {
		this.headerFields = headerFields;
	}

	public Boolean getNeedIndex() {
		return needIndex;
	}

	public void setNeedIndex(Boolean needIndex) {
		this.needIndex = needIndex;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
}
