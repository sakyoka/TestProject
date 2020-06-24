package com.csy.test.commons.valid.utils;


import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.csy.test.commons.valid.annotion.Valid;
import com.csy.test.commons.valid.base.AbstractValidBase;
import com.csy.test.commons.valid.bean.ParamValidResult;
import com.csy.test.commons.valid.exception.ValidException;

/**
 * 校验实体类字段@Valid
 * <br> 可通过自定义校验器（实现AbstractValidBase） ， 实现自己校验规则
 * @author chensy
 * @Description
 * @date: 2019-05-16 11:41
 */
public class ValidUtils {

    private ValidUtils(){}

    /**
     * 描述：校验
     * @author csy 
     * @date 2020年6月12日
     * @param entity
     * @return ParamResult
     */
    public static ParamValidResult valid(Object entity){

        return valid(null, entity);
    }

    /**
     * 描述：校验
     * @author csy 
     * @date 2020年6月12日
     * @param target
     * @param entity
     * @return ParamResult
     */
    public static ParamValidResult valid(String target, Object entity){
    	ParamValidResult paramResult = ParamValidResult.getBuilder();
        
    	if (entity == null){
            return paramResult;
        }
        
        Field[] fields = entity.getClass().getDeclaredFields();
        if (isEmptyArray(fields)){
            return paramResult;
        }
        
        //暂不处理子对象
        Class<Valid> validCaseClazz = Valid.class;
        Valid validCaseAnnotation = null;
        String targetString = null;
        String targetStringSpilt = null;
        for (Field field:fields){
            if (! field.isAnnotationPresent(validCaseClazz)){
                //没有该注解
                continue;
            }
            validCaseAnnotation  = field.getAnnotation(validCaseClazz);
            targetString = validCaseAnnotation.targetString();
            targetStringSpilt = validCaseAnnotation.targetStringSpilt();
            if ( notInGroup(target , validCaseAnnotation.target() , targetString , targetStringSpilt) ){
                //如果不属于该分类
                continue;
            }
            
            Class<? extends AbstractValidBase>[] validExecuteClazzs = validCaseAnnotation.validType();
            if (isEmptyArray(validExecuteClazzs)){
                //校验规则集合为空
                continue;
            }
            
            Boolean result = false;//校验结果，如果通过返回true,反则false
            String fieldName = isEmptySting( validCaseAnnotation.fieldName() ) ?  field.getName() : validCaseAnnotation.fieldName();//当前字段
            AbstractValidBase validCaseBase = null;//基类
            String errorMessage = validCaseAnnotation.errorMessage();//校验错误信息(默认在注解上的信息)
            for (Class<? extends AbstractValidBase> validExecuteClazz:validExecuteClazzs){
                try {
                    //执行校验
                    validCaseBase = validExecuteClazz.newInstance();
                    field.setAccessible(true);
                    result = validCaseBase.valid(field.get(entity));
                    if (! result){
                    	paramResult.paramError(fieldName , errorMessage);
                    	break;
                    }
                }catch (ValidException e){
                    //如果是自己抛的异常
                    errorMessage = isEmptySting(e.getMessage()) ?  errorMessage : e.getMessage();
                    paramResult.paramError(fieldName , errorMessage);
                }catch (Exception e){
                    //如果是其它异常
                    throw new RuntimeException("ValidUtil valid => 校验过程中出现未知错误 " , e);
                }finally {
                    field.setAccessible(false);
                }
            }
        }
        return paramResult;
    }
    
    private static boolean notInGroup(String target , String[] targetArray , String targetString , String targetStringSpilt){
    	return target != null && !arrayToList(targetArray).contains(target) 
		&& !stringToList(targetString , targetStringSpilt).contains(target);    	
    }

    private static boolean isEmptyArray(Object[] objects){
        return objects == null || objects.length == 0 ? true : false;
    }

    @SuppressWarnings("unchecked")
	private static <T> List<T> arrayToList(T [] arr){
        return null == arr || arr.length == 0 ? Collections.EMPTY_LIST : Arrays.asList(arr);
    }

    @SuppressWarnings("unchecked")
	private static List<String> stringToList(String source , String spiltTag){
        return source == null || "".equals(source) ? Collections.EMPTY_LIST : arrayToList( source.split(spiltTag) );
    }

    private static boolean isEmptySting(String v){
        return null == v || v.isEmpty();
    }
}
