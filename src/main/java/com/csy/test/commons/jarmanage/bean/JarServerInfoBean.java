package com.csy.test.commons.jarmanage.bean;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * 描述：服务对象
 * @author csy
 * @date 2022年6月7日 下午5:59:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JarServerInfoBean implements Serializable{
	
	private static final long serialVersionUID = 3385343886738762743L;

	private String ip;
	
	private String port;
}
