package com.csy.test.commons.excel.utils;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;

/**
 * 单元格功能类
 * @author csy
 * @date 2020年6月30日
 */
public class CellStyleUtils {
	
	private CellStyleUtils(){}
	
	/**
	 * 描述：创建列标题样式
	 * @author csy 
	 * @date 2020年6月30日
	 * @param workbook
	 * @param exportExcelField
	 * @return CellStyle
	 */
	public static CellStyle createHeaderStyle(Workbook workbook , ExportExcelField exportExcelField){
		CellStyle cellStyle = workbook.createCellStyle();//获取style
        Font font = workbook.createFont();
        font.setFontName( exportExcelField.headerFontName());//列标题单元格字体格式
        font.setBoldweight(exportExcelField.headerFontBoldweight());//粗体显示
        font.setFontHeightInPoints(exportExcelField.headerFontSize());//字体大小

        cellStyle.setAlignment( exportExcelField.hearderAlign() );//表标题单元格对齐方式
        cellStyle.setVerticalAlignment(exportExcelField.headerVerticalAlignment());
        cellStyle.setFont( font );
        cellStyle.setWrapText(exportExcelField.headerWarpText());
        return cellStyle;
	}
	
	/**
	 * 描述：创建内容单位格样式
	 * @author csy 
	 * @date 2020年6月30日
	 * @param workbook
	 * @param exportExcelField
	 * @return CellStyle
	 */
	public static CellStyle createContentCellStyle(Workbook workbook , ExportExcelField exportExcelField){
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName( exportExcelField.fontName());//列标题单元格字体格式
        font.setFontHeightInPoints(exportExcelField.fontSize());//字体大小
        
        cellStyle.setAlignment( exportExcelField.align() );//表标题单元格对齐方式
        cellStyle.setVerticalAlignment(exportExcelField.verticalAlignment());
        cellStyle.setFont( font );
        cellStyle.setWrapText(exportExcelField.warpText());
        return cellStyle;
	}
	
	/**
	 * 描述：创建行号标题样式
	 * @author csy 
	 * @date 2020年6月30日
	 * @param workbook
	 * @param exportExcelHeader
	 * @return CellStyle
	 */
	public static CellStyle createOrderNumberStyle(Workbook workbook , ExportExcelHeader exportExcelHeader){
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName( exportExcelHeader.indexFontName());//序号字体格式
        font.setBoldweight(exportExcelHeader.indexFontBoldweight());//粗体显示
        font.setFontHeightInPoints(exportExcelHeader.indexFontSize());//字体大小
        cellStyle.setAlignment( exportExcelHeader.indexAlign() );//序号左右对齐方式
        cellStyle.setVerticalAlignment(exportExcelHeader.indexVerticalAlignment());//序号上下对齐方式
        cellStyle.setFont(font);
        return cellStyle;
	}
	
	/**
	 * 描述：创建行号列内容样式
	 * @author csy 
	 * @date 2020年6月30日
	 * @param workbook
	 * @param exportExcelHeader
	 * @return CellStyle
	 */
	public static CellStyle createOrderNumberContentCellStyle(Workbook workbook , ExportExcelHeader exportExcelHeader){
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        cellStyle.setAlignment(exportExcelHeader.indexAlign());
        cellStyle.setVerticalAlignment(exportExcelHeader.indexVerticalAlignment());
        font.setFontName(exportExcelHeader.indexFontName());
        //font.setBoldweight(exportExcelHeader.indexFontBoldweight());
        cellStyle.setFont(font);
        return cellStyle;
	}
	
	/**
	 * 描述：创建大标题样式
	 * @author csy 
	 * @date 2020年6月30日
	 * @param workbook
	 * @param exportExcelHeader
	 * @return CellStyle
	 */
	public static CellStyle createTitleStyle(Workbook workbook , ExportExcelHeader exportExcelHeader){
        Font font = workbook.createFont();
        font.setFontName(exportExcelHeader.fontName());//设置大标题字体类型
        font.setFontHeightInPoints(exportExcelHeader.headerNameFontSize());//设置大标题字体大小

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(exportExcelHeader.align());//设置大标题单元格对齐方式
        cellStyle.setVerticalAlignment(exportExcelHeader.verticalAlignment());//上下对齐方式
        return cellStyle;
	}
}
