package com.csy.test.commons.codegenerate.database.util;

import java.util.ArrayList;
import java.util.List;

import com.csy.test.commons.codegenerate.bean.BeanFieldMessage;
import com.csy.test.commons.database.bean.ColumnMetaData;
import com.csy.test.commons.entity.utils.EntityUtils;

/**
 * 
 * 描述:数据库字段工具类
 * @author csy
 * @date 2021年1月23日 下午12:57:55
 */
public class DataMetaUtils {

	private DataMetaUtils() {}
	
	/**
	 * 
	 * 描述：ColumnMetaData转BeanFieldMessage
	 * @author csy
	 * @date 2021年1月23日 下午12:58:08
	 * @param columnMetaDatas
	 * @return List<BeanFieldMessage>
	 */
	public static List<BeanFieldMessage> tranferToBeanFields(List<ColumnMetaData> columnMetaDatas){
		final List<BeanFieldMessage> beanFieldMessages = new ArrayList<BeanFieldMessage>(columnMetaDatas.size());
		columnMetaDatas.forEach((e) -> {
			beanFieldMessages.add(EntityUtils.sourceTranferTo(e, BeanFieldMessage.class));
		});
		return beanFieldMessages;
	}
}
