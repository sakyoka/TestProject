package com.csy.test.webui.commonapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * 描述：公共页面跳转
 * @author csy
 * @date 2022年9月12日 下午2:55:26
 */
@RequestMapping("/view/")
@Controller
public class CommonWebUiController {
	
	/**
	 * 
	 * 描述：公共页面跳转
	 * @author csy
	 * @date 2022年9月12日 下午2:55:24
	 * @param service 业务文件夹
	 * @param page    页面
	 * @return ModelAndView
	 */
	@RequestMapping("/{service}/{page}")
	public ModelAndView page(@PathVariable String service, @PathVariable String page){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/"+ service +"/" + page);
		return modelAndView;
	}
}
