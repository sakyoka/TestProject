package com.csy.test.commons.entity.base;

/**
 * 
 * 描述：属性转换器(外部字段，对应转换)
 * @author csy
 * @date 2021年2月4日 上午10:38:40
 */
public interface EntityTranferBase {

	/**
	 * 描述：转换值
	 * @author csy 
	 * @date 2022年6月17日 下午4:38:18
	 * @param source
	 * @param entity 
	 * @return Object
	 */
	Object tranfer(Object source, Object entity);
}
