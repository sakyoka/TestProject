package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;
import com.csy.test.commons.excel.base.ExcelExportInitBase;
import com.csy.test.commons.excel.base.defaults.DefaultCommon;
import com.csy.test.commons.excel.bean.ExportExcelTempBean;
import com.csy.test.commons.excel.bean.exportheader.HeaderField;

/**
 * 初始化实现
 * @author csy
 * @date 2019年12月19日
 */
public class ExcelExportInitBaseContextHolder {
	
    private Map<String, ExcelExportFormatBase> formatMap;//key为实体类对象的属性，对应的value为字段的format实例

    private Map<String, CellStyle> styleMap;//key为实体类对象的属性，对应的value为字段的CellStyle实例

    private List<ExportExcelTempBean> exportExcelTempBeans;//该集合为每个字段对应的ExportXls
    
    private ExcelExportInitBaseContextHolder(){}
    
    /**
     * 描述：获取ExcelExportInitBaseContextHolder实例
     * @author csy 
     * @date 2019年12月19日
     * @return InitBaseContextHolder
     */
    public static ExcelExportInitBaseContextHolder getInstance(){
    	return new ExcelExportInitBaseContextHolder();
    }

    /**
     * 描述：初始化样式
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param workbook
     * @param group void
     */
	public <T> ExcelExportInitBaseContextHolder initStyle(Class<T> clazz , Workbook workbook , String group) {
    	
		if (this.styleMap == null)
    		this.styleMap = new HashMap<String, CellStyle>();
    	
        final Map<String, CellStyle> xlsStyleTempMap = new HashMap<String, CellStyle>();//style实例
        new ExcelExportInitBase() {
       		
    			@Override
    			public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
                    //每个字段对应的style实例
    				String fieldName = field.getName();
                    xlsStyleTempMap.put(fieldName , createStyleAndSetProperties(workbook , exportExcelField));
    			}
    			
    	}.init(clazz, workbook, group);
        
    	this.styleMap.putAll(xlsStyleTempMap);

