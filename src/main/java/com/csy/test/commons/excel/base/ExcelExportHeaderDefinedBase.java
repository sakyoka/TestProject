package com.csy.test.commons.excel.base;


import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.bean.ExportExcelTempBean;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;
import com.csy.test.commons.excel.bean.exportheader.HeaderCell;
import com.csy.test.commons.excel.bean.exportheader.MergeOrder;
import com.csy.test.commons.excel.utils.CollectionUtils;


/**
 * 初始化头部
 * @author csy
 * @date 2019年12月19日
 */
public abstract class ExcelExportHeaderDefinedBase {
	
	/**
	 * 描述：初始化头部信息(根据自定义传来的header信息)
	 * @author csy 
	 * @date 2019年12月19日
	 * @param workbook
	 * @param clazz
	 * @param sheetIndex
	 * @param initBaseContextHolder
	 * @param headerData
	 * @return Sheet
	 */
	public abstract <T> Sheet initHeaderByHeaderData(Workbook workbook , Class<T> clazz , int sheetIndex , ExcelExportInitBaseContextHolder initBaseContextHolder , 
			ExcelExportHeaderData headerData);
	
	/**
	 * 描述：初始化头部信息(根据注解类信息)
	 * @author csy 
	 * @date 2019年12月19日
	 * @param workbook
	 * @param clazz
	 * @param sheetIndex
	 * @param initBaseContextHolder
	 * @return Sheet
	 */
	public abstract <T> Sheet initHeaderByClass(Workbook workbook , Class<T> clazz , int sheetIndex ,ExcelExportInitBaseContextHolder initBaseContextHolder);
	
	/**
	 * 描述：初始化头部信息(根据自定义传来的header信息)的额外样式设置
	 * @author csy 
	 * @date 2019年12月19日
	 * @param workbook
	 * @return Map<String,CellStyle>
	 */
	protected abstract Map<String, CellStyle> addExtraCellStyle(Workbook workbook);
	
	
	/**
	 * 描述：初始化头部信息(根据自定义传来的header信息)默认实现
	 * @author csy 
	 * @date 2019年12月19日
	 * @param workbook
	 * @param clazz
	 * @param sheetIndex
	 * @param initBaseContextHolder
	 * @param headerData
	 * @return Sheet
	 */
	protected <T> Sheet initHeaderByHeaderDataDefaul(Workbook workbook, Class<T> clazz, int sheetIndex,
			ExcelExportInitBaseContextHolder initBaseContextHolder , ExcelExportHeaderData headerData){
		
		ExportExcelHeader exportExcelHeader = clazz.getAnnotation(ExportExcelHeader.class);
        //创建工作簿，并命名
        Sheet sheet = ExcelOperateBase.createSheet(workbook, sheetIndex, exportExcelHeader.sheetName());
        
		int totalRow = headerData.getTotalRow();
		
		//初始化行
		for (int i = 0 ; i < totalRow ;i++){
			sheet.createRow(i);
		}
		
		List<MergeOrder> mergeOrders = headerData.getMergeOrders();
		if (CollectionUtils.isNotEmpty(mergeOrders)){
			//执行合并行合并列命令
			for (MergeOrder mergeOrder:mergeOrders){
				sheet.addMergedRegion(
						new CellRangeAddress(mergeOrder.getRowSpanStart(), 
								mergeOrder.getRowSpanEnd(), 
								mergeOrder.getColSpanStart(), 
								mergeOrder.getColSpanEnd()));
			}
		}
		

		Map<String, CellStyle> extraCellStyleMap = this.addExtraCellStyle(workbook);
		Map<String, CellStyle> cellStyleMap = initBaseContextHolder.getStyleMap();
		if (extraCellStyleMap != null && extraCellStyleMap.size() > 0)
			cellStyleMap.putAll(extraCellStyleMap);
		
		Row row;
		Cell cell;
		List<HeaderCell> headerCells = headerData.getHeaderCells();
		if (CollectionUtils.isNotEmpty(headerCells)){
			//初始化每一个单元格
			String keyName = null;
			for (HeaderCell headerCell:headerCells){
				
				row = sheet.getRow(headerCell.getRowNum());//获取行对象
				row.setHeightInPoints(headerCell.getRowHeight());
				cell = row.createCell(headerCell.getColNum());//创建单元格
				cell.setCellValue(headerCell.getCellValue());//设置单元格值
				sheet.setColumnWidth( headerCell.getColNum(), headerCell.getCellWidth());
				
				keyName = headerCell.getKeyName();
				if (keyName != null && cellStyleMap.containsKey(keyName))//如果有样式
					cell.setCellStyle(cellStyleMap.get(keyName));//设置样式
			}
		}
		
		return sheet;
	}
	
