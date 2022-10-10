package com.csy.test.commons.entity.build;

import java.util.HashMap;
import java.util.Map;

import com.csy.test.commons.entity.base.TransformBase;
import com.csy.test.commons.entity.utils.EntityUtils;
import com.csy.test.commons.utils.Objects;

/**
 * 
 * 描述：属性填充实体类
 * @author csy
 * @date 2021年11月19日 下午4:50:44
 * @param <T>
 */
public class BeanBuilder<T>{

	private Class<T> clz;
	
	private Map<String, Object> fieldValueMap;
	
	private Map<String, TransformBase> transformMap;
	
	public BeanBuilder(Class<T> clz){
		Objects.notNullAssert(clz, "clz not allow null");
		this.clz = clz;
		this.fieldValueMap = new HashMap<String, Object>();
		this.transformMap = new HashMap<String, TransformBase>();		
	}
	
	public BeanBuilder<T> reset(Class<T> clz){
		Objects.notNullAssert(clz, "clz not allow null");
		this.clz = clz;
		this.fieldValueMap.clear();
		this.transformMap.clear();
		return this;
	}
	
	public BeanBuilder<T> addTransformBase(String fieldName, TransformBase transformBase){
		this.transformMap.put(fieldName, transformBase);
		return this;
	}
	
	public BeanBuilder<T> addTransformBase(Map<String, TransformBase> transformMap){
		
		if (Objects.notNull(transformMap))
			this.transformMap.putAll(transformMap);;
		return this;
	}
	
	public BeanBuilder<T> set(String fieldName, Object value){
		this.fieldValueMap.put(fieldName, value);
		return this;
	}
	
	public BeanBuilder<T> set(Map<String, Object> fieldValueMap){
		
		if (Objects.notNull(fieldValueMap))
			this.fieldValueMap.putAll(fieldValueMap);
		
		return this;
	}
	
	public T build() {
		return this.build(false);
	}
	
	/**
	 * 描述：设置属性
	 * @author csy 
	 * @date 2021年11月19日 下午4:51:13
	 * @param ignoreField 是否可以忽略不存在的属性 true可以，false不可以
	 * @return T
	 */
	public T build(Boolean ignoreField){
		return EntityUtils.sourceTranferTo(fieldValueMap, clz, transformMap, ignoreField);		
	}
}
