package com.csy.test.commons.excel.annotion;

import java.lang.annotation.*;

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
}
