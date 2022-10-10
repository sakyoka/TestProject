package com.csy.test.commons.codegenerate.database.defaults;

import com.csy.test.commons.entity.base.EntityTranferBase;
import com.csy.test.commons.utils.StrUtil;

/**
 * 
 * 描述:转驼峰
 * @author csy
 * @date 2021年1月23日 下午12:34:15
 */
public class EntityTranferToHump implements EntityTranferBase{

	@Override
	public Object tranfer(Object source) {
		if (source == null)
			return source;
		
		return StrUtil.toCamelCase(source.toString());
	}

}