	/**
	 * 描述：初始化头部信息(根据注解类信息)默认实现
	 * @author csy 
	 * @date 2019年12月19日
	 * @param workbook
	 * @param clazz
	 * @param sheetIndex
	 * @param initBaseContextHolder
	 * @return Sheet
	 */
	protected <T> Sheet initHeaderByClassDefault(Workbook workbook, Class<T> clazz, int sheetIndex,
			ExcelExportInitBaseContextHolder initBaseContextHolder) {
		
		ExportExcelHeader exportExcelHeader = clazz.getAnnotation(ExportExcelHeader.class);
        //创建工作簿，并命名
        Sheet sheet = ExcelOperateBase.createSheet(workbook, sheetIndex, exportExcelHeader.sheetName());

        List<ExportExcelTempBean> exportXlsTempBeans = initBaseContextHolder.getExportExcelTempBeans();
        //获取所有列数
        int cells = exportXlsTempBeans.size() + (exportExcelHeader.needIndex() ? 1 : 0);
        //设置单元格大小
        for (int i = 0 ; i < cells ; i++){
            sheet.setColumnWidth(i , exportExcelHeader.cellWidth());
        }
        
        //创建大标题行
        createTitleColumn(exportExcelHeader , workbook ,sheet , cells);

        Row cellNameRow = sheet.createRow(exportExcelHeader.needHead() ? 1 : 0);
        cellNameRow.setHeightInPoints(exportExcelHeader.headerCellHeight());//设置列标题行高

        //创建序号单元格
        int startIndex = createOrderNumberColumn(exportExcelHeader, workbook, cellNameRow);

        //创建所有的列标题
        createTitleColumns(exportXlsTempBeans , workbook , sheet , cellNameRow , startIndex);
		
        return sheet;
	}
	
    /**
     * 描述：创建大标题
     * @author csy 
     * @date 2019年12月5日
     * @param exportExcelHeader
     * @param workbook
     * @param sheet
     * @param cols
     */
    private static void createTitleColumn(ExportExcelHeader exportExcelHeader , Workbook workbook , Sheet sheet , int cols){
        //如果有第一行标题
        if (exportExcelHeader.needHead()){
            //创建第一行
            Row row0 = sheet.createRow(0);
            //第一行,合并列
            sheet.addMergedRegion(new CellRangeAddress(0 , 0 , 0 , cols));

            Font font = workbook.createFont();
            font.setFontName(exportExcelHeader.fontName());//设置大标题字体类型
            font.setFontHeightInPoints(exportExcelHeader.headerNameFontSize());//设置大标题字体大小

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            cellStyle.setAlignment(exportExcelHeader.align());//设置大标题单元格对齐方式
            cellStyle.setVerticalAlignment(exportExcelHeader.verticalAlignment());//上下对齐方式

            Cell cell0 = row0.createCell(0);
            row0.setHeightInPoints(exportExcelHeader.headerHeight());//设置大标题行高度
            cell0.setCellValue(exportExcelHeader.headerName());//设置大标题单元格值
            cell0.setCellStyle(cellStyle);

        }
    }
    
    /**
     * 描述：创建序号列标题单元格
     * @author csy 
     * @date 2019年12月5日
     * @param exportExcelHeader
     * @param workbook
     * @param row
     * @return int
     */
    private static int createOrderNumberColumn(ExportExcelHeader exportExcelHeader , Workbook workbook , Row row){
        //如果有序号列
    	int startIndex = 0;
        if (exportExcelHeader.needIndex()){
            Cell cell = row.createCell(startIndex);
            CellStyle cellStyle = workbook.createCellStyle();

            Font font = workbook.createFont();
            font.setFontName( exportExcelHeader.indexFontName());//序号字体格式
            font.setBoldweight(exportExcelHeader.indexFontBoldweight());//粗体显示
            font.setFontHeightInPoints(exportExcelHeader.indexFontSize());//字体大小
            cellStyle.setAlignment( exportExcelHeader.indexAlign() );//序号左右对齐方式
            cellStyle.setVerticalAlignment(exportExcelHeader.indexVerticalAlignment());//序号上下对齐方式
            cellStyle.setFont(font);

            cell.setCellValue(exportExcelHeader.indexName());//设置序号单元格标题
            cell.setCellStyle(cellStyle);
            startIndex = 1;
        }
        return startIndex;
    }
    
    /**
     * 描述：创建列标题
     * @author csy 
     * @date 2019年12月5日
     * @param exportExcelTempBeans
     * @param workbook
     * @param sheet
     * @param row
     * @param startIndex 
     */
    private static void createTitleColumns(List<ExportExcelTempBean> exportExcelTempBeans , Workbook workbook , 
    		Sheet sheet , Row row , int startIndex){
    	
        ExportExcelField exportExcelField = null;
        CellStyle cellStyle = null;
        ExportExcelTempBean exportExcelTempBean = null;
        for (int i = 0 , exportXlsTempBeansSize = exportExcelTempBeans.size(); i < exportXlsTempBeansSize ; i++){
        	exportExcelTempBean = exportExcelTempBeans.get(i);
            exportExcelField = exportExcelTempBean.getExportExcelField();

            Cell cell = row.createCell( i + startIndex );
            cellStyle = workbook.createCellStyle();//获取style
            cell.setCellValue( exportExcelField.cellName() );//设置列标题

            Font font = workbook.createFont();
            font.setFontName( exportExcelField.headerFontName());//列标题单元格字体格式
            font.setBoldweight(exportExcelField.headerFontBoldweight());//粗体显示
            font.setFontHeightInPoints(exportExcelField.headerFontSize());//字体大小

            cellStyle.setAlignment( exportExcelField.hearderAlign() );//表标题单元格对齐方式
            cellStyle.setVerticalAlignment(exportExcelField.headerVerticalAlignment());
            cellStyle.setFont( font );
            cellStyle.setWrapText(exportExcelField.headerWarpText());
            cell.setCellStyle( cellStyle );

            sheet.setColumnWidth( (i + startIndex) , (exportExcelField.headerCellWidth() >= exportExcelField.cellWidth() ? exportExcelField.headerCellWidth() : exportExcelField.cellWidth()));
        }	
    }
}
