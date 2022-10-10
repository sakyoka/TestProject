package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.defaults.DefaultCommon;
import com.csy.test.commons.excel.base.defaults.exportinitbase.DefaultInitFormatImpl;
import com.csy.test.commons.excel.base.defaults.exportinitbase.DefaultInitStyleImpl;
import com.csy.test.commons.excel.base.defaults.exportinitbase.InitBeanImpl;
import com.csy.test.commons.excel.base.defaults.exportinitbase.InitFormatImpl;
import com.csy.test.commons.excel.base.defaults.exportinitbase.InitStyleImpl;
import com.csy.test.commons.excel.base.defaults.exportinitbase.InitSubClassImpl;
import com.csy.test.commons.excel.bean.ExportExcelTempBean;
import com.csy.test.commons.excel.bean.exportheader.HeaderField;
import com.csy.test.commons.utils.Objects;

/**
 * 初始化实现
 * @author csy
 * @date 2019年12月19日
 */
public class ExcelExportInitBaseContextHolder {
	
    private Map<String, ExcelExportFormatBase> formatMap;//key为实体类对象的属性，对应的value为字段的format实例

    private Map<String, CellStyle> styleMap;//key为实体类对象的属性，对应的value为字段的CellStyle实例

    private List<ExportExcelTempBean> exportExcelTempBeans;//该集合为每个字段对应的ExportXls
    
    private List<ExportExcelTempBean> exportExcelHeaderBeans;//该集合用于生成header by class
    
    private Map<String, CellStyle> headerStyleMap;//key为实体类对象的属性，对应的value为字段的CellStyle实例 列标题样式
    
    private ExcelExportInitBaseContextHolder subExcelExportInitBaseContextHolder;
    
    private ExcelExportInitBaseContextHolder(){}
    
    /**
     * 描述：获取ExcelExportInitBaseContextHolder实例
     * @author csy 
     * @date 2019年12月19日
     * @return InitBaseContextHolder
     */
    public static ExcelExportInitBaseContextHolder newInstance(){
    	return new ExcelExportInitBaseContextHolder();
    }
    
    /**
     * 描述：初始化样式
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param workbook
     * @param group 
     * @return ExcelExportInitBaseContextHolder
     */
	public <T> ExcelExportInitBaseContextHolder initStyle(Class<T> clazz , Workbook workbook , String group) {
        
		Map<String, CellStyle> xlsStyleTempMap = new HashMap<String, CellStyle>();//style实例
        Map<String, CellStyle> xlsHeaderStyleTempMap = new HashMap<String, CellStyle>();//style实例
        ExcelExportInitBase excelExportInitBase = new InitStyleImpl(xlsStyleTempMap, xlsHeaderStyleTempMap);
        excelExportInitBase.init(clazz, workbook, group);
		
    	if (Objects.isNull(this.styleMap))
    		this.styleMap = new HashMap<String, CellStyle>(xlsStyleTempMap.size());
		
    	this.styleMap.putAll(xlsStyleTempMap);
    	
    	if (Objects.isNull(this.headerStyleMap))
    		this.headerStyleMap = new HashMap<String, CellStyle>(xlsHeaderStyleTempMap.size());
    	
    	this.headerStyleMap.putAll(xlsHeaderStyleTempMap);
    	
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
		
        Map<String, CellStyle> xlsStyleTempMap = new HashMap<String, CellStyle>();//style实例
        Map<String, CellStyle> xlsHeaderStyleTempMap = new HashMap<String, CellStyle>();//style实例
		ExcelExportInitBase excelExportInitBase = new DefaultInitStyleImpl(xlsStyleTempMap, xlsHeaderStyleTempMap);
		excelExportInitBase.init(clazz, workbook, null);
		
    	if (Objects.isNull(this.styleMap))
    		this.styleMap = new HashMap<String, CellStyle>(xlsStyleTempMap.size());
		
    	this.styleMap.putAll(xlsStyleTempMap);
    	
    	if (Objects.isNull(this.headerStyleMap))
    		this.headerStyleMap = new HashMap<String, CellStyle>(xlsHeaderStyleTempMap.size());
    	
    	this.headerStyleMap.putAll(xlsHeaderStyleTempMap);

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
        Map<String, ExcelExportFormatBase> xlsFormatTempMap = new HashMap<String, ExcelExportFormatBase>();//format实例
        ExcelExportInitBase excelExportInitBase = new InitFormatImpl(xlsFormatTempMap);
        excelExportInitBase.init(clazz, null, group);
        
		if (Objects.isNull(this.formatMap))
			this.formatMap = new HashMap<String, ExcelExportFormatBase>(xlsFormatTempMap.size());
		
		this.formatMap.putAll(xlsFormatTempMap);
        
		return this;
	}
	
	/**
	 * 描述：默认format 
	 * @author csy 
	 * @date 2020年1月6日
	 * @param clazz
	 * @return ExcelExportInitBaseContextHolder
	 */
	public <T> ExcelExportInitBaseContextHolder defaultFormat(Class<T> clazz){

		Map<String, ExcelExportFormatBase> xlsFormatTempMap = new HashMap<String, ExcelExportFormatBase>();//format实例
		
		ExcelExportInitBase excelExportInitBase = new DefaultInitFormatImpl(xlsFormatTempMap);
		excelExportInitBase.init(clazz, null, null);
		
		if (Objects.isNull(this.formatMap))
			this.formatMap = new HashMap<String, ExcelExportFormatBase>(xlsFormatTempMap.size());
		
		this.formatMap.putAll(xlsFormatTempMap);
		
		return this;
	}	
    
