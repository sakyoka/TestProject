package com.csy.test.commons.entity.base;

import java.lang.reflect.Field;

import com.csy.test.commons.entity.base.annotion.EntityTranfer;
import com.csy.test.commons.entity.base.defaults.DefaultEntityTranferForEach.EntityTranferConfiguration;

/**
 * 
 * 描述：属性转换器(外部字段，对应转换)
 * @author csy
 * @date 2021年2月4日 上午10:38:40
 */
public interface EntityTranferBase {

    /**
     * 
     * 描述：转换值
     * <br> 默认调用方法
     * @author csy
     * @date 2023年7月3日 下午4:47:43
     * @param sourceValue
     * @param sourceField
     * @param targetField
     * @param sourceEntity
     * @param targetEntity
     * @param entityTranfer
     * @param configuration 
     * @return Object
     */
    default Object objectTransfer(
    		Object sourceValue, 
    		Field sourceField,
    		Field targetField,
    		Object sourceEntity, 
    		Object targetEntity, 
    		EntityTranfer entityTranfer, 
    		EntityTranferConfiguration configuration){
    	return this.objectTransfer(sourceValue, sourceField, targetField, sourceEntity, targetEntity, entityTranfer);
    };
    
    /**
     * 
     * 描述：转换值
     * @author csy
     * @date 2023年7月3日 下午4:47:43
     * @param sourceValue
     * @param sourceField
     * @param targetField
     * @param sourceEntity
     * @param targetEntity
     * @param entityTranfer
     * @return Object
     */
    default Object objectTransfer(
    		Object sourceValue, 
    		Field sourceField,
    		Field targetField,
    		Object sourceEntity, 
    		Object targetEntity, 
    		EntityTranfer entityTranfer){
    	
    	return this.tranfer(sourceValue, targetEntity);
    };
	
    /**
     * 描述：转换值
     * <br> 不推荐实现这个方法，而是直接使用objectTransfer
     * @param sourceValue
     * @param entity
     * @return Object
     * @author csy
     * @date 2022年6月17日 下午4:38:18
     */
	@Deprecated
    default Object tranfer(Object sourceValue, Object entity){
    	return sourceValue;
    };
}
