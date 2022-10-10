package com.csy.test.webui.systemconfig.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.csy.test.webui.jarmanage.utils.JwtTokenUtils;
import com.csy.test.webui.jarmanage.utils.RefreshTokenCache;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：stomp websocket 拦截器
 * @author csy
 * @date 2022年9月2日 上午11:59:39
 */
@Component
@Log4j2
public class WebSocketEmptyLogInterceptor implements ChannelInterceptor{

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
    	final StompHeaderAccessor accessor = 
    			MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    	//连接
    	if (StompCommand.CONNECT.equals(accessor.getCommand())) {
        	String token = accessor.getFirstNativeHeader("token");
    		if (StringUtils.isBlank(token)){
    			log.debug("token is null");
    			throw new RuntimeException("token is null");
    		}
    		
    		//判断token有效性
    		if (!JwtTokenUtils.tokenValid(token)){
    			String refreshToken = RefreshTokenCache.getInstance().get(token);
    			if (!JwtTokenUtils.tokenValid(refreshToken)) {
    				throw new RuntimeException("invalid token");
    			}
    		}
    	}
    	return message;
    }
}
