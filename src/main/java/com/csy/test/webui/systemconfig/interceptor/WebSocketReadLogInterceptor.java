package com.csy.test.webui.systemconfig.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.csy.test.commons.result.ResultBean;
import com.csy.test.webui.jarmanage.utils.JwtTokenUtils;
import com.csy.test.webui.jarmanage.utils.RefreshTokenCache;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：websocket 拦截
 * @author csy
 * @date 2022年9月1日 上午10:29:57
 */
@Component
@Log4j2
public class WebSocketReadLogInterceptor implements HandshakeInterceptor{

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		HttpServletRequest rs = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse hp = ((ServletServerHttpResponse)response).getServletResponse();
		String token = rs.getParameter("token");
		if (StringUtils.isBlank(token)){
			log.error("token is null");
			hp.setStatus(Integer.valueOf(ResultBean.UNAUTHORIZED));
			return false;
		}
		
		//判断token有效性
		if (!JwtTokenUtils.tokenValid(token)){
			String refreshToken = RefreshTokenCache.getInstance().get(token);
			if (!JwtTokenUtils.tokenValid(refreshToken)) {
				log.error("invalid token ");
				hp.setStatus(Integer.valueOf(ResultBean.UNAUTHORIZED));
				return false;	
			}
		}
		
		//request 参数放入 attributes中
		rs.getParameterMap().forEach((k, v) -> {
			attributes.put(k, v[0]);
		});
		
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

	}
}
