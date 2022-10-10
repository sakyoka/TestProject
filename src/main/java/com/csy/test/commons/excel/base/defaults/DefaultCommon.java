package com.csy.test.commons.excel.base.defaults;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.annotion.ExportExcelHeader;
import com.csy.test.commons.excel.base.ExcelExportFormatBase;

/**
 * 公共默认实例
 * @author csy
 * @date 2019年12月20日
 */
public class DefaultCommon {

    /**
     * 默认的ExportExcelField
     */
    public static ExportExcelField DEFAULTE_EXPORTXLS;
    
    /**
     * 默认header
     */
    public static ExportExcelHeader DEFAULTE_EXPORTXLSHEADER;
    
    /**
     * 默认ExcelExportFormatBase实例
     */
    public static ExcelExportFormatBase DEFAULT_FORMAT_IMPL = new DefaultExcelExportFormat();
    
    static{
    	try {
    		Class<ExampleBean> clazz = ExampleBean.class;
    		DEFAULTE_EXPORTXLS = clazz.getDeclaredField("defaultField").getAnnotation(ExportExcelField.class);
    		
    		DEFAULTE_EXPORTXLSHEADER = clazz.getAnnotation(ExportExcelHeader.class);
    		
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 样式、例子
     * @author csy
     * @date 2019年12月20日
     */
    @ExportExcelHeader
    private class ExampleBean{
    	
        @ExportExcelField(order = 0)
        private String defaultField;
    }
}
