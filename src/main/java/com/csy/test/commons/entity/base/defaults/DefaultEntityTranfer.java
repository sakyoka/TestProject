package com.csy.test.commons.entity.base.defaults;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.csy.test.commons.entity.base.EntityTranferBase;
import com.csy.test.commons.entity.base.annotion.EntityTranfer;
import com.csy.test.commons.entity.base.defaults.DefaultEntityTranferForEach.EntityTranferConfiguration;
import com.csy.test.commons.entity.utils.EntityUtils;
import com.csy.test.commons.utils.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 描述:什么都不做直接返回
 * @author csy
 * @date 2021年1月23日 上午11:47:32
 */
@Slf4j
public class DefaultEntityTranfer implements EntityTranferBase{

	@Override
    public Object objectTransfer(Object sourceValue, Field sourceField, Field targetField,
    		Object sourceEntity, Object targetEntity, EntityTranfer entityTranfer, EntityTranferConfiguration configuration){
		Class<?> fieldTypeCls = targetField.getType();
		if (Objects.isNull(sourceValue)){
			return null;
		}
		
		//尝试转换，失败直接返回
		try {
			return this.tryToTransfer(sourceValue, sourceField, targetField, fieldTypeCls, configuration);
		} catch (Exception e) {
			log.error("默认尝试转换失败，直接返回值", e);
			return sourceValue;
		}
    }
	
    /**
     * 
     * 描述：尝试转换值
     * @author csy
     * @date 2023年8月28日 上午11:44:57
     * @param sourceValue
     * @param sourceField
     * @param targetField
     * @param fieldTypeCls
     * @param configuration 
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
	private Object tryToTransfer(Object sourceValue, Field sourceField, Field targetField, Class<?> fieldTypeCls, 
			EntityTranferConfiguration configuration) 
    		throws NoSuchMethodException, SecurityException, IllegalAccessException, 
    		IllegalArgumentException, InvocationTargetException{
		//List转换
		if (sourceValue instanceof List && fieldTypeCls == List.class){
			
			if (((List<?>)sourceValue).size() == 0){
				return new ArrayList<>(16);
			}
			
			//集合只支持List
	        ParameterizedType parameterizedType = (ParameterizedType)targetField.getGenericType();
	        Class<?> cls = (Class<?>)parameterizedType.getActualTypeArguments()[0];
	        boolean isHaveDefaultConstrutorJavaBean = isHaveDefaultConstrutorJavaBean(cls);
	        //判断集合的对象泛型类型是不是java bean，如果是执行遍历转换
	        if (isHaveDefaultConstrutorJavaBean){
	    		List<?> sourceEntitys = (List<?>)sourceValue;
	    		return EntityUtils.sourceTranferTo(sourceEntitys, cls, configuration);	
	        }
	        
	        List<?> sourceValueList = ((List<?>)sourceValue);
	        //由于上面是java bean，那么这里只能是基本类型，判断两个集合对象泛型类型是不是一致，一致直接返回
	        Class<?> sourceCls = sourceValueList.get(0).getClass();
	        if (sourceCls == cls){
	        	return new ArrayList<>(sourceValueList);
	        }
	        
	        //剩下就是泛型不对应的基本类型，尝试转换
	        return sourceValueList.stream().map(e -> {
				try {
					return tryToNormalTransfer(e, cls);
				} catch (Exception e1) {
					throw new ClassCastException(String.format("字段：%s，尝试转换集合对象泛型类型失败，数据源集合对象泛型类型：%s，目标集合对象泛型类型：%s", 
							targetField.getName(), sourceCls, cls));
				}
	        }).collect(Collectors.toList());
		}
		
		//java bean 转换
		boolean isHaveDefaultConstrutorJavaBean = isHaveDefaultConstrutorJavaBean(targetField);
		if (isHaveDefaultConstrutorJavaBean){
			return EntityUtils.sourceTranferTo(sourceValue, fieldTypeCls, configuration);	
		}
		
		//普通类型转换
		return tryToNormalTransfer(sourceValue, fieldTypeCls);
    } 
    
	/**
     * 
     * 描述：判断是否是java bean
     * @author csy
     * @date 2023年7月3日 下午6:21:13
     * @param targetField
     * @return true是
     */
    private boolean isHaveDefaultConstrutorJavaBean(Field targetField){
    	Class<?> fieldTypeCls = targetField.getType();
    	return this.isHaveDefaultConstrutorJavaBean(fieldTypeCls);
    }
    
    /**
     * 
     * 描述：判断是否是java bean
     * @author csy
     * @date 2024年7月11日 上午10:14:55
     * @param fieldTypeCls
     * @return boolean
     */
    private boolean isHaveDefaultConstrutorJavaBean(Class<?> fieldTypeCls){
    	//判断是否是基本类型（还有很多，但是一般使用的包装类就这些，有还需添加判断）
    	if (fieldTypeCls == String.class
    			|| fieldTypeCls == Boolean.class
    			|| fieldTypeCls == Short.class
    			|| fieldTypeCls == Integer.class
    		    || fieldTypeCls == Long.class
    			|| fieldTypeCls == Float.class
    			|| fieldTypeCls == Double.class
    			|| fieldTypeCls == Byte.class
    			|| fieldTypeCls == Character.class
    			|| fieldTypeCls == Date.class
    			|| fieldTypeCls == java.util.Date.class
    			|| fieldTypeCls == Timestamp.class
    			|| fieldTypeCls == Collection.class
    			|| fieldTypeCls == Map.class){
    		return false;
    	}
    	
    	//判断是否有默认构造器
    	long defaultConstructors = Stream.of(fieldTypeCls.getDeclaredConstructors())
    			.filter(e -> e.getParameterCount() == 0)
    			.count();
    	if (defaultConstructors == 0){
    		return false;
    	}
    	
    	//判断字段修饰符
    	int modifier = fieldTypeCls.getModifiers();
    	if (Modifier.isFinal(modifier)
    			|| Modifier.isInterface(modifier)
    			|| Modifier.isStatic(modifier)){
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * 
     * 描述：尝试普通字段类型转换
     * @author csy
     * @date 2023年7月20日 下午2:57:28
     * @param sourceValue
     * @param fieldTypeCls
     * @return
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException 
     */
    private Object tryToNormalTransfer(Object sourceValue, Class<?> fieldTypeCls) 
    		throws NoSuchMethodException, SecurityException, IllegalAccessException, 
    		IllegalArgumentException, InvocationTargetException{
    	Class<?> valueCls = sourceValue.getClass();
    	//类型一致返回
    	if (valueCls == fieldTypeCls){
    		return sourceValue;
    	}
    	    	
    	//是否是父类
        if (fieldTypeCls.isAssignableFrom(valueCls)) {
            return fieldTypeCls.cast(sourceValue);
        }
        
        //排序非数据类型
    	if (valueCls != String.class
    			&& valueCls != Boolean.class
    			&& valueCls != Short.class
    			&& valueCls != Integer.class
    			&& valueCls != Long.class
    			&& valueCls != Float.class
    			&& valueCls != Double.class
    			&& valueCls != Byte.class
    			&& valueCls != Character.class){
    		throw new IllegalArgumentException("不是基本数据类型");
    	}
        
        if (valueCls == String.class && fieldTypeCls == String.class){
        	return sourceValue.toString();
        }

        //调用基本数据类型包装类的valueOf(String value)
        Method method = fieldTypeCls.getMethod("valueOf", String.class);
        return method.invoke(null, sourceValue.toString());	
    }

}
