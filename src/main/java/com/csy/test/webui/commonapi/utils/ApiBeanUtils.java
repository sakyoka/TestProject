package com.csy.test.webui.commonapi.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.excel.utils.CollectionUtils;
import com.csy.test.webui.commonapi.bean.ApiBean;
import com.csy.test.webui.commonapi.bean.ApiParamBean;
import com.csy.test.webui.commonapi.bean.PageBean;

/**
 * 
 * 描述：ApiBean 工具类
 * @author csy
 * @date 2022年10月9日 下午5:54:31
 */
public class ApiBeanUtils {

	/**
	 * 
	 * 描述：参数list转map
	 * @author csy
	 * @date 2022年9月12日 下午1:57:03
	 * @param apiBean
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> paramlistToMap(ApiBean apiBean){
		List<ApiParamBean> apiParamBeans = apiBean.getParams();
		if (CollectionUtils.isNotEmpty(apiParamBeans)) {
			return apiParamBeans.stream().collect(
					Collectors.toMap(ApiParamBean::getFieldName, ApiParamBean::getFieldValue));
		}
		return Collections.emptyMap();
	}
	
	/**
	 * 
	 * 描述：转java对象
	 * @author csy
	 * @date 2022年10月8日 下午4:53:03
	 * @param apiBean
	 * @param cls
	 * @return T
	 */
	public static <T> T toBean(ApiBean apiBean, Class<T> cls){
		return toBean(paramlistToMap(apiBean), cls);
	}
	
	/**
	 * 
	 * 描述：转java对象
	 * @author csy
	 * @date 2022年10月8日 下午4:53:06
	 * @param text
	 * @param cls
	 * @return T
	 */
	public static <T> T toBean(String text, Class<T> cls){
		return JSON.parseObject(text, cls);
	}
	
	/**
	 * 
	 * 描述：转java对象
	 * @author csy
	 * @date 2022年10月8日 下午4:53:15
	 * @param param
	 * @param cls
	 * @return T
	 */
	public static <T> T toBean(Map<String, Object> param, Class<T> cls){
		return toBean(JSON.toJSONString(param), cls);
	}
	
	/**
	 * 
	 * 描述：获取pageBean
	 * @author csy
	 * @date 2022年10月9日 下午5:52:09
	 * @param apiBean
	 * @return PageBean
	 */
	public static PageBean getPageBean(ApiBean apiBean){
		Map<String, Object> paramObject = paramlistToMap(apiBean);
		Object pageObject = paramObject.get("pageObject");
		return toBean(JSON.toJSONString(pageObject), PageBean.class);
	}
	
	/**
	 * 
	 * 描述：获取pageBean
	 * @author csy
	 * @date 2022年10月9日 下午5:52:44
	 * @param paramObject
	 * @return PageBean
	 */
	public static PageBean getPageBean(Map<String, Object> paramObject){
		Object pageObject = paramObject.get("pageObject");
		return toBean(JSON.toJSONString(pageObject), PageBean.class);
	}
}
