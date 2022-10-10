package com.csy.test.webui.commonapi.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.excel.utils.CollectionUtils;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.Properties;
import com.csy.test.webui.commonapi.annotation.ServiceApi;
import com.csy.test.webui.commonapi.annotation.ServiceApiMethod;
import com.csy.test.webui.commonapi.bean.ApiBean;
import com.csy.test.webui.systemconfig.annotation.Ignore;
import com.csy.test.webui.systemconfig.component.SpringContextHolder;
import com.csy.test.webui.systemconfig.exception.CommonException;

/**
 * 
 * 描述：api方法工具
 * @author csy
 * @date 2022年9月12日 下午1:10:39
 */
public class CommonApiUtils {
	
	/**业务类型方法对应真实方法*/
	private static final Map<String, Method> SERVICE_METHOD_REF_METHOD = new HashMap<String, Method>(12);
	
	/**方法名对应的类*/
	private static final Map<String, Class<?>> SERVICE_METHOD_REF_CLS = new HashMap<String, Class<?>>(12);
	
	private static final String LINK_TAG = "_";
	
	/**
	 * 
	 * 描述：收集方法
	 * @author csy
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @date 2022年9月12日 下午1:12:09
	 */
	@SuppressWarnings("unchecked")
	public static synchronized void collectMethods(String ...packages) throws ClassNotFoundException, IOException {
		if (Objects.isNull(packages) 
				|| packages.length == 0) {
			packages = Properties.get("common.api.scan.packages", "com.csy.test.webui").split(",");
		}
		@SuppressWarnings("rawtypes")
		Set<Class> taskClzs = ClassUtils.getClassesByPackageNames(packages);
		if (CollectionUtils.isNotEmpty(taskClzs)) {
			taskClzs.stream().filter((cls) -> {
				if (!cls.isAnnotationPresent(Ignore.class) 
						&& cls.isAnnotationPresent(ServiceApi.class)) {
					ServiceApi serviceApi = (ServiceApi) cls.getAnnotation(ServiceApi.class);
					return serviceApi.normalUse();
				}
				return false;
			}).collect(Collectors.toList()).forEach((cls) -> {
				ServiceApi serviceApi = (ServiceApi) cls.getAnnotation(ServiceApi.class);
				String serviceClass = serviceApi.serviceClass();
				serviceClass = (StringUtils.isBlank(serviceClass) ? cls.getName() : serviceClass);
				Method[] methods = cls.getDeclaredMethods();
				for (Method method:methods) {
					if (method.isAnnotationPresent(ServiceApiMethod.class)) {
						ServiceApiMethod serviceApiMethod = method.getAnnotation(ServiceApiMethod.class);
						if (serviceApiMethod.normalUse()) {
							String serviceMethod = serviceApiMethod.serviceMethod();
							serviceMethod = StringUtils.isBlank(serviceMethod) ? method.getName() : serviceMethod;
							String key = serviceClass + LINK_TAG + serviceMethod;
							Objects.isConditionAssert(!SERVICE_METHOD_REF_METHOD.containsKey(key), 
									CommonException.class, 
									String.format("方法名重复请检查，key:%s，class：%s，serviceMethod：%s", key, cls.getName(), serviceMethod));
							//分类加方法名组合
							SERVICE_METHOD_REF_METHOD.put(key, method);
							SERVICE_METHOD_REF_CLS.put(key, cls);
						}
					}
				}
			});
		}		
	}
	
	/**
	 * 
	 * 描述：执行方法
	 * @author csy
	 * @date 2022年9月12日 下午1:36:30
	 * @param apiBean 执行参数对象（统一是这个参数）
	 * @return 执行结果
	 */
	public static Object executeMethod(ApiBean apiBean) {
		//组合标识
		String key = apiBean.getServiceClass() + LINK_TAG + apiBean.getServiceMethod();
		
		//获取方法
		Method method = SERVICE_METHOD_REF_METHOD.get(key);
		Objects.notNullAssert(method, CommonException.class, 
				String.format("找不到对应方法，分类（serviceClass）：%s，方法名（serviceMethod）:%s" , 
						apiBean.getServiceClass(), apiBean.getServiceMethod()));
		//获取业务类
		Class<?> cls = SERVICE_METHOD_REF_CLS.get(key);
		Object object = SpringContextHolder.getBean(cls);
		
		ServiceApiMethod serviceApiMethod = method.getAnnotation(ServiceApiMethod.class);
		//增强参数功能，参数除了可以是ApiBean，还可以是paramType类型对象
		Class<?>[] paramTypes = serviceApiMethod.paramTypes();
		List<Object> paramObjects = new ArrayList<Object>(5);
		if (paramTypes.length == 0){
			paramObjects.add(apiBean);
		}else{
			for (Class<?> paramType:paramTypes){
				if (paramType == ApiBean.class){
					paramObjects.add(apiBean);
				}else{
					paramObjects.add(ApiBeanUtils.toBean(apiBean, paramType));
				}
			}
		}
		
		try {
			Object result = method.invoke(object, paramObjects.toArray());
			return result;
		} catch (Exception e) {
			
			if (e.getCause() instanceof CommonException){
				throw new CommonException(e.getCause().getMessage());
			}

			throw new CommonException("执行方法失败", e);
		} 
	}
}
