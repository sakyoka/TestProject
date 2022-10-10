package com.csy.test.webui.jarmanage.model;

import java.io.InputStream;

import lombok.Data;

/**
 * 
 * 描述：jar log socket记录对象
 * @author csy
 * @date 2022年9月2日 上午9:09:22
 */
@Data
public class JarLogWebSocketBean {

	/** socket session id*/
	private String sessionId;
	
	/** jarId*/
	private String jarId;
	
	/** 是否展示前端传来的信息*/
	private Boolean showMessage = false;
	
	/**线程对象*/
    private Process process;
    
    /**日志文件流*/
    private InputStream inputStream;
}
