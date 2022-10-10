package com.csy.test.webui.taskmanage.task.init;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.csy.test.commons.database.annotation.Table;
import com.csy.test.commons.database.utils.TableAboutUtils;
import com.csy.test.commons.utils.ClassUtils;
import com.csy.test.commons.utils.Properties;
import com.csy.test.webui.taskmanage.base.InitTaskBase;

/**
 * 
 * 描述：初始化table任务
 * @author csy
 * @date 2022年9月29日 下午5:16:42
 */
public class InitBeanTableTask implements InitTaskBase{
	
	@Override
	public String taskName() {
		return "初始化表的任务";
	}
	
	@Override
	public int order() {
		return 9999;
	}

	
	@Override
	public void execute() {
		try {
			initTable();
		} catch (Exception e) {
			//暂时不抛异常
			//throw new RuntimeException("初始化table信息失败", e);
		}
	}
	
	/**
	 * 
	 * 描述：初始化table信息
	 * @author csy
	 * @date 2022年9月29日 下午5:29:44
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initTable() throws ClassNotFoundException, IOException{
		String[] basePackages = 
				Properties.get("init.table.scan.packages", "com.csy.test.webui").split(",");
		Set<Class> taskClzs = ClassUtils.getClassesByPackageNames(basePackages);
		if (CollectionUtils.isNotEmpty(taskClzs)) {
			taskClzs.stream().filter((cls) -> {
				if (!cls.isAnnotationPresent(Table.class)){
					return false;
				}
				Table table = (Table) cls.getAnnotation(Table.class);
				return table.enable();
			}).collect(Collectors.toList()).forEach((cls) -> {
				TableAboutUtils.generateAndIfExistsUpdate(cls);
			});
		}		
	}
}
