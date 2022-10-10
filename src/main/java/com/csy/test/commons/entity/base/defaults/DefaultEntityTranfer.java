package com.csy.test.commons.entity.base.defaults;

import com.csy.test.commons.entity.base.EntityTranferBase;

/**
 * 
 * 描述:什么都不做直接返回
 * @author csy
 * @date 2021年1月23日 上午11:47:32
 */
public class DefaultEntityTranfer implements EntityTranferBase{

	@Override
	public Object tranfer(Object source) {
		return source;
	}

}
