package com.csy.test.commons.excel.bean;

import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：导出配置参数
 * @author csy
 * @date 2022年4月18日 下午1:43:47
 */
public class ExportConfig {

	/**
	 * 是否自动多线程处理数据：默认false否
	 * <br>建议：
	 * <br>1、数据量少时候不建议开启
	 * <br>注：因为setCellValue不支持同步，在多线程下，容易造成下标越界问题；
	 *         需要添加锁，如在format里添加锁，但是存在细粒度问题，待处理
	 */
	private boolean autoMultiThreading = false;
	
	/**
	 * 一个线程负责的sheet个数，仅当autoMultiThreading 为true时候生效
	 * <br>默认一个线程负责2个sheet数据
	 */
	private int oneThreadDisposeSheetNumber = 2;
	
	/**
	 * 是否自动移除元素
	 * <br>建议：
	 * <br>1、如果数据量大，建议开启，进行元素移除，减少导出时候内存消耗，
	 *        但同时会增加分割集合时间和一定内存(cutListByLengthOfNew造成)
	 * <br>2、subList分割集合最优但不适合移除元素，存在异常
	 * <br>注：
	 * <br>1、开启时候集合分割选取，如果小于最大值MAX_ROW（即只有一个sheet）;
	 *        可以用cutListByLength分割（不需要增加额外内存和几乎不需要时间）
	 * <br>2、如果大于MAX_ROW，用cutListByLengthOfNew分割（需要消耗一定时间和内存）
	 */
	private boolean autoRemoveElement = false;
	
	
	/**
	 *  一个sheet最大行数（推荐此参数）
	 *  <br>默认值：0 ，此时会直接读取BeanRowUtils.MAX_ROW 值
	 *  <br>注：1、此值的大小关系到sheet个数，值越大sheet个数越小。
	 *  <br>    2、对于xls类型最大值能不超过65536（需要减去表头）
	 *  <br>    3、越小时候，响应更快，但是sheet个数多
	 */
	private int oneSheetMaxRows = 0;
	
	/**
	 * 自动设置sheet、row、cell阈值
	 * <br>注：此配置可以减低xls类型 workbook里面的扩容次数
	 */
	private boolean autoInitExcelCapacity = false;

	public boolean isAutoMultiThreading() {
		return autoMultiThreading;
	}

	public void setAutoMultiThreading(boolean autoMultiThreading) {
		this.autoMultiThreading = autoMultiThreading;
	}

	public boolean isAutoRemoveElement() {
		return autoRemoveElement;
	}

	public void setAutoRemoveElement(boolean autoRemoveElement) {
		this.autoRemoveElement = autoRemoveElement;
	}

	public int getOneThreadDisposeSheetNumber() {
		return oneThreadDisposeSheetNumber;
	}

	public void setOneThreadDisposeSheetNumber(int oneThreadDisposeSheetNumber) {
		Objects.isConditionAssert(oneThreadDisposeSheetNumber >= 1, RuntimeException.class,
				"oneThreadDisposeSheetNumber要大于等于1");
		this.oneThreadDisposeSheetNumber = oneThreadDisposeSheetNumber;
	}

	public int getOneSheetMaxRows() {
		return oneSheetMaxRows;
	}

	public void setOneSheetMaxRows(int oneSheetMaxRows) {
		this.oneSheetMaxRows = oneSheetMaxRows;
	}

	public boolean isAutoInitExcelCapacity() {
		return autoInitExcelCapacity;
	}

	public void setAutoInitExcelCapacity(boolean autoInitExcelCapacity) {
		this.autoInitExcelCapacity = autoInitExcelCapacity;
	}
}