    	return this;
    }
	
	/**
	 * 描述：实体类没有样式的自动补上默认样式
	 * @author csy 
	 * @date 2020年1月6日
	 * @param clazz
	 * @param workbook
	 * @return InitBaseContextHolder
	 */
	public <T> ExcelExportInitBaseContextHolder defaultStyle(Class<T> clazz , Workbook workbook){
		
		Field[] fields = clazz.getDeclaredFields();
		String fieldName = null;
		
		if (this.styleMap == null)
			this.styleMap = new HashMap<String, CellStyle>();
		
		for (Field field: fields){
			fieldName = field.getName();
			if (!this.styleMap.containsKey(fieldName))
				this.styleMap.put(fieldName, createStyleAndSetProperties(workbook , DefaultCommon.DEFAULTE_EXPORTXLS));
		}
		
		return this;
	}
    
    /**
     * 描述：初始化format
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param group
     * @throws InstantiationException
     * @throws IllegalAccessException void
     */
	public <T> ExcelExportInitBaseContextHolder initFormat(Class<T> clazz , String group) {
		
		if (this.formatMap == null)
			this.formatMap = new HashMap<String, ExcelExportFormatBase>();
    
        final Map<String, ExcelExportFormatBase> xlsFormatTempMap = new HashMap<String, ExcelExportFormatBase>();//format实例
        
        new ExcelExportInitBase() {
    		
			@Override
			public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
				String fieldName = field.getName();
				try {
					xlsFormatTempMap.put(fieldName, exportExcelField.formatClass().newInstance());
				} catch (Exception e) {
					throw new RuntimeException("实例化format对象失败!" , e);
				}
				
			}
			
		}.init(clazz, null, group);
        
		this.formatMap.putAll(xlsFormatTempMap);;
        return this;
    }
	
	/**
	 * 描述：默认format 
	 * @author csy 
	 * @date 2020年1月6日
	 * @param clazz
	 * @return InitBaseContextHolder
	 */
	public <T> ExcelExportInitBaseContextHolder defaultFormat(Class<T> clazz){
		Field[] fields = clazz.getDeclaredFields();
		String fieldName = null;

		if (this.formatMap == null)
			this.formatMap = new HashMap<String, ExcelExportFormatBase>();

		for (Field field: fields){
			fieldName = field.getName();
			if (!this.formatMap.containsKey(fieldName))
				this.formatMap.put(fieldName, DefaultCommon.DEFAULT_FORMAT_IMPL);
		}
		
		return this;
	}	
    
    /**
     * 描述：初始化ExportBean
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param group void
     */
	public <T> ExcelExportInitBaseContextHolder initExportBean(Class<T> clazz , String group){

    	final List<ExportExcelTempBean> exportExcelTempBeans = new ArrayList<ExportExcelTempBean>();
    	
    	new ExcelExportInitBase() {
    		
			@Override
			public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
				String fieldName = field.getName();
				exportExcelTempBeans.add(new ExportExcelTempBean()
						.exportExcelField(exportExcelField)
						.fieldName(fieldName));
			}
			
    	}.init(clazz, null, group);
        
        //重排序
        Collections.sort(exportExcelTempBeans , new Comparator<ExportExcelTempBean>(){
        	
            @Override
            public int compare(ExportExcelTempBean o1, ExportExcelTempBean o2) {
                return o1.getExportExcelField().order() - o2.getExportExcelField().order();
            }

        });
        
        this.exportExcelTempBeans = exportExcelTempBeans;
        return this;
    }
	
    /**
     * 描述：初始化ExportBean
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param headerFields void
     */
	public <T> ExcelExportInitBaseContextHolder initExportBean(Class<T> clazz , List<HeaderField> headerFields){

        if (headerFields == null || headerFields.size() == 0)
        	throw new RuntimeException("headerFields is null");
		
        Field field = null;
        ExportExcelField exportExcelField = null;
        String fieldName = null;
        List<ExportExcelTempBean> exportExcelTempBeans = new ArrayList<ExportExcelTempBean>();
        for (HeaderField headerField:headerFields){
    		try {
    			fieldName = headerField.getFieldName();
				field = clazz.getDeclaredField(fieldName);
    			field.setAccessible(true);
    			exportExcelField = field.isAnnotationPresent(ExportExcelField.class) ? field.getAnnotation(ExportExcelField.class) : DefaultCommon.DEFAULTE_EXPORTXLS;
				exportExcelTempBeans.add(new ExportExcelTempBean()
						.exportExcelField(exportExcelField)
						.fieldName(fieldName));
				
			} catch (NoSuchFieldException e) {
				throw new RuntimeException("找不到字段：" + fieldName , e);
			} catch (SecurityException e) {
				throw new RuntimeException("获取字段失败:" + fieldName, e);
			}finally {
				if (field != null)
					field.setAccessible(false);
			}
        }
        
        this.exportExcelTempBeans = exportExcelTempBeans;
        return this;
    }
	
	/**
	 * 描述：获取format map
	 * @author csy 
	 * @date 2019年12月19日
	 * @return Map<String,ExcelExportFormatBase>
	 */
    public Map<String, ExcelExportFormatBase> getFormatMap() {
		return formatMap;
	}

    /**
     * 描述：获取style map
     * @author csy 
     * @date 2019年12月19日
     * @return Map<String,CellStyle>
     */
	public Map<String, CellStyle> getStyleMap() {
		return styleMap;
	}

	/**
	 * 描述：获取exportExcelTempBeans 集合
	 * @author csy 
	 * @date 2019年12月19日
	 * @return List<ExportXlsTempBean>
	 */
	public List<ExportExcelTempBean> getExportExcelTempBeans() {
		return exportExcelTempBeans;
	}

	/**
     * 描述：創建style並設置屬性
     * @author csy
     * @date 2019年12月4日
     * @param workbook
     * @param exportExcelField
     * @return CellStyle
     */
    private CellStyle createStyleAndSetProperties(Workbook workbook , ExportExcelField exportExcelField){
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
    
}
