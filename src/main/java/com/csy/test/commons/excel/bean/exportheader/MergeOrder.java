package com.csy.test.commons.excel.bean.exportheader;

/**
 * 合并指令
 * @author csy
 * @date 2019年12月6日
 */
public class MergeOrder {
	
	private Integer colSpanStart = 0;//列合并开始
	
	private Integer colSpanEnd  = 0;//列合并结合
	
	private Integer rowSpanStart  = 0;//行合并开始
	
	private Integer rowSpanEnd  = 0;//行合并结束

	public Integer getColSpanStart() {
		return colSpanStart;
	}

	public void setColSpanStart(Integer colSpanStart) {
		this.colSpanStart = colSpanStart;
	}

	public Integer getColSpanEnd() {
		return colSpanEnd;
	}

	public void setColSpanEnd(Integer colSpanEnd) {
		this.colSpanEnd = colSpanEnd;
	}

	public Integer getRowSpanStart() {
		return rowSpanStart;
	}

	public void setRowSpanStart(Integer rowSpanStart) {
		this.rowSpanStart = rowSpanStart;
	}

	public Integer getRowSpanEnd() {
		return rowSpanEnd;
	}

	public void setRowSpanEnd(Integer rowSpanEnd) {
		this.rowSpanEnd = rowSpanEnd;
	}
	
	
}
