package com.csy.test.commons.excel.annotion;

import java.lang.annotation.*;

import com.csy.test.commons.excel.base.ExcelImportConvertBase;
import com.csy.test.commons.excel.base.defaults.DefaultExcelImportConvert;

/**
 * @Description 处理xls导入
 * @Author csy
 * @Date 2019/9/30 12:52
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportExcelField {

    /**
     * @Description //定位对应xls列
     * @Author csy
     * @Date 2019/9/30 12:53
     **/
    int order();
    
    /**
     * 描述：转换类
     * @author csy 
     * @date 2020年7月1日 下午6:10:40
     */
    Class<? extends ExcelImportConvertBase> convertClazz() default DefaultExcelImportConvert.class;
    
    /**
     * 描述：导入分组
     * @author csy 
     * @date 2020年10月16日 下午3:21:46
     */
    String[] groups() default {};
}
