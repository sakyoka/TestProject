package com.csy.test.webui.commonapi.bean;

import lombok.Data;

/**
 * 
 * 描述：接口参数对象
 * @author csy
 * @date 2022年9月12日 下午12:09:20
 */
@Data
public class ApiParamBean {

	/**字段key*/
	private String fieldName;
	
	/**字段值*/
	private Object fieldValue;
}
