package com.csy.test.commons.valid.annotion;


import java.lang.annotation.*;

import com.csy.test.commons.valid.base.AbstractValidBase;

/**
 * @Description // 校验案件字段
 * @Author csy
 * @Date 2019/5/16 11:10
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Valid {

    /**
     * @Description //校验分类（只有所属分类才校验）
     * @Author csy
     * @Date 2019/5/16 11:14
     **/
    String[] target() default {};

    /**
     * @Description  同target，String最终根据分割符分割数组
     * <br>注:因为target数组可能很长，所以可以用字符串方式
     * @Author csy
     * @Date 2019/5/17 13:46
     **/
    String targetString() default "";

    /**
     * @Description targetString的分隔符
     * @Author csy
     * @Date 2019/5/17 14:00
     **/
    String targetStringSpilt() default ",";

    /**
     * @Description //校验类型
     * @Author csy
     * @Date 2019/5/16 11:16
     **/
    Class<? extends AbstractValidBase>[] validType() default {};

    /**
     * @Description //错误提示语
     * @Author csy
     * @Date 2019/5/16 11:24
     **/
    String errorMessage() default "Validation result is failed ! detail message asking for developer.";//如果是null时，会提示本字段不符合格式

    /**
     * @Description //字段别名
     * @Author csy
     * @Date 2019/7/12 16:11
     * @Param []
     * @return java.lang.String
     **/
    String fieldName() default "";
}
