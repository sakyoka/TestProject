package com.csy.test.webui.jarmanage.websocket;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.jarmanage.utils.JarTaskUtils;
import com.csy.test.commons.utils.Command;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.webui.jarmanage.constants.JarSystemConstant;
import com.csy.test.webui.jarmanage.model.JarLogWebSocketBean;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：@see JarLogWebSocket 对应改造。使用WebSocketHandler处理消息
 * @author csy
 * @date 2022年9月2日 上午9:15:41
 */
@Log4j2
public class JarLogWebSocketHandler implements WebSocketHandler{

	/**存储session id 对应的数据*/
	private static final Map<String, JarLogWebSocketBean> sessionLogSocketBeanMap = 
			new ConcurrentHashMap<String, JarLogWebSocketBean>(24);
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		JarLogWebSocketBean jarLogWebSocketBean = new JarLogWebSocketBean();
		jarLogWebSocketBean.setSessionId(session.getId());
		sessionLogSocketBeanMap.put(session.getId(), jarLogWebSocketBean);
		
		Map<String, Object> paramsMap = session.getAttributes();
    	String jarId = (String)paramsMap.get("jarId");
    	jarLogWebSocketBean.setJarId(jarId);
        log.debug(PrintUtils.getFormatString("接收到jarId:%s, sessionid:%s", jarId, session.getId()));
        
        if (paramsMap.containsKey("showMessage")){
        	boolean showMessage = Boolean.valueOf(paramsMap.get("showMessage").toString());
        	jarLogWebSocketBean.setShowMessage(showMessage);
        }
		
        String logPath = null;
        if (JarSystemConstant.isSystemType(jarId)){
        	//本系统日志读取
        	//原先方案 截取控制台输出，但是由于多个窗口打开，只能一个窗口看问题，不采用
        	//备用方案：1、采取广播式(需要改造页面及提供接口) 
            //         2、直接读取日志文件（但是要配置固定路径）这个比较方便
        	String systemLog = JarInitBaseConstants.getSystemLogFilePath();
        	if (!new File(systemLog).exists()){
        		try {
        			session.sendMessage(new TextMessage("系统日志文件不存在，并且确定日志是否有输出到，path：" + systemLog));
				} catch (IOException e) {
					
				}
        		return ;
        	}
        	logPath = systemLog;
        }else{
            JarManageBean jarManageBean = JarEntityUtils.get(jarId);
    		String fullLogPath = JarInitBaseConstants.getJarRootPath() + jarManageBean.getLogPath();
    		logPath = fullLogPath;
        }
        String cmd = "tail -f " +logPath; 
        
        log.debug(PrintUtils.getFormatString("show log cmd >> %s", cmd));
		
		Command command = Command. getBuilder().commandStr(cmd).autoReadStream(false);
		command.exec();
		Process process = command.getProcess();
		jarLogWebSocketBean.setProcess(process);
		InputStream inputStream = process.getInputStream();
		jarLogWebSocketBean.setInputStream(inputStream);
		
		JarTaskUtils.getLogExecutorService().execute(() -> {
	        String line;
	        BufferedReader reader = null;
	        try {
	        	reader = new BufferedReader(new InputStreamReader(inputStream));
	            while((line = reader.readLine()) != null) {
	                session.sendMessage(new TextMessage(line + "<br>"));
	            }
	        } catch (IOException e) {
	        	
	        }
		});
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (sessionLogSocketBeanMap.containsKey(session.getId())) {
			JarLogWebSocketBean jarLogWebSocketBean = sessionLogSocketBeanMap.get(session.getId());
			if (jarLogWebSocketBean.getShowMessage()) {
				log.debug(PrintUtils.getFormatString("socket onmessage ==> jarId:%s, 接收到信息:%s", jarLogWebSocketBean.getJarId(), message.getPayload()));
			}
		}
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		if (sessionLogSocketBeanMap.containsKey(session.getId())) {
			JarLogWebSocketBean jarLogWebSocketBean = sessionLogSocketBeanMap.get(session.getId());
			log.debug(PrintUtils.getFormatString("socket异常，jarId:%s, errorMessage:%s" , jarLogWebSocketBean.getJarId(), exception.getMessage()));
		}
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		JarLogWebSocketBean jarLogWebSocketBean = sessionLogSocketBeanMap.get(session.getId());
		if (Objects.notNull(jarLogWebSocketBean.getProcess())) {
			jarLogWebSocketBean.getProcess().destroy();
		}
		if (Objects.notNull(jarLogWebSocketBean.getInputStream())) {
			try {
				jarLogWebSocketBean.getInputStream().close();
			} catch (Exception e) {}
		}
		String jarId = jarLogWebSocketBean.getJarId();
		jarLogWebSocketBean = null;
		log.debug(PrintUtils.getFormatString("socket已关闭，jarId:%s, sessionid:%s, closeStatus:%s" , jarId, session.getId(), closeStatus));
		sessionLogSocketBeanMap.remove(session.getId());
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

}
