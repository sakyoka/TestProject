package com.csy.test.webui.taskmanage.utils;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Objects;
import com.csy.test.commons.utils.Properties;
import com.csy.test.webui.systemconfig.annotation.Ignore;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * 描述：初始化任务
 * @author csy
 * @date 2022年9月29日 下午5:12:09
 */
@Log4j2
public class InitTaskUtils {

	/**
	 * 
	 * 描述：执行初始化方法
	 * @author csy
	 * @date 2022年9月12日 下午1:40:29
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void executeInitTasks(String ...basePackages) throws ClassNotFoundException, IOException {
		if (basePackages == null || basePackages.length == 0){
			basePackages = Properties.get("init.task.scan.packages", 
					"com.csy.test.webui").split(",");
		}
		@SuppressWarnings("rawtypes") 
		Set<Class> taskClzs = ClassUtils.getClassesByPackageNames(basePackages);
		if (CollectionUtils.isNotEmpty(taskClzs)) {
			List<InitTaskBase> tastList = new ArrayList<InitTaskBase>(taskClzs.size());
			taskClzs.stream().filter((cls) -> {
				if (!cls.isAnnotationPresent(Ignore.class) 
						&& Objects.notNull(cls.getInterfaces())
						&& !Modifier.isAbstract(cls.getModifiers())
						&& !Modifier.isInterface(cls.getModifiers())) {
					return ClassUtils.isContainTargetInterface(cls, InitTaskBase.class);
				}
				return false;
			}).collect(Collectors.toList()).forEach((cls) -> {
				tastList.add((InitTaskBase) ClassUtils.newInstance(cls));
			});
			
			tastList.sort((e1, e2) -> {
				return e1.order() - e2.order();
			});
			
			tastList.forEach((e) -> {
				
				String taskName = e.taskName();
				if (StringUtils.isNotBlank(taskName)){
					log.debug("开始执行任务 ： " + taskName);
				}
				
				e.execute();
				
				if (StringUtils.isNotBlank(taskName)){
					log.debug("执行任务 ：" + taskName + " 完毕。");
				}
			});
		}
	}
}
