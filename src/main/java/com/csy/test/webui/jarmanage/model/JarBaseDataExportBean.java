package com.csy.test.webui.jarmanage.model;

import java.util.Date;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.base.defaults.ExcelExportTimeFormat;
import com.csy.test.webui.jarmanage.format.WhetherOrNotFormat;

import lombok.Data;

/**
 * 
 * 描述：jar数据
 * @author csy
 * @date 2022年9月11日 下午9:43:42
 */
@Data
@ExportExcelHeader
public class JarBaseDataExportBean {

	@ExportExcelField(order = 1, cellName = "jarId", warpText = true, cellWidth = 375 * 30)
	private String jarId;
	
	@ExportExcelField(order = 2, cellName = "进程ID")
	private String pId;

	@ExportExcelField(order = 3, cellName = "jar中文名字", cellWidth = 375 * 20)
	private String jarChName;

	@ExportExcelField(order = 4, cellName = "jar英文名字", cellWidth = 375 * 20)
	private String jarEnName;
	
	@ExportExcelField(order = 5, cellName = "jar描述", warpText = true, cellWidth = 375 * 20)
	private String jarDesc;
	
	@ExportExcelField(order = 6, cellName = "日志路径", warpText = true, cellWidth = 375 * 30)
	private String logPath;
	
	@ExportExcelField(order = 7, cellName = "jar包保存相对路径", warpText = true, cellWidth = 375 * 30)
	private String jarPath;
	
	@ExportExcelField(order = 8, cellName = "是否运行正常", formatClass = WhetherOrNotFormat.class)
	private Integer isAlive;
	
	@ExportExcelField(order = 9, cellName = "是否启动", formatClass = WhetherOrNotFormat.class)
	private Integer isRuning;

	@ExportExcelField(order = 10, cellName = "文件uuid目录", warpText = true, cellWidth = 375 * 30)
	private String dirUuid;
	
	@ExportExcelField(order = 11, cellName = "排序号")
	private Integer orderNumber;

	@ExportExcelField(order = 12, cellName = "创建日期", formatClass = ExcelExportTimeFormat.class, cellWidth = 375 * 20)
	private Date createTime;

	@ExportExcelField(order = 13, cellName = "修改日期", formatClass = ExcelExportTimeFormat.class, cellWidth = 375 * 20)
	private Date updateTime;

	@ExportExcelField(order = 14, cellName = "是否删除", formatClass = WhetherOrNotFormat.class)
	private Integer deleteFlag;

	@ExportExcelField(order = 15, cellName = "包后缀")
	private String sufixName;
}
