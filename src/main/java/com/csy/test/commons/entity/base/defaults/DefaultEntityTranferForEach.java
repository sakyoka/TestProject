package com.csy.test.commons.entity.base.defaults;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.csy.test.commons.entity.base.AbstractFieldForEach;
import com.csy.test.commons.entity.base.annotion.EntityTranfer;
import com.csy.test.commons.entity.cache.FieldExecutorCache;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

/**
 * 
 * 描述:对象转换
 * @author csy
 * @date 2021年1月23日 下午12:06:04
 * @param <T>
 * @param <W>
 */
public class DefaultEntityTranferForEach<T , W> extends AbstractFieldForEach<T>{
	
	/**数据源对象*/
    private W sourceEntity;
    
    /**配置对象-配置参数抽取到这个对象中*/
    private EntityTranferConfiguration configuration;
    
    /**为了适配不配置注解时候的字段，默认的注解*/
    private static EntityTranfer DEFAULT_ENTITY_TRANFER;
        
    static{
    	try {
			DEFAULT_ENTITY_TRANFER = DefaultEntityTranferClass.class
			        .getDeclaredField("defaultField").getAnnotation(EntityTranfer.class);
		} catch (NoSuchFieldException e) {}
    }
    
    public DefaultEntityTranferForEach(W sourceEntity, EntityTranferConfiguration configuration) {
        this.sourceEntity = sourceEntity;
        this.configuration = Objects.notNull(configuration) 
        		? configuration 
        		: EntityTranferConfiguration.builder().build();
    }

    @Override
    protected void execute(Object targetEntity, Field targetField) {
    	
        EntityTranfer entityTranfer = targetField.isAnnotationPresent(EntityTranfer.class) ?
        		targetField.getAnnotation(EntityTranfer.class) : DEFAULT_ENTITY_TRANFER;
        try {
        	//获取数据源字段
        	Field sourceField = this.getSourceField(entityTranfer, targetField);
        	//获取数据源字段对应的值
            Object sourceValue = ClassUtils.getFieldValue(sourceField, this.sourceEntity);
            //处理sourceValue 转换成最终需要的值
            Object targetValue = FieldExecutorCache.getEntityTranferByClazz(entityTranfer.entityTranferClazz())
            		.objectTransfer(sourceValue, sourceField, targetField, this.sourceEntity, targetEntity, entityTranfer, configuration);
            //设置值
            ClassUtils.setFieldValue(targetField, targetEntity, targetValue);
        } catch (Exception e) {
        	//追加参数是否需要抛异常，默认不抛出
        	if (Objects.notNull(this.configuration.getIgnoreError()) && !this.configuration.getIgnoreError()){
        		throw new RuntimeException("处理转换失败", e);
        	}
        	//失败不处理
        }	
    }
    
    /**
     * 考虑到来源值可能在不同对象而且字段名称不一样，允许sourceFieldName配多个来源多段
     * <br> 如果空的时候默认使用targetField.getName()
     * <br> 否则从sourceFieldName遍历，从sourceEntity获取成功的field
     * @author csy
     * @date 2023年6月26日 下午3:44:08
     * @param entityTranfer 转换器
     * @param targetField 目标字段
     * @return Field 数据源字段
     */
    private Field getSourceField(EntityTranfer entityTranfer, Field targetField){
    	
    	//优先从targetRefSourceFieldNameMap获取数据源对象fieldName
    	if (Objects.notNull(this.configuration.getTargetRefSourceFieldNameMap()) 
    			&& this.configuration.getTargetRefSourceFieldNameMap().containsKey(targetField.getName())){
            String sourceFieldName = this.configuration.getTargetRefSourceFieldNameMap().get(targetField.getName());
            return ClassUtils.getFieldByFieldName(this.sourceEntity.getClass(), sourceFieldName);
    	}
    	
    	//判断sourceFieldName有没有配置，没有默认使用field.getName()作为fieldName
        String[] sourceFieldNames = entityTranfer.sourceFieldName().length == 0 
        		? new String[]{targetField.getName()} : entityTranfer.sourceFieldName();
        for (String sourceFieldName:sourceFieldNames){
            try {
                return ClassUtils.getFieldByFieldName(this.sourceEntity.getClass(), sourceFieldName);
            } catch (Exception e) {}	
        }
        return null;
    }

    @Override
    public boolean canContiune(T entity, Field field) {

    	if (!super.canContiune(entity, field)){
    		return false;
    	}
    	
    	//在排除集合中，直接返回false
    	if (Objects.notNull(this.configuration.getExcludeFieldNames()) 
    			&& this.configuration.getExcludeFieldNames().contains(field.getName())){
    		return false;
    	}
    	
    	//当仅在这个集中，字段不在这个集合时候，返回false
    	if (Objects.notNull(this.configuration.getOnlyIncludeFieldNames()) 
    			&& this.configuration.getOnlyIncludeFieldNames().size() > 0
    			&& !this.configuration.getOnlyIncludeFieldNames().contains(field.getName())){
    		return false;
    	}
    	
        boolean isAnnotation = field.isAnnotationPresent(EntityTranfer.class);

        //分组优先
        if (Objects.notNull(this.configuration.getGroup()) 
        		&& !Objects.EMPTY_STRING.equals(this.configuration.getGroup())) {
            if (!isAnnotation) {
                return false;
            }
            EntityTranfer entityTranfer = field.getAnnotation(EntityTranfer.class);
            String[] groups = entityTranfer.groups();
            return Arrays.asList(groups).contains(this.configuration.getGroup());
        }

        if (isAnnotation) {
            EntityTranfer entityTranfer = field.getAnnotation(EntityTranfer.class);
            return !entityTranfer.ignoreWhenGroupIsNull();
        }

        return true;
    }

    static class DefaultEntityTranferClass {

        @EntityTranfer
        private String defaultField;
    }
    
    /**
     * 
     * 描述：配置属性对象
     * @author csy
     * @date 2023年9月25日 上午10:23:35
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EntityTranferConfiguration {
    	
        /**字段分组：如果该值不为空，取对应分组值的字段处理，否则处理全部字段*/
        private String group;

        /**
         * 扩展属性：
         * <br>为了有些不使用注解的情况，但是fieldName不一致问题
         * <br>目标对象fieldName映射数据源对象的fieldName
         * <br>当前字段  -> 目标字段 ，当前字段需要从哪个字段中获取值
         */
        @Default
        private Map<String, String> targetRefSourceFieldNameMap = Collections.emptyMap();
        
        /**
         * 是否忽略报错，默认忽略
         */
        @Default
        private Boolean ignoreError = true;
        
        /**
         * 排除字段集合（只要在这个集合里面字段都会排除掉）
         */
        @Default
        private List<String> excludeFieldNames = Collections.emptyList();
        
        /**
         * 只在这个集合中的
         */
        private List<String> onlyIncludeFieldNames;
    }
}
