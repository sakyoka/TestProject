package com.csy.test.webui.jarmanage.websocket;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.csy.test.commons.jarmanage.base.impl.LogKeyLocalCache;
import com.csy.test.commons.jarmanage.bean.JarManageBean;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.jarmanage.utils.JarEntityUtils;
import com.csy.test.commons.jarmanage.utils.JarTaskUtils;
import com.csy.test.commons.utils.Command;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.PrintUtils;
import com.csy.test.commons.utils.UUID;
import com.csy.test.webui.jarmanage.constants.JarSystemConstant;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：读取日志信息
 * <br>PS:由于在controller下面，被ResultExceptionAop切面切到(异常as it is not annotated with @ServerEndpoint)，
 *     所以转移到这个包下面
 * <br>弃用：这个ServerEndpoint + Component 在拦截器匹配不上 @see JarLogWebSocketHandler
 * @author csy
 * @date 2022年1月27日 上午11:26:43
 */
@ServerEndpoint("/jarlog")
@Component
@Deprecated
@Log4j2
public class JarLogWebSocket {

    private Process process;
    
    private InputStream inputStream;
    
    private String jarId = null;
    
    private boolean showMessage = false;
    
    private String openLogConsoleId;

    @OnOpen
    public void onOpen(Session session) {
    	
    	Map<String, List<String>> params = session.getRequestParameterMap(); 
        
    	this.jarId = params.get("jarId").get(0); 
    	this.openLogConsoleId = UUID.getString() + "-" + this.jarId;
    	LogKeyLocalCache.getInstance().add(openLogConsoleId, jarId);
        log.debug(PrintUtils.getFormatString("接收到jarId:%s", jarId));
        
        if (params.containsKey("showMessage")){
        	this.showMessage = Boolean.valueOf(params.get("showMessage").get(0));
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
					session.getBasicRemote().sendText("系统日志文件不存在，并且确定日志是否有输出到，path：" + systemLog);
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
		process = command.getProcess();
		inputStream = process.getInputStream();
		
		JarTaskUtils.getLogExecutorService().execute(() -> {
	        String line;
	        BufferedReader reader = null;
	        try {
	        	reader = new BufferedReader(new InputStreamReader(inputStream));
	            while((line = reader.readLine()) != null) {
	                session.getBasicRemote().sendText(line + "<br>");
	            }
	        } catch (IOException e) {
	        	
	        }
		});

    }

	@OnMessage
    public void onMessage(String message, Session session){
    	if (showMessage){
    		log.debug(PrintUtils.getFormatString("socket onmessage ==> jarId:%s, 接收到信息:%s", jarId, message));
    	}
    }

    @OnClose
    public void onClose(Session session) {
    	
    	LogKeyLocalCache.getInstance().remove(openLogConsoleId);
    	
    	this.close();
    	log.debug(PrintUtils.getFormatString("socket已关闭，jarId:%s" , jarId));
    }

    @OnError
    public void onError(Throwable thr) {
    	this.close();
    	log.debug(PrintUtils.getFormatString("socket异常，jarId:%s, errorMessage:%s" , jarId, thr.getMessage()));
    }
    
    /**
     * 描述：关闭资源
     * @author csy 
     * @date 2022年1月27日 下午5:20:57
     */
    private void close(){
    	
    	//这里应该先停止命令, 然后再关闭流
        if(process != null){
        	process.destroy();
        }
        
        close(inputStream);
    }
    
    /**
     * 描述：关闭
     * @author csy 
     * @date 2022年2月17日 下午6:15:48
     * @param closeable
     */
    private void close(Closeable closeable){
        try {
			if (Objects.notNull(closeable)){
				closeable.close();
			}
		} catch (Exception e) {}   	
    }
}