    /**
     * 描述：初始化ExportBean
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param group
     * @return ExcelExportInitBaseContextHolder
     */
	public <T> ExcelExportInitBaseContextHolder initExportBean(Class<T> clazz , String group){

    	List<ExportExcelTempBean> exportExcelTempBeans = new ArrayList<ExportExcelTempBean>();
    	List<ExportExcelTempBean> exportExcelHeaderBeans = new ArrayList<ExportExcelTempBean>();
    	
    	ExcelExportInitBase excelExportInitBase = new InitBeanImpl(exportExcelTempBeans, exportExcelHeaderBeans, group);
    	excelExportInitBase.init(clazz, null, group);
    	
        //重排序
    	this.resortExportTempBean(exportExcelTempBeans);
    	this.resortExportTempBean(exportExcelHeaderBeans);
        
        if (Objects.isNull(this.exportExcelTempBeans)){
        	this.exportExcelTempBeans = new ArrayList<ExportExcelTempBean>(exportExcelTempBeans.size());
        }
        
        if (Objects.isNull(this.exportExcelHeaderBeans)){
        	this.exportExcelHeaderBeans = new ArrayList<ExportExcelTempBean>(exportExcelHeaderBeans.size());
        }
        
        this.exportExcelTempBeans.addAll(exportExcelTempBeans);
        this.exportExcelHeaderBeans.addAll(exportExcelHeaderBeans);
        return this;
    }
	
	private void resortExportTempBean(List<ExportExcelTempBean> exportExcelTempBeans){
        Collections.sort(exportExcelTempBeans , new Comparator<ExportExcelTempBean>(){
        	
            @Override
            public int compare(ExportExcelTempBean o1, ExportExcelTempBean o2) {
                return o1.getExportExcelField().order() - o2.getExportExcelField().order();
            }

        });
	}
	
    /**
     * 描述：初始化ExportBean
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param headerFields
     * @return ExcelExportInitBaseContextHolder
     */
	public <T> ExcelExportInitBaseContextHolder initExportBean(Class<T> clazz , List<HeaderField> headerFields){

        if (headerFields == null || headerFields.size() == 0)
        	throw new RuntimeException("headerFields is null");
		
        Field field = null;
        ExportExcelField exportExcelField = null;
        String fieldName = null;
        final List<ExportExcelTempBean> exportExcelTempBeans = new ArrayList<ExportExcelTempBean>();
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
        
        if (Objects.isNull(this.exportExcelTempBeans)){
        	this.exportExcelTempBeans = new ArrayList<ExportExcelTempBean>(exportExcelTempBeans.size());
        }
        
        this.exportExcelTempBeans.addAll(exportExcelTempBeans);
        
        return this;
    }
	
	/**
	 * 描述：初始化子集合类型
	 * @author csy 
	 * @date 2021年11月29日 下午4:50:17
	 * @param clz
	 * @param workbook
	 * @param group
	 * @return ExcelExportInitBaseContextHolder
	 */
	public <T> ExcelExportInitBaseContextHolder initSubClazz(Class<T> clz, Workbook workbook , String group){
		ExcelExportInitBase excelExportInitBase = new InitSubClassImpl(this, group);
		excelExportInitBase.init(clz, workbook, group);
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
	 * 描述：获取列标题对应样式
	 * @author csy 
	 * @date 2020年6月30日
	 * @return Map<String,CellStyle>
	 */
	public Map<String, CellStyle> getHeaderStyleMap() {
		return headerStyleMap;
	}
	
	public List<ExportExcelTempBean> getExportExcelHeaderBeans() {
		return exportExcelHeaderBeans;
	}

	/**
	 * 描述：持有仅且一个子对象
	 * @author csy 
	 * @date 2021年11月29日 下午4:26:09
	 * @return ExcelExportInitBaseContextHolder
	 */
	public ExcelExportInitBaseContextHolder getSubExcelExportInitBaseContextHolder() {
		return subExcelExportInitBaseContextHolder;
	}

	public void setSubExcelExportInitBaseContextHolder(
			ExcelExportInitBaseContextHolder subExcelExportInitBaseContextHolder) {
		this.subExcelExportInitBaseContextHolder = subExcelExportInitBaseContextHolder;
	}
	
	/**
	 * 描述：清空资源
	 * @author csy 
	 * @date 2021年12月1日 上午10:16:29
	 */
	public void release(){
	    if (Objects.notNull(formatMap)){
	    	formatMap.clear();
	    }
	    
	    if (Objects.notNull(styleMap)){
	    	styleMap.clear();
	    }

	    if (Objects.notNull(headerStyleMap)){
	    	headerStyleMap.clear();
	    }
	    
	    if (Objects.notNull(exportExcelTempBeans)){
	    	exportExcelTempBeans.clear();
	    }
	    
	    if (Objects.notNull(subExcelExportInitBaseContextHolder)){
	    	subExcelExportInitBaseContextHolder.release();
	    }
	    
	    if(Objects.notNull(exportExcelHeaderBeans)){
	    	exportExcelHeaderBeans.clear();
	    }
	}
}
