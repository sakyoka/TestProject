package com.csy.test.webui.systemconfig.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.csy.test.webui.jarmanage.websocket.JarLogWebSocketHandler;
import com.csy.test.webui.systemconfig.interceptor.WebSocketEmptyLogInterceptor;
import com.csy.test.webui.systemconfig.interceptor.WebSocketReadLogInterceptor;

/**
 * 
 * 描述：注册ServerEndpointExporter
 * @author csy
 * @date 2022年1月27日 上午11:13:26
 */
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer implements WebSocketConfigurer{
	
	@Autowired
	WebSocketReadLogInterceptor webSocketReadLogInterceptor;
	
	@Autowired
	WebSocketEmptyLogInterceptor webSocketEmptyLogInterceptor;
	
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//服务节点
		registry.addEndpoint("/server")
		        .setAllowedOrigins("*")
		        .withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		//队列节点
		registry.enableSimpleBroker("/topic");
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		
		// /ws/handler/jarlog 路径就是前端要访问的路径 类似@ServerEndpoint("/jarlog")
		registry.addHandler(new JarLogWebSocketHandler(), "/ws/handler/jarlog")
		        .setAllowedOrigins("*")
		        .addInterceptors(webSocketReadLogInterceptor);
		
		/* use configureClientInboundChannel interceptors
		 * 放弃addHandler方式， 目前测试这种使广播订阅地址失效 
		*/
//		registry.addHandler(new JarEmptyLogStompSocketHandler(), "/server/**")
//				.addInterceptors(webSocketInterceptor)
//				.setAllowedOrigins("*")  
//		        .withSockJS();
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		//添加路由拦截，判断请求头是否带上token、token是否有效
		registration.setInterceptors(webSocketEmptyLogInterceptor);
	}
}
