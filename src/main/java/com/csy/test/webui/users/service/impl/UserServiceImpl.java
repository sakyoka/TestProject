package com.csy.test.webui.users.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.UUID;
import com.csy.test.commons.valid.bean.ParamValidResult;
import com.csy.test.commons.valid.utils.ValidUtils;
import com.csy.test.webui.commonapi.annotation.ServiceApi;
import com.csy.test.webui.commonapi.annotation.ServiceApiMethod;
import com.csy.test.webui.commonapi.bean.ApiBean;
import com.csy.test.webui.commonapi.bean.PageBean;
import com.csy.test.webui.commonapi.bean.PageDataBean;
import com.csy.test.webui.commonapi.utils.ApiBeanUtils;
import com.csy.test.webui.systemconfig.exception.CommonException;
import com.csy.test.webui.users.cache.UserCache;
import com.csy.test.webui.users.model.UserBean;
import com.csy.test.webui.users.service.UserService;

import cn.hutool.crypto.SecureUtil;

/**
 * 
 * 描述：用户业务接口实现类
 * @author csy
 * @date 2022年9月12日 下午12:06:14
 */
@Service
@ServiceApi(serviceClass = "user")
public class UserServiceImpl implements UserService{

	@Override
	@ServiceApiMethod
	public UserBean getUser(ApiBean apiBean) {
		Map<String, Object> params = ApiBeanUtils.paramlistToMap(apiBean);
		String userName = (String)params.get("userName");
		return UserCache.getInstance().get(userName);
	}

	@Override
	@ServiceApiMethod
	public Boolean deleteUsers(ApiBean apiBean) {
		Map<String, Object> params = ApiBeanUtils.paramlistToMap(apiBean);
		String userName = (String)params.get("userName");
		UserCache.getInstance().remove(userName);
		return true;
	}

	@Override
	@ServiceApiMethod
	public PageDataBean<UserBean> userPageData(ApiBean apiBean) {
		PageBean pageBean = ApiBeanUtils.getPageBean(apiBean);
		Objects.notNullAssert(pageBean, 
				CommonException.class, "query error, page object is not allow null");
		
		Map<String, UserBean> userMap = UserCache.getInstance().getAll();
		List<UserBean> userBeans = new ArrayList<UserBean>(pageBean.getPageSize());
		if (!userMap.isEmpty()){
			userMap.forEach((userName, userBean) -> {
				userBeans.add(userBean);
			});
		}
		return PageDataBean.listToPageDataBean(pageBean, userBeans);
	}

	@Override
	@ServiceApiMethod
	public UserBean saveUser(ApiBean apiBean) {
				
		Map<String, Object> params = ApiBeanUtils.paramlistToMap(apiBean);
		Object userObject = params.get("userObject");
		Objects.notNullAssert(userObject, 
				CommonException.class, "save error, user object is not allow null");
		UserBean userBean = JSON.toJavaObject(
				JSON.parseObject(JSON.toJSONString(userObject)), UserBean.class);
		
		if (StringUtils.isNotBlank(UserCache.DEFAULT_USER_NAME)){
			Objects.isConditionAssert(!UserCache.DEFAULT_USER_NAME.equals(userBean.getUserName()), 
					CommonException.class, "userName:" + userBean.getUserName() 
					+ " 是properties配置的用户不可编辑。");
		}
		
		ParamValidResult validResult = ValidUtils.valid(userBean);
		Objects.isConditionAssert(!validResult.getHasError(), 
				CommonException.class,  "校验信息：" + validResult.getErrorMessageMap());
		
		userBean.setUpdateTime(new Date());
		userBean.setPassWord(SecureUtil.md5(userBean.getPassWord()));
		UserCache cache = UserCache.getInstance();
		if (!cache.getAll().containsKey(userBean.getUserName())){
			userBean.setUuid(UUID.getString());
			userBean.setCreateTime(new Date());
		}
		cache.add(userBean.getUserName(), userBean);
		return userBean;
	}

}
