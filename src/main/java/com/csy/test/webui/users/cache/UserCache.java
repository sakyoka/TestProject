package com.csy.test.webui.users.cache;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.csy.test.commons.jarmanage.base.Cache;
import com.csy.test.commons.jarmanage.constants.JarInitBaseConstants;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.Properties;
import com.csy.test.commons.utils.file.FileUtils;
import com.csy.test.webui.users.model.UserBean;

public class UserCache implements Cache<UserBean>{
	
	/**默认账号*/
	public static final String DEFAULT_USER_NAME = Properties.get("login.default.username");
	
	/**默认密码*/
	public static final String DEFAULT_PASSWORD = Properties.get("login.default.password");
	
	/**用户存储文件*/
	private static String JAR_LOGIN_USERS_FILE;
	
	private static final Map<String, UserBean> USER_CACHE_MAP = 
			new HashMap<String, UserBean>(12);
	
	private static final UserCache USER_CACHE = new UserCache();
	
	public static UserCache getInstance(){
		return USER_CACHE;
	}
	
	static{
		String rootPath = JarInitBaseConstants.getJarRootPath();
		String userPath = rootPath + File.separator + "users";
		JAR_LOGIN_USERS_FILE = userPath + File.separator + "users.json";
		FileUtils.ifNotExistsCreate(JAR_LOGIN_USERS_FILE);
		
		String conents = FileUtils.read(JAR_LOGIN_USERS_FILE);
		if (StringUtils.isNotBlank(conents)){
			@SuppressWarnings("unchecked")
			Map<String, String> localContectMap = JSON.parseObject(conents, Map.class);
			if (Objects.notNull(localContectMap)){
				Set<Entry<String, String>> sets = localContectMap.entrySet();
				for (Entry<String, String> set:sets){
					USER_CACHE_MAP.put(set.getKey(), 
							JSON.toJavaObject((JSON)JSON.toJSON(set.getValue()), UserBean.class));
				}
			}
		}
	}

	@Override
	public UserBean get(String key) {
		return USER_CACHE_MAP.get(key);
	}

	@Override
	public synchronized void add(String key, UserBean value) {
		USER_CACHE_MAP.put(key, value);
		FileUtils.writeFile(JAR_LOGIN_USERS_FILE, 
				JSON.toJSONString(USER_CACHE_MAP, SerializerFeature.PrettyFormat));
	}

	@Override
	public synchronized void remove(String key) {
		USER_CACHE_MAP.remove(key);
		FileUtils.writeFile(JAR_LOGIN_USERS_FILE, 
				JSON.toJSONString(USER_CACHE_MAP, SerializerFeature.PrettyFormat));
	}

	@Override
	public Map<String, UserBean> getAll() {
		return USER_CACHE_MAP;
	}

}
