package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;

/**
 * 初始化基类
 * @author csy
 * @date 2019年12月19日
 */
public abstract class ExcelExportInitBase {
	
    /**
     * 描述：初始化
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param group void
     */
    public <T> void init(Class<T> clazz , Workbook workbook , String group){
    	
    	List<Integer> judgeDuplicates = new ArrayList<Integer>();
        Field[] fields = clazz.getDeclaredFields();
        Class<ExportExcelField> exportExcelFieldClass = ExportExcelField.class;
        for (Field field:fields){
        	
            if (!field.isAnnotationPresent(exportExcelFieldClass))
                continue;
            
            ExportExcelField exportExcelField = field.getAnnotation(exportExcelFieldClass);
            //group不为null时，设置分组导出了，然后判断当前字段是否有设置当前分组值
            if (group != null && !Arrays.asList(exportExcelField.groups()).contains(group))
            	continue;
            
            int order = exportExcelField.order();
            //判断是否有重复的下标,如果有重复抛异常
            if (judgeDuplicates.contains(order))
                throw new RuntimeException("当前导出对象有重复下标值，请核对");
            
            judgeDuplicates.add(order);
            
            this.done(field , workbook , exportExcelField);
        } 	
    }
 

    /**
     * 描述：真正做事的
     * @author csy 
     * @date 2019年12月19日
     * @param field
     * @param workbook
     * @param exportExcelField 
     */
    public abstract void done(Field field , Workbook workbook , ExportExcelField exportExcelField);
}
