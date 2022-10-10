package com.csy.test.webui.commonapi.bean;

import java.util.List;
import lombok.Data;

/**
 * 
 * 描述：公共接口参数对象
 * @author csy
 * @date 2022年9月12日 下午12:10:03
 */
@Data
public class ApiBean {
	
	/**业务分类(大主题) @see ServiceClassEnum*/
	private String serviceClass;
	
	/**业务方法(主题里面具体方法) @see ServiceMethodEnum*/
	private String serviceMethod;
	
	/**参数集合*/
	private List<ApiParamBean> params;
}
