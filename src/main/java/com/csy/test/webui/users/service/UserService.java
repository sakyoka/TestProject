package com.csy.test.webui.users.service;

import com.csy.test.webui.commonapi.bean.ApiBean;
import com.csy.test.webui.commonapi.bean.PageDataBean;
import com.csy.test.webui.users.model.UserBean;

/**
 * 
 * 描述：用户业务接口
 * @author csy
 * @date 2022年9月12日 下午12:05:24
 */
public interface UserService {

	/**
	 * 
	 * 描述：获取用户
	 */
	UserBean getUser(ApiBean apiBean);

	/**
	 * 
	 * 描述：删除用户
	 */
	Boolean deleteUsers(ApiBean apiBean);
	
	/**
	 * 
	 * 描述：用户分页数据
	 */
	PageDataBean<UserBean> userPageData(ApiBean apiBean);
	
	/**
	 * 
	 * 描述：保存用户数据
	 */
	UserBean saveUser(ApiBean apiBean);
}
