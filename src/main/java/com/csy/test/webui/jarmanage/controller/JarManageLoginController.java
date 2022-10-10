package com.csy.test.webui.jarmanage.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.csy.test.commons.result.ResultBean;
import com.csy.test.commons.utils.Objects;
import com.csy.test.webui.jarmanage.utils.JwtTokenUtils;
import com.csy.test.webui.jarmanage.utils.RefreshTokenCache;
import com.csy.test.webui.jarmanage.utils.RefreshTokenRefNewCache;
import com.csy.test.webui.systemconfig.exception.CommonException;
import com.csy.test.webui.users.cache.UserCache;
import com.csy.test.webui.users.model.UserBean;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：jar manage login controller
 * @author csy
 * @date 2022年8月31日 下午3:01:41
 */
@RequestMapping("/jarmanage")
@Controller
@Log4j2
public class JarManageLoginController {
	
	/**
	 * 
	 * 描述：登录页面
	 * @author csy
	 * @date 2022年8月31日 下午3:06:59
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView loginpage(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/login/login");
		return modelAndView;
	}
	
	/**
	 * 
	 * 描述：登录方法
	 * @author csy
	 * @date 2022年8月31日 下午3:08:15
	 * @return ResultBean
	 */
	@PostMapping("/login")
	@ResponseBody
	public ResultBean login(HttpServletRequest request){
		
		String username = request.getParameter("username");
		if (StringUtils.isBlank(username)){
			return ResultBean.badRequest("username is blank", null);
		}
		
		String password = request.getParameter("password");
		if (StringUtils.isBlank(password)){
			return ResultBean.badRequest("password is blank", null);
		}
		
		String token = JwtTokenUtils.generateAccessToken(username);
		String refreshToken = JwtTokenUtils.generateRefreshToken(username);
		RefreshTokenCache.getInstance().add(token, refreshToken);
		//校验账号密码
		//校验通过生成token
		if (UserCache.DEFAULT_USER_NAME.equals(username)){
			if (this.checkDefaultUser(username, password)){
				return ResultBean.ok(token);
			}
		}else{
			UserBean userBean = UserCache.getInstance().get(username);
			if (Objects.notNull(userBean)){
				String _password = userBean.getPassWord();
				if (password.equals(_password)){
					return ResultBean.ok(token);
				}
			}
		}
		return ResultBean.builder()
				.code(ResultBean.UNAUTHORIZED)
				.msg("校验失败")
				.build();
	}
	
	/**
	 * 
	 * 描述：检查默认账号
	 * @author csy
	 * @date 2022年8月31日 下午6:16:17
	 * @param username
	 * @param password
	 * @return true通过，false不通过
	 */
	private boolean checkDefaultUser(String username, String password){
		
		if (StringUtils.isBlank(UserCache.DEFAULT_USER_NAME)) {
			log.warn("没有设置默认账号，请在配置文件设置login.default.username、login.default.password");
			throw new CommonException("没有设置默认账号，请在配置文件设置login.default.username、login.default.password");
		}
		
		if (UserCache.DEFAULT_USER_NAME.equals(username)){
			if (UserCache.DEFAULT_PASSWORD.equals(password)){
				return true;
			}else{
				throw new CommonException("password incorrect");
			}
		}
		
		throw new CommonException("not match account");
	}
	
	/**
	 * 
	 * 描述：登出方法
	 * @author csy
	 * @date 2022年8月31日 下午3:08:22
	 * @param token
	 * @return ResultBean
	 */
	@PostMapping("/loginout")
	@ResponseBody
	public ResultBean loginout(HttpServletRequest request){
		
		String token = request.getHeader("token");
		
		if (!JwtTokenUtils.tokenValid(token)) {
			return ResultBean.ok("退出成功");
		}
		
		RefreshTokenCache refreshTokenCache = RefreshTokenCache.getInstance();
		String refreshToken = refreshTokenCache.get(token);
		refreshTokenCache.remove(token);
		RefreshTokenRefNewCache.getInstance().remove(refreshToken);
		return ResultBean.ok("退出成功");
	}
}
