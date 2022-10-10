package com.csy.test.webui.commonapi.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.csy.test.commons.result.ResultBean;
import com.csy.test.webui.commonapi.bean.ApiBean;
import com.csy.test.webui.commonapi.utils.CommonApiUtils;

/**
 * 
 * 描述：公共接口
 * @author csy
 * @date 2022年9月12日 下午12:00:12
 */
@RequestMapping("/api/common")
@RestController
public class CommonApiController {

	/**
	 * 
	 * 描述：公共接口
	 * @author csy
	 * @date 2022年9月12日 下午12:03:37
	 * @param apiBean
	 * @return ResultBean
	 */
	@PostMapping
	public ResultBean commonApi(@RequestBody ApiBean apiBean) {
		Object data = CommonApiUtils.executeMethod(apiBean);
		return ResultBean.ok(data);
	}
}
