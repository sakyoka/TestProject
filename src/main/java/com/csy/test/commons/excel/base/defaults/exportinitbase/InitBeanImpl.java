package com.csy.test.commons.excel.base.defaults.exportinitbase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.poi.ss.usermodel.Workbook;

import com.csy.test.commons.excel.annotion.ExportExcelField;
import com.csy.test.commons.excel.base.ExcelExportInitBase;
import com.csy.test.commons.excel.bean.ExportExcelTempBean;
import com.csy.test.commons.excel.bean.ListCount;
import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：fieldName ref ExportExcelField 获取
 * @author csy
 * @date 2021年11月26日 上午9:55:53
 */
public class InitBeanImpl extends ExcelExportInitBase{
	
    private List<ExportExcelTempBean> exportExcelTempBeans;
    
    private List<ExportExcelTempBean> exportExcelHeaderBeans;
	
    private boolean isSubClazzField;
    
    private String group;
    
    private ListCount listCount;
    
    private List<Integer> judgeDuplicates;
    
	@SuppressWarnings("unused")
	private InitBeanImpl(){}
	
	public InitBeanImpl(List<ExportExcelTempBean> exportExcelTempBeans, List<ExportExcelTempBean> exportExcelHeaderBeans, String group){
		this(exportExcelHeaderBeans, group, new ListCount(), new ArrayList<Integer>(), false);
		this.exportExcelTempBeans = exportExcelTempBeans;
	}
	
	private InitBeanImpl(List<ExportExcelTempBean> exportExcelHeaderBeans, String group, 
			ListCount listCount, List<Integer> judgeDuplicates,boolean isSubClazzField){
		this.exportExcelHeaderBeans = exportExcelHeaderBeans;
		this.isSubClazzField = isSubClazzField;
		this.group = group;
		this.listCount = listCount;
		this.judgeDuplicates = judgeDuplicates;
	}
	
	@Override
	protected boolean canContinue(Field field, String group) {
		
        if (!field.isAnnotationPresent(ExportExcelField.class)){
        	return false;
        }
        
        ExportExcelField exportExcelField = field.getAnnotation(ExportExcelField.class);
        //group不为null时，设置分组导出了，然后判断当前字段是否有设置当前分组值
        if (Objects.notNull(group) && !Arrays.asList(exportExcelField.groups()).contains(group)){
        	return false;
        }
        
        return true;
	}

	@Override
	public void done(Field field, Workbook workbook, ExportExcelField exportExcelField) {
		
        int order = exportExcelField.order();
        //判断是否有重复的下标,如果有重复抛异常
        if (this.judgeDuplicates.contains(order)){
            throw new RuntimeException("当前导出对象有重复下标值，请核对");
        }
        this.judgeDuplicates.add(order);
		
		String fieldName  = field.getName();
		//存储最初实体类字段(顶级实体类字段)
		if (!isSubClazzField){
			exportExcelTempBeans.add(new ExportExcelTempBean()
					.exportExcelField(exportExcelField)
					.fieldName(fieldName));
		}
		
		if (exportExcelField.subClazz() == Class.class){			
			//存储header生成字段(包括子集)
			exportExcelHeaderBeans.add(new ExportExcelTempBean()
					.exportExcelField(exportExcelField)
					.fieldName(fieldName));
		}else{
			//如果有子集，那么还需要去子集找
			listCount.oneAccessAssert(field);
			new InitBeanImpl(exportExcelHeaderBeans, group, listCount, judgeDuplicates, true).init(exportExcelField.subClazz(), workbook, group);
		}
	}
}
