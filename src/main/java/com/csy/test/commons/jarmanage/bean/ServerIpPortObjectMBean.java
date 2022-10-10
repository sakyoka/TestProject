package com.csy.test.commons.jarmanage.bean;

/**
 * 
 * 描述：定义ip、port
 * @author csy
 * @date 2022年6月7日 上午9:32:47
 */
public interface ServerIpPortObjectMBean {

	/**
	 * 描述：获取ip信息
	 * @author csy 
	 * @date 2022年6月7日 上午9:33:08
	 * @return ip
	 */
	public String getIp();
	
	/**
	 * 描述：获取端口
	 * @author csy 
	 * @date 2022年6月7日 上午9:33:19
	 * @return 端口
	 */
	public String getPort();
}
