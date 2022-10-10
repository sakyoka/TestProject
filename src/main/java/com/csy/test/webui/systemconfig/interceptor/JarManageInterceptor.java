package com.csy.test.webui.systemconfig.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.csy.test.commons.result.ResultBean;
import com.csy.test.webui.jarmanage.utils.JwtTokenUtils;
import com.csy.test.webui.jarmanage.utils.RefreshTokenCache;
import com.csy.test.webui.jarmanage.utils.RefreshTokenRefNewCache;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：请求拦截器
 * @author csy
 * @date 2022年8月31日 下午2:53:46
 */
@Component
@Log4j2
public class JarManageInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return verfiyToken(request, response);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, 
			HttpServletResponse response, Object handler, Exception ex)throws Exception {

	}

	/**
	 * 
	 * 描述：校验token
	 * @author csy
	 * @date 2022年9月6日 上午10:46:47
	 * @param request
	 * @param response
	 * @return true通过，false不通过
	 */
	private boolean verfiyToken(HttpServletRequest request, HttpServletResponse response){
		
		//从header中获取token值
		String token = request.getHeader("token");
		token = StringUtils.isBlank(token) ? request.getParameter("token") : token;
		if (StringUtils.isBlank(token)){
			log.debug("token is null");
			response.setStatus(Integer.valueOf(ResultBean.UNAUTHORIZED));
			return false;
		}
		
		//判断token在不在缓存中，不在的话被剔除了
		if (!RefreshTokenCache.getInstance().getAll()
				.containsKey(RefreshTokenCache.PREFIX_TOKEN_KEY + token)) {
			log.debug("not exists token.");
			response.setStatus(Integer.valueOf(ResultBean.UNAUTHORIZED));
			return false;
		}
		
		//判断token有效性
		boolean tokenValid = JwtTokenUtils.tokenValid(token);
		if (!tokenValid){
			log.debug("invalid token and then verify real valid");
			synchronized (this) {
				//获取refreshToken
				String refreshToken = RefreshTokenCache.getInstance().get(token);
				//如果找不到refreshToken，直接是无效的token
				if (StringUtils.isBlank(refreshToken)) {
					log.debug("refreshToken is null.");
					response.setStatus(Integer.valueOf(ResultBean.UNAUTHORIZED));
					return false;
				}
				
				//获取refreshToken 对应的新的refreshToken，如果有数据，就是已经产生过了，不需要再进入判断
				String newRefreshToken = RefreshTokenRefNewCache.getInstance().get(refreshToken);
				if (StringUtils.isNotBlank(newRefreshToken)) {
					log.debug("invalid token but have new refreshToken.");
					this.setUserCode(request, newRefreshToken);
					return true;
				}
				
				//校验refreshToken是否过期
				DecodedJWT decodedJWT = JwtTokenUtils.getJwt(refreshToken);
				Date exprieDate = decodedJWT.getExpiresAt();
				long currentTime = System.currentTimeMillis();
				//前提5分钟
				long advanceTime = exprieDate.getTime() - 5 * 60 * 1000;
				if (currentTime <= advanceTime){
					log.debug("invalid token and create new token.");
					String userName = decodedJWT.getClaim("username").asString();
					String newToken = JwtTokenUtils.generateAccessToken(userName);
					String newRefeshToken = JwtTokenUtils.generateRefreshToken(userName);
					RefreshTokenCache.getInstance().add(newToken, newRefeshToken);
					RefreshTokenRefNewCache.getInstance().add(refreshToken, newRefeshToken);
					response.addHeader("token_exprie", newToken);
					this.setUserCode(request, newRefeshToken);
					return true;
				}
			}
			
			log.debug("invalid token.");
			response.setStatus(Integer.valueOf(ResultBean.UNAUTHORIZED));
			return false;
		}
		
		this.setUserCode(request, token);
		return true;
	}
	
	private void setUserCode(HttpServletRequest request, String token) {
		DecodedJWT decodedJWT = JwtTokenUtils.getJwt(token);
		String userName = decodedJWT.getClaim("username").asString();
		request.setAttribute("userCode", userName);
	}
}
