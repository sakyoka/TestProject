package com.csy.test.commons.excel.base.defaults.exportinitbase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportInitBase;
import com.csy.test.commons.excel.base.ExcelExportInitBaseContextHolder;
import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：处理子集合初始化
 * @author csy
 * @date 2021年11月29日 下午4:51:52
 */
public class InitSubClassImpl extends ExcelExportInitBase{

	private ExcelExportInitBaseContextHolder excelExportInitBaseContextHolder;
	
	private String group;
	
	private int listCount = 0;
	
	public InitSubClassImpl(ExcelExportInitBaseContextHolder excelExportInitBaseContextHolder, String group){
		this.excelExportInitBaseContextHolder = excelExportInitBaseContextHolder;
		this.group = group;
	}
	
	@Override
	protected boolean canContinue(Field field, String group) {
		
		if (!defaultCanContinue(field, group)){
			return false;
		}
		
		if (field.getAnnotation(ExportExcelField.class).subClazz() == Class.class){
			return false;
		}
		
    	if (field.getType() == List.class
    			|| field.getType() == ArrayList.class
    			|| field.getType() == LinkedList.class
    			|| field.getType() == Set.class
    			|| field.getType() == HashSet.class
    			|| field.getType() == LinkedHashSet.class){
    		Objects.isConditionAssert(listCount == 0, RuntimeException.class, "对象(子对象不许存在集合)里面仅且只有一个集合类型");
        	listCount++;
        	return true;
    	}
    	
    	throw new RuntimeException("field subClass must be collection");

	}
	
	@Override
	public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
		//不支持 initExportBean(Class<T> clazz , List<HeaderField> headerFields) 类型
		if (exportExcelField.subClazz() != Class.class){
		this.excelExportInitBaseContextHolder.setSubExcelExportInitBaseContextHolder(ExcelExportInitBaseContextHolder.newInstance()
				.initExportBean(exportExcelField.subClazz(), group)
				.initStyle(exportExcelField.subClazz(), workbook, group)
				.initFormat(exportExcelField.subClazz(), group));
		}
	}
}
