package com.csy.test.commons.excel.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.utils.ClassUtils;

/**
 * 初始化基类
 * @author csy
 * @date 2019年12月19日
 */
public abstract class ExcelExportInitBase {
	
	private List<Integer> judgeDuplicates = null;
	
    /**
     * 描述：初始化
     * @author csy 
     * @date 2019年12月19日
     * @param clazz
     * @param group void
     */
    public <T> void init(Class<T> clazz , Workbook workbook , String group){

        List<Field> fields = ClassUtils.getAllFields(clazz);
        Class<ExportExcelField> exportExcelFieldClass = ExportExcelField.class;
        judgeDuplicates = new ArrayList<Integer>();
        for (Field field:fields){
        	
            if (!this.canContinue(field , group))
            	continue;
            
            this.done(field , workbook , field.isAnnotationPresent(exportExcelFieldClass) ? 
            		                           field.getAnnotation(exportExcelFieldClass) : null);
        } 	
    }
    
    /**
     * 描述：描述：是否跳过当前字段 true继续，false跳过
     * @author csy 
     * @date 2020年6月28日
     * @param field
     * @param group
     * @return boolean
     */
    protected abstract boolean canContinue(Field field , String group);

    /**
     * 描述：真正做事的
     * @author csy 
     * @date 2019年12月19日
     * @param field
     * @param workbook
     * @param exportExcelField 
     */
    public abstract void done(Field field , Workbook workbook , ExportExcelField exportExcelField);
    
    /**
     * 描述：默认判断
     * @author csy 
     * @date 2020年6月28日
     * @param field
     * @param group
     * @return boolean
     */
    protected boolean defaultCanContinue(Field field , String group){
    	Class<ExportExcelField> exportExcelFieldClass = ExportExcelField.class;
        if (!field.isAnnotationPresent(exportExcelFieldClass))
        	return false;
        
        ExportExcelField exportExcelField = field.getAnnotation(exportExcelFieldClass);
        //group不为null时，设置分组导出了，然后判断当前字段是否有设置当前分组值
        if (group != null && !Arrays.asList(exportExcelField.groups()).contains(group))
        	return false;
        
        int order = exportExcelField.order();
        //判断是否有重复的下标,如果有重复抛异常
        if (judgeDuplicates.contains(order))
            throw new RuntimeException("当前导出对象有重复下标值，请核对");
        
        judgeDuplicates.add(order);   	
    	return true;
    }
}
