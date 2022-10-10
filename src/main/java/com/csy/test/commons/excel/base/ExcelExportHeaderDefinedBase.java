package com.csy.test.commons.excel.base;


import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.bean.ExportExcelTempBean;
import com.csy.test.commons.excel.bean.Params;
import com.csy.test.commons.excel.bean.exportheader.ExcelExportHeaderData;
import com.csy.test.commons.excel.bean.exportheader.HeaderCell;
import com.csy.test.commons.excel.bean.exportheader.MergeOrder;
import com.csy.test.commons.excel.utils.CellStyleUtils;
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
	 * 描述：初始化头部信息(如果headerData 不为空调用initHeaderByHeaderData实例化，否者用initHeaderByClass)
	 * @author csy 
	 * @date 2020年7月2日 下午2:23:18
	 * @param sheetIndex 需要创建的sheet下标对象
	 * @param params 
	 * @return Sheet
	 */
	public <T> Sheet initHeader(int sheetIndex , Params<T> params){
		
		ExcelExportInitBaseContextHolder initBaseContextHolder = params.getInitBaseContextHolder();
		ExcelExportHeaderData headerData = params.getHeaderData();
		Workbook workbook = params.getWorkbook();
		Class<?> clazz = params.getClazz();
		return headerData == null ? 
				this.initHeaderByClass(workbook, clazz, sheetIndex, initBaseContextHolder) : 
			    this.initHeaderByHeaderData(workbook, clazz, sheetIndex, initBaseContextHolder, headerData);
	}
	
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
        
		boolean needHead = exportExcelHeader.needHead();
		int totalRow = headerData.getTotalRow() + (needHead ? 1 : 0);
		//初始化行
		for (int i = 0 ; i < totalRow ;i++){
			sheet.createRow(i);
		}
		
		int addColsNum = needHead ? 1 : 0; //因为需要表头原因，原本行数需要额外+1
		List<MergeOrder> mergeOrders = headerData.getMergeOrders();
		if (CollectionUtils.isNotEmpty(mergeOrders)){
			//执行合并行合并列命令
			for (MergeOrder mergeOrder:mergeOrders){
				sheet.addMergedRegion(
						new CellRangeAddress(mergeOrder.getRowSpanStart() + addColsNum, 
								mergeOrder.getRowSpanEnd() + addColsNum, 
								mergeOrder.getColSpanStart(), 
								mergeOrder.getColSpanEnd()));
			}
		}
		

		Map<String, CellStyle> extraCellStyleMap = this.addExtraCellStyle(workbook);
		Map<String, CellStyle> cellStyleMap = initBaseContextHolder.getHeaderStyleMap();
		if (extraCellStyleMap != null && extraCellStyleMap.size() > 0){
			cellStyleMap.putAll(extraCellStyleMap);
			initBaseContextHolder.getStyleMap().putAll(extraCellStyleMap);
		}
		
		Row row;
		Cell cell;
		List<HeaderCell> headerCells = headerData.getHeaderCells();
		if (CollectionUtils.isNotEmpty(headerCells)){
			//初始化每一个单元格
			String keyName = null;
			int maxCols = 0;
			for (HeaderCell headerCell:headerCells){
				
				row = sheet.getRow(headerCell.getRowNum() + addColsNum);//获取行对象
				row.setHeightInPoints(headerCell.getRowHeight());
				cell = row.createCell(headerCell.getColNum());//创建单元格
				cell.setCellValue(headerCell.getCellValue());//设置单元格值
				sheet.setColumnWidth( headerCell.getColNum(), headerCell.getCellWidth());
				
				keyName = headerCell.getKeyName();
				if (keyName != null && cellStyleMap.containsKey(keyName))//如果有样式
					cell.setCellStyle(cellStyleMap.get(keyName));//设置样式
				
				maxCols = (headerCell.getColNum() > maxCols) ? headerCell.getColNum() : maxCols;
			}
			
			if (needHead){
				this.createTitleColumn(exportExcelHeader, workbook, sheet, maxCols);
				//重新命名表头名字
				String headerName = headerData.getHeaderName();
				if (headerName != null){
					Row headerRow = sheet.getRow(0);
					headerRow.getCell(0).setCellValue(headerName);					
				}
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

        List<ExportExcelTempBean> exportXlsTempBeans = initBaseContextHolder.getExportExcelHeaderBeans();
        //获取所有列数
        int cells = exportXlsTempBeans.size() + (exportExcelHeader.needIndex() ? 1 : 0);
        //设置单元格大小
        for (int i = 0 ; i < cells ; i++){
            sheet.setColumnWidth(i , exportExcelHeader.cellWidth());
        }
        
        //创建大标题行
       //调整1、ExportExcelField有注解字段时候需要总个数-1 2、没有注解字段时候，给个默认长度7
        int titleSize = cells > 0 ? cells - 1 : 7;
        createTitleColumn(exportExcelHeader , workbook ,sheet , titleSize);

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
    private void createTitleColumn(ExportExcelHeader exportExcelHeader , Workbook workbook , Sheet sheet , int cols){
        //如果有第一行标题
        if (exportExcelHeader.needHead()){
            //创建第一行
            Row row0 = sheet.createRow(0);
            //第一行,合并列
            sheet.addMergedRegion(new CellRangeAddress(0 , 0 , 0 , cols));

            Cell cell0 = row0.createCell(0);
            row0.setHeightInPoints(exportExcelHeader.headerHeight());//设置大标题行高度
            cell0.setCellValue(exportExcelHeader.headerName());//设置大标题单元格值
            cell0.setCellStyle(CellStyleUtils.createTitleStyle(workbook, exportExcelHeader));

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
    private int createOrderNumberColumn(ExportExcelHeader exportExcelHeader , Workbook workbook , Row row){
        //如果有序号列
    	int startIndex = 0;
        if (exportExcelHeader.needIndex()){
            Cell cell = row.createCell(startIndex);
            cell.setCellValue(exportExcelHeader.indexName());//设置序号单元格标题
            cell.setCellStyle(CellStyleUtils.createOrderNumberStyle(workbook, exportExcelHeader));
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
    private void createTitleColumns(List<ExportExcelTempBean> exportExcelTempBeans , Workbook workbook , 
    		Sheet sheet , Row row , int startIndex){
    	
        ExportExcelField exportExcelField = null;
        ExportExcelTempBean exportExcelTempBean = null;
        for (int i = 0 , exportXlsTempBeansSize = exportExcelTempBeans.size(); i < exportXlsTempBeansSize ; i++){
        	exportExcelTempBean = exportExcelTempBeans.get(i);
            exportExcelField = exportExcelTempBean.getExportExcelField();
            Cell cell = row.createCell( i + startIndex );
            cell.setCellStyle( CellStyleUtils.createHeaderStyle(workbook, exportExcelField) );
            cell.setCellValue( exportExcelField.cellName() );//设置列标题
            sheet.setColumnWidth( (i + startIndex) , (exportExcelField.headerCellWidth() >= exportExcelField.cellWidth() ? 
            		                                                                           exportExcelField.headerCellWidth() : exportExcelField.cellWidth()));
        }	
    }
}
