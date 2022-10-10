package com.csy.test.webui.jarmanage.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.bean.JarRefJmxRmiIpPortBean;
import com.csy.test.commons.jarmanage.bean.JmxRmiInfoBean;
import com.csy.test.commons.jarmanage.manage.JarManageNeetInit;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.result.ResultBean;
import com.csy.test.commons.utils.Objects;
import com.csy.test.webui.jarmanage.model.JarRumtimeMessageBean;

@RequestMapping("/api/jarmanage/")
@RestController
public class JarManageApplicationApiController {

	/**
	 * 描述：获取应用信息
	 * @author csy 
	 * @date 2022年2月12日 下午5:50:05
	 * @param jarId jarId
	 * @return ResultBean
	 */
	@GetMapping("/application")
	public ResultBean application(@RequestParam(value = "jarId", required = false)String jarId){
		//总的
		//详细的
		if (StringUtils.isBlank(jarId)){
			return ResultBean.ok("请求成功", JarManageNeetInit.getJarState().fillPidJarManageMap());
		}else{
			JarManageBean jarManageBean = JarEntityUtils.get(jarId);
			JarRefJmxRmiIpPortBean jarRefJmxRmiIpPortBean = jarManageBean.getJarRefJmxRmiIpPortBean();
			JmxRmiInfoBean jmxRmiInfoBean = null;
			try {
				jmxRmiInfoBean = JmxRmiInfoBean.newIntance(jarRefJmxRmiIpPortBean.getIp(), jarRefJmxRmiIpPortBean.getPort());
				JarRumtimeMessageBean jarRumtimeMessageBean = new JarRumtimeMessageBean();
				jarRumtimeMessageBean.jmxRmiInfoBeanTransterJarRumtimeMessageBean(jmxRmiInfoBean);
				return ResultBean.ok(jarRumtimeMessageBean);
			} finally {
				if (Objects.notNull(jmxRmiInfoBean)) {
					jmxRmiInfoBean.closeConnetor();
				}
			}
		}
	}
}
